package com.example.Bank.service;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.Bank.service.jaxws.ProcessPaymentOrder;
import com.example.service.paymentorder.PaymentOrder;
import com.example.service.security.EncryptedStringXmlAdapter;
import com.example.service.security.KeyStoreReader;
import com.example.service.security.X509KeySelector;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Override
	public void decryptSessionKey(byte[] key) {
		KeyStoreReader ks = new KeyStoreReader();
		PrivateKey privateKey = ks.readPrivateKey("keystore.p12", "tomcat", "tomcat", "tomcat");
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");  
			cipher.init(Cipher.DECRYPT_MODE, privateKey);  
			byte[] sessionKeyBytes = cipher.doFinal(key);
			String sessionKey = Base64.getEncoder().encodeToString(sessionKeyBytes);
			EncryptedStringXmlAdapter.setKey(sessionKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}   
	}

	@Override
	public void validateWithSchema(Object object) {
		File schemaFile = null;
		Document doc = null;
		if(object instanceof PaymentOrder){
			schemaFile = new File(getClass().getClassLoader().getResource("paymentOrder.xsd").getFile());
			PaymentOrder paymentOrder = (PaymentOrder) object;
			try {
				JAXBContext jc = JAXBContext.newInstance(PaymentOrder.class);
				Marshaller marshaller = jc.createMarshaller();
			    DOMResult domResult = new DOMResult();
			    marshaller.marshal(paymentOrder, domResult);
				doc = (Document) domResult.getNode();
				jc.createUnmarshaller().unmarshal(doc);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Source xmlFile = new DOMSource(doc);
		try {
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
			System.out.println("XML is valid");
		} catch (SAXException e) {
			System.out.println("XML is NOT valid reason:" + e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean validateSignature(Object object) {
		if (object instanceof ProcessPaymentOrder) {
			ProcessPaymentOrder processPaymentOrder = (ProcessPaymentOrder) object;
			JAXBContext jc;
			try {
				jc = JAXBContext.newInstance(ProcessPaymentOrder.class);
				EncryptedStringXmlAdapter.setKey("inv");
				Marshaller marshaller = jc.createMarshaller();
				DOMResult domResult = new DOMResult();
				marshaller.marshal(processPaymentOrder, domResult);
				Document doc = (Document) domResult.getNode();
				doc.normalizeDocument();

				XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

				NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
				if (nl.getLength() == 0) {
					throw new Exception("Cannot find Signature element");
				}
				((Element) nl.item(0)).removeAttribute("xmlns:SOAP-ENV");
				((Element) nl.item(0)).removeAttribute("xmlns:ns4");
				DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), nl.item(0));
				XMLSignature signature = fac.unmarshalXMLSignature(valContext);
				boolean coreValidity = signature.validate(valContext);
				return coreValidity;
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}

}
