package com.example.Company.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.w3c.dom.Document;

import com.example.Company.model.BankStatement;
import com.example.Company.model.BankStatementItem;
import com.example.Company.model.BusinessPartner;
import com.example.Company.model.Company;
import com.example.Company.model.Invoice;
import com.example.Company.repository.BankStatementRepository;
import com.example.Company.model.PaymentOrderModel;
import com.example.Company.model.pojo.PaymentOrderPojo;
import com.example.Company.repository.BusinessPartnerRepository;
import com.example.Company.repository.CompanyRepository;
import com.example.Company.repository.InvoiceRepository;
import com.example.Company.repository.PaymentOrderRepository;
import com.example.Company.service.XMLsecurity.EncryptedStringXmlAdapter;
import com.example.Company.service.XMLsecurity.KeyStoreReader;
import com.example.Company.service.jaxws.ProcessBankStatementRequest;
import com.example.Company.service.jaxws.ProcessBankStatementRequestResponse;
import com.example.Company.service.jaxws.ProcessPaymentOrder;
import com.example.Company.service.jaxws.ProcessPaymentOrderResponse;
import com.example.service.bankstatementrequest.BankStatementRequest;
import com.example.service.paymentorder.PaymentOrder;
import com.example.service.paymentorder.TCompanyData;

@Service
public class SOAPClientServiceImpl extends WebServiceGatewaySupport implements SOAPClientService {

	private String tempKey;
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private BusinessPartnerRepository businessPartnerRepository;
	
	@Autowired
	private BankStatementRepository bankStatementRepository;

	@Autowired
	private PaymentOrderRepository paymentOrderRepository;
	
