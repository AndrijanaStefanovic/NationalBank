package com.example.Company.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.crypto.SecretKey;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.example.Company.model.BusinessPartner;
import com.example.Company.model.Invoice;
import com.example.Company.model.InvoiceItem;
import com.example.Company.service.XMLsecurity.KeyStoreReader;
import com.example.Company.service.XMLsecurity.XMLEncryptionUtility;
import com.example.Company.service.XMLsecurity.XMLSigningUtility;

import sun.misc.BASE64Encoder;

public class SaveToXml {
	
	public static void generateXMLDocument(Invoice invoice, List<InvoiceItem> items, BusinessPartner bp) {
		String messageId = invoice.getMessageId();
		String supplierName = invoice.getSupplierName();
		String supplierAddress = invoice.getSupplierAddress();
	    String supplierPIB = invoice.getSupplierPIB();
	    String buyerName = invoice.getBuyerName();
		String buyerAddress = invoice.getBuyerAddress();
	    String buyerPIB = invoice.getBuyerPIB();
	    String accountNumbeer = Integer.toString(invoice.getAccountNumber());
	    String dateOfInvoice = DateFormatUtils.format(invoice.getDateOfInvoice(), "yyyy-MM-dd");
	    String merchandiseValue = Double.toString(invoice.getMerchandiseValue());
	    String servicesValue = Double.toString(invoice.getServicesValue());
	    String totalValue = Double.toString(invoice.getTotalValue());
	    String totalDiscount = Double.toString(invoice.getTotalDiscount());
	    String totalTax = Double.toString(invoice.getTotalTax());
	    String currency = invoice.getCurrency();
	    String totalDue = Double.toString(invoice.getTotalDue());
	    String billingAccountNumber = invoice.getBillingAccountNumber();
	    String dateOfValue = DateFormatUtils.format(invoice.getDateOfValue(), "yyyy-MM-dd");
	    
	    try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.newDocument();
	   
	         Element topElement = doc.createElement("invoice");	 
	         topElement.setAttribute("xmlns", "http://service.example.com/invoice");
	         doc.appendChild(topElement);	         
                 	         	         
	         Element messageIdEl = doc.createElement("messageId");
	         messageIdEl.appendChild(doc.createTextNode(messageId));
	         
	         Element buyerNameEl = doc.createElement("buyerName");
	         buyerNameEl.appendChild(doc.createTextNode(buyerName));
	         
	         Element buyerAddressEl = doc.createElement("buyerAddress");
	         buyerAddressEl.appendChild(doc.createTextNode(buyerAddress));
	         
	         Element buyerPIBEl = doc.createElement("buyerPIB");
	         buyerPIBEl.appendChild(doc.createTextNode(buyerPIB));
	         
	         Element supplierNameEl = doc.createElement("supplierName");
	         supplierNameEl.appendChild(doc.createTextNode(supplierName));
	         
	         Element supplierAddressEl = doc.createElement("supplierAddress");
	         supplierAddressEl.appendChild(doc.createTextNode(supplierAddress));
	         
	         Element supplierPIBEl = doc.createElement("supplierPIB");
	         supplierPIBEl.appendChild(doc.createTextNode(supplierPIB));	         
	         
	         Element accountNumberEl = doc.createElement("accountNumber");
	         accountNumberEl.appendChild(doc.createTextNode(accountNumbeer));
	         
	         Element dateOfInvoiceEl = doc.createElement("dateOfInvoice");
	         dateOfInvoiceEl.appendChild(doc.createTextNode(dateOfInvoice));
	         
	         Element merchandiseValueEl = doc.createElement("merchandiseValue");
	         merchandiseValueEl.appendChild(doc.createTextNode(merchandiseValue));
	         
	         Element servicesValueEl = doc.createElement("servicesValue");
	         servicesValueEl.appendChild(doc.createTextNode(servicesValue));
	         
	         Element totalValueEl = doc.createElement("totalValue");
	         totalValueEl.appendChild(doc.createTextNode(totalValue));
	         
	         Element totalDiscountEl = doc.createElement("totalDiscount");
	         totalDiscountEl.appendChild(doc.createTextNode(totalDiscount));
	         
	         Element totalTaxEl = doc.createElement("totalTax");
	         totalTaxEl.appendChild(doc.createTextNode(totalTax));
	         
	         Element totalDueEl = doc.createElement("totalDue");
	         totalDueEl.appendChild(doc.createTextNode(totalDue));
	         
	         Element currencyEl = doc.createElement("currency");
	         currencyEl.appendChild(doc.createTextNode(currency));         
	         	         
	         Element billingAccountNumberEl = doc.createElement("billingAccountNumber");
	         billingAccountNumberEl.appendChild(doc.createTextNode(billingAccountNumber));
	         
	         Element dateOfValueEl= doc.createElement("dateOfValue");
	         dateOfValueEl.appendChild(doc.createTextNode(dateOfValue));
	         
	         topElement.appendChild(messageIdEl);
	         topElement.appendChild(buyerNameEl);
	         topElement.appendChild(buyerAddressEl);
	         topElement.appendChild(buyerPIBEl);
	         topElement.appendChild(supplierNameEl);
	         topElement.appendChild(supplierAddressEl);
	         topElement.appendChild(supplierPIBEl);
	         topElement.appendChild(accountNumberEl);
	         topElement.appendChild(dateOfInvoiceEl);
	         topElement.appendChild(merchandiseValueEl);
	         topElement.appendChild(servicesValueEl);
	         topElement.appendChild(totalValueEl);
	         topElement.appendChild(totalDiscountEl);
	         topElement.appendChild(totalTaxEl);
	         topElement.appendChild(totalDueEl);
	         topElement.appendChild(currencyEl);
	         topElement.appendChild(billingAccountNumberEl);
	         topElement.appendChild(dateOfValueEl);
		
	         if (items != null) {
				 for (InvoiceItem item : items) {
					 Element invoice_element =  doc.createElement("invoiceItem");
					 Element number =  doc.createElement("number");
					 number.appendChild(doc.createTextNode(Integer.toString(item.getNumber())));
					 Element name = doc.createElement("name");
					 name.appendChild(doc.createTextNode(item.getName()));
					 Element amountItem = doc.createElement("amount");
					 amountItem.appendChild(doc.createTextNode(Double.toString(item.getAmount())));
					 Element measurmentUnit = doc.createElement("measurementUnit");
					 measurmentUnit.appendChild(doc.createTextNode(item.getMeasurmentUnit()));
					 Element unitPrice = doc.createElement("unitPrice");
					 unitPrice.appendChild(doc.createTextNode(Double.toString(item.getUnitPrice())));
					 Element value = doc.createElement("value");
					 value.appendChild(doc.createTextNode(Double.toString(item.getValue())));
					 Element discountPercent = doc.createElement("discountPercent");
					 discountPercent.appendChild(doc.createTextNode(Double.toString(item.getDiscountPercent())));
					 Element discountTotal = doc.createElement("discountTotal");
					 discountTotal.appendChild(doc.createTextNode(Double.toString(item.getTotalDiscount())));
					 Element subtractedDiscount = doc.createElement("subtractedDiscount");
					 subtractedDiscount.appendChild(doc.createTextNode(Double.toString(item.getSubtractedDiscount())));
					 Element totalTaxItem = doc.createElement("taxTotal");
					 totalTaxItem.appendChild(doc.createTextNode(Double.toString(item.getTotalTax()))); 
					 Element kindEl = doc.createElement("kind");
					 kindEl.appendChild(doc.createTextNode(item.getKind()));
					 
					 invoice_element.appendChild(number);
					 invoice_element.appendChild(name);
					 invoice_element.appendChild(amountItem);
					 invoice_element.appendChild(measurmentUnit);
					 invoice_element.appendChild(unitPrice);
					 invoice_element.appendChild(value);
					 invoice_element.appendChild(discountPercent);
					 invoice_element.appendChild(discountTotal);
					 invoice_element.appendChild(subtractedDiscount);
					 invoice_element.appendChild(totalTaxItem);
					 invoice_element.appendChild(kindEl);
					 
					 topElement.appendChild(invoice_element);
				 }
	         }
	         
		        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        DOMSource source = new DOMSource(doc);
		        StreamResult sr = new StreamResult(new File("inovices.xml"));
		        transformer.transform(source, sr);
	         	        
		        XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
		        XMLSigningUtility sigUtility = new XMLSigningUtility();
		        KeyStoreReader ksReader = new KeyStoreReader();
		        
		 		SecretKey secretKey = encUtility.generateDataEncryptionKey();
		 		System.out.println("*************Generate Key: *************" 
		 						+ new BASE64Encoder().encode(secretKey.getEncoded()));
		 		
		 		Certificate cert = ksReader.readCertificate("primer.jks", "primer", "primer");
		 		doc = encUtility.encrypt(doc, secretKey, cert);
		 		
		 		PrivateKey privateKey = ksReader.readPrivateKey("primer.jks", "primer", "primer", "primer");
		 		doc = sigUtility.signDocument(doc, privateKey, cert);
		 		
		        DOMSource source1 = new DOMSource(doc);
		        StreamResult sr1 = new StreamResult(new File("inovices1.xml"));
		        transformer.transform(source1, sr1);
		 		
		 		sendPost(doc, bp.getUrl());	   
	         
	    } catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	
	public static void sendPost(Document doc, String url) throws Exception {			
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
    
        StringWriter writer = new StringWriter();
        StreamResult resw = new StreamResult(writer);
        
        transformer.transform(source, resw);	
        
		URL obj = new URL(url);
		HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/xml");
		connection.setDoOutput(true);
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.writeBytes(writer.toString());
		outputStream.flush();
		outputStream.close();
		
		int responseCode = connection.getResponseCode();
		System.out.println("\nSending 'POST' xml document over URL: " + url );
		System.out.println("Post parameters: " + writer.toString());
		System.out.println("Response code: " + responseCode);
		
		BufferedReader in = new BufferedReader (new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
	}
	
	public static void disableSslVerification() {
	    try
	    {
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) {
	            }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) {
	            }
	        }
	        };

	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };

	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (KeyManagementException e) {
	        e.printStackTrace();
	    }
	}
	
}