	@Override
	public String sendPaymentOrder(PaymentOrderPojo paymentOrderModel) {
		
		
		Invoice invoice = invoiceRepository.findOne(paymentOrderModel.getInvoiceId());
		double totalDue = invoice.getTotalDue() - paymentOrderModel.getAmount();
		if(totalDue < 0){
			totalDue = 0;
		}
		invoice.setTotalDue(totalDue);
		invoiceRepository.save(invoice);
		
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setAmount(new BigDecimal(paymentOrderModel.getAmount()));
		paymentOrder.setUrgent(paymentOrderModel.getUrgent());
		paymentOrder.setPaymentPurpose("Payment based on an invoice");
		paymentOrder.setCurrency(invoice.getCurrency());
		paymentOrder.setMessageId("?");
		try {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(invoice.getDateOfValue());
			XMLGregorianCalendar dateOfValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			c.setTime(new Date());
			XMLGregorianCalendar dateOfPayment = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			paymentOrder.setDateOfValue(dateOfValue);
			paymentOrder.setDateOfPayment(dateOfPayment);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		List<Company> debtorList = companyRepository.findByCompanyPIB(invoice.getBuyerPIB());
		if(debtorList.isEmpty()){
			return "CompanyPIBError";
		}
		List<BusinessPartner> creditorList = businessPartnerRepository.findByPartnerPIB(invoice.getSupplierPIB());
		if(creditorList.isEmpty()){
			return "BusinessPartnerPIBError";
		}
		Company debtor = debtorList.get(0);
		BusinessPartner creditor =  creditorList.get(0);
		
		TCompanyData debtorData = new TCompanyData();
		debtorData.setInfo(debtor.getName() + " " + debtor.getCompanyAddress());
		debtorData.setAccountNumber(debtor.getCompanyAccount());
		debtorData.setModel(Integer.parseInt(debtor.getModel()));
		debtorData.setReferenceNumber(debtor.getReferenceNumber());
		
		TCompanyData creditorData = new TCompanyData();
		creditorData.setInfo(creditor.getName() + " " + creditor.getPartnerAddress());
		creditorData.setAccountNumber(creditor.getPartnerAccount());
		creditorData.setModel(Integer.parseInt(creditor.getModel()));
		creditorData.setReferenceNumber(creditor.getReferenceNumber());
		
		paymentOrder.setCreditor(creditorData);
		paymentOrder.setDebtor(debtorData);
		
		ProcessPaymentOrder ppo = new ProcessPaymentOrder();
		ppo.setArg0(paymentOrder);
		
		sendSessionKey();
		
		PaymentOrderModel paymentOrderDB = new PaymentOrderModel(debtor.getCompanyAccount(),
				debtor.getReferenceNumber(),
				debtor.getName(),
				Integer.parseInt(debtor.getModel()), 
				creditor.getPartnerAccount(), 
				creditor.getReferenceNumber(), 
				creditor.getName(), 
				Integer.parseInt(creditor.getModel()),
				invoice.getDateOfValue(), 
				new Date(), 
				"Payment based on an invoice",
				paymentOrderModel.getAmount(), 
				invoice.getCurrency(), 
				paymentOrderModel.getUrgent());
		
		paymentOrderRepository.save(paymentOrderDB);
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
	    marshaller.setClassesToBeBound(ProcessPaymentOrder.class, ProcessPaymentOrderResponse.class);
	    setMarshaller(marshaller);
	    setUnmarshaller(marshaller);
		ppo = signWithCert(ppo);
		String uri = debtor.getBankUrl();
		Object o = getWebServiceTemplate().marshalSendAndReceive(uri, ppo);
		ProcessPaymentOrderResponse response = (ProcessPaymentOrderResponse) o;
		return response.getReturn();
	}

	@Override
	public void sendSessionKey() {
		 URL url1;
			try {
				String encodedKey = Base64.getEncoder().encodeToString(generateAndEncryptSessionKey());
				url1 = new URL("https://localhost:8080/bank/receiveKey");
				HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
				conn1.setDoOutput(true);
				conn1.setUseCaches( false );
				conn1.setRequestMethod("POST");
				conn1.setRequestProperty("Content-Type", "application/json");
		        conn1.setRequestProperty( "charset", "utf-8");
		        OutputStream os1 = conn1.getOutputStream();
				os1.write(encodedKey.getBytes());
				os1.close();
				
				if (conn1.getResponseCode() != HttpURLConnection.HTTP_OK) {
					throw new RuntimeException("Failed : HTTP error code : " + conn1.getResponseCode());
				}

				BufferedReader br1 = new BufferedReader(new InputStreamReader((conn1.getInputStream())));

				String output1;
				System.out.println("Output from Server .... \n");
				while ((output1 = br1.readLine()) != null) {
					System.out.println(output1);
				}
				conn1.disconnect();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
	@Override
	public byte[] generateAndEncryptSessionKey() {
		KeyStoreReader ksReader = new KeyStoreReader();		
		PublicKey bankPublicKey = ksReader.readCertificate("bank.p12", "tomcat", "tomcat").getPublicKey();
		
		KeyGenerator keyGen;
		try {
			//generate session key
			keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(192);
			SecretKey secretKey = keyGen.generateKey();
			String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			EncryptedStringXmlAdapter.setKey(encodedKey);
			tempKey = encodedKey;
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");   
			cipher.init(Cipher.ENCRYPT_MODE, bankPublicKey);
			byte[] cipherBytes = cipher.doFinal(secretKey.getEncoded());
			return cipherBytes;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ProcessPaymentOrder signWithCert(ProcessPaymentOrder ppo) {
		try {
			JAXBContext jc = JAXBContext.newInstance(ProcessPaymentOrder.class);
			Marshaller marshaller = jc.createMarshaller();
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder docBuilder = domFactory.newDocumentBuilder();
			EncryptedStringXmlAdapter.setKey("inv");
			Document docum = docBuilder.newDocument();
			DOMResult domResult = new DOMResult(docum);
			marshaller.marshal(ppo, domResult);
			Document doc = (Document) domResult.getNode();
			doc.normalizeDocument();

			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

			Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null),
					Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
					null, null);

			SignedInfo si = fac.newSignedInfo(
					fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
					fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

			// Load the KeyStore and get the signing key and certificate.
			KeyStoreReader ks = new KeyStoreReader();

			X509Certificate cert = (X509Certificate) ks.readCertificate("keystore.p12", "tomcat", "tomcat");

			// Create the KeyInfo containing the X509Data.
			KeyInfoFactory kif = fac.getKeyInfoFactory();
			@SuppressWarnings("rawtypes")
			List x509Content = new ArrayList();
			x509Content.add(cert.getSubjectX500Principal().getName());
			x509Content.add(cert);
			X509Data xd = kif.newX509Data(x509Content);
			KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DOMSignContext dsc = new DOMSignContext(ks.readPrivateKey("keystore.p12", "tomcat", "tomcat", "tomcat"),
					doc.getDocumentElement());

			XMLSignature signature = fac.newXMLSignature(si, ki);
			signature.sign(dsc);
			EncryptedStringXmlAdapter.setKey(tempKey);
			DOMSource source = new DOMSource(domResult.getNode());
			ProcessPaymentOrder ret = (ProcessPaymentOrder) jc.createUnmarshaller().unmarshal(source);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(ret, System.out);
			
			return ret;

			
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String sendBankStatementRequest(BankStatementRequest bankStatementRequest) {
		
		bankStatementRequest.setSectionNumber(1); //ispraviti
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
	    marshaller.setClassesToBeBound(ProcessBankStatementRequest.class, ProcessBankStatementRequestResponse.class);
	    ProcessBankStatementRequest processBankStatementRequest = new ProcessBankStatementRequest();
	    processBankStatementRequest.setArg0(bankStatementRequest);
	    
	    setMarshaller(marshaller);
	    setUnmarshaller(marshaller);
		String uri = "https://localhost:8080/ws/bankstatement";
		Object o = getWebServiceTemplate().marshalSendAndReceive(uri, processBankStatementRequest);
		ProcessBankStatementRequestResponse response = (ProcessBankStatementRequestResponse) o;
		com.example.service.bankstatement.BankStatement bs = response.getReturn();
		
		BankStatement bankStatement = new BankStatement(bs.getAccountNumber(), bs.getSectionNumber(),
				bs.getDate().toGregorianCalendar().getTime(), bs.getPreviousBalance().doubleValue(), 
				bs.getNumberOfDeposits(), bs.getTotalDeposited().doubleValue(), bs.getNumberOfWithdrawals(), 
				bs.getTotalWithdrawn().doubleValue(), bs.getNewBalance().doubleValue());
		
		ArrayList<BankStatementItem> bankStatementItems = new ArrayList<BankStatementItem>();
		for (com.example.service.bankstatement.BankStatement.BankStatementItem bsi : bs.getBankStatementItem()) {
			BankStatementItem bankStatementItem = new BankStatementItem(bsi.getDebtor().getInfo(), bsi.getPaymentPurpose(),
					bsi.getCreditor().getInfo(), bsi.getDateOfPayment().toGregorianCalendar().getTime(), 
					bsi.getDateOfValue().toGregorianCalendar().getTime(), bsi.getDebtor().getAccountNumber(), bsi.getDebtor().getModel(),
					bsi.getDebtor().getReferenceNumber(), bsi.getCreditor().getAccountNumber(), bsi.getCreditor().getModel(), 
					bsi.getCreditor().getReferenceNumber(), bsi.getTotal().doubleValue(), bsi.getDirection(), bankStatement);
			bankStatementItems.add(bankStatementItem);
		}
		
		bankStatement.setBankStatementItems(bankStatementItems);
		
		bankStatementRepository.save(bankStatement);
				
		return "OK";
	}

	


	
	

}
