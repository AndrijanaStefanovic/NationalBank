package com.example.Company.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.crypto.SecretKey;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.Company.model.Invoice;
import com.example.Company.model.InvoiceItem;
import com.example.Company.service.XMLsecurity.KeyStoreReader;
import com.example.Company.service.XMLsecurity.XMLEncryptionUtility;
import com.example.Company.service.XMLsecurity.XMLSigningUtility;

import sun.misc.BASE64Encoder;

public abstract class SaveToXml {
	
	@SuppressWarnings("restriction")
	static void saveToXML(Invoice invoice, List<InvoiceItem> items) {
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
					 topElement.appendChild(invoice_element);
				 }
	         }
	        
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
	 		
	 		sendPost(doc, privateKey);	         
	      	      
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	
	private static PrivateKey readPrivateKey() {
		KeyStoreReader ksReader = new KeyStoreReader();
		
		return ksReader.readPrivateKey("primer.jks", "primer", "primer", "primer");
	}
	
	private static void sendPost(Document doc, 
								 PrivateKey privateKey) throws Exception {	
		
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
    
        StringWriter writer = new StringWriter();
        StreamResult resw = new StreamResult(writer);
        transformer.transform(source, resw);	
        
		String url = "https://localhost:8444/invoice/getBody";
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
	
	public static void saveXMltoDB(Document doc) {      
		PrivateKey privateKey = readPrivateKey();
		XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
		XMLSigningUtility sigUtility = new XMLSigningUtility();
		
		boolean res = sigUtility.verifySignature(doc);
 		if (res) {
 			System.out.println("\n Verifyed signature!");
 			doc = encUtility.decrypt(doc, privateKey);
 			System.out.println("\n Decryption is completed! Company B has reveived XML. ");
 			File schemaFile = new File("invoice.xsd"); 
 			SchemaFactory schemaFactory = SchemaFactory
 					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
 			
 	  	    Source xmlFile = new DOMSource(doc);
 	  	    xmlFile.setSystemId("invoice");
 			try {
 				Schema schema = schemaFactory.newSchema(schemaFile);
 				Validator validator = schema.newValidator();
 				validator.validate(xmlFile);
 				validateXML(doc);
 				System.out.println(xmlFile.getSystemId() + " is valid");
 			} catch (SAXException e) {
 				System.out.println(xmlFile.getSystemId() + " is NOT valid reason:" + e);
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 		} else {
 			System.out.println("\n Signature is invalid! ");
 		}		
	}
	
	public static void validateXML(Document doc) {
		NodeList nodeList = doc.getElementsByTagName("invoice");
  	  	String url = "jdbc:mysql://localhost/companyxml?useSSL=false&createDatabaseIfNotExist=true";
  	  	String user = "root";
  	  	String password = "root";
  	  	Connection con = null;
  	  	
  	  	PreparedStatement preparedStatement = null;
  	    try {
			con = DriverManager.getConnection(url, user, password);
		
	  	  	for (int i = 0; i < nodeList.getLength(); i++)
	  	  	{
	  	  		Node nNode = nodeList.item(i);
	  		  
	  	  		System.out.println("\nCurrent Element :" + nNode.getNodeName());
	  		  
	  	  		if (nNode.getNodeType() == Node.ELEMENT_NODE) 
	  	  		{
	  	  			Element element = (Element) nNode; 
	  	  			String query = " insert into invoice (messageId, supplierName, supplierAddress, supplierPIB, "
				  					+ "buyerName, buyerAddress, buyerPIB, accountNumber, dateOfInvoice, "
				  					+ "merchandiseValue, servicesValue, totalValue, totalTax, currency, "
				  					+ "totalDue, billingAccountNumber, dateOfValue, totalDiscount)"
				  					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  	  			try {
						preparedStatement = con.prepareStatement(query);
						preparedStatement.setString(1, element.getElementsByTagName("messageId").item(0).getTextContent());
						preparedStatement.setString(2, element.getElementsByTagName("supplierName").item(0).getTextContent());
						preparedStatement.setString(3, element.getElementsByTagName("supplierAddress").item(0).getTextContent());
						preparedStatement.setString(4, element.getElementsByTagName("supplierPIB").item(0).getTextContent());
						preparedStatement.setString(5, element.getElementsByTagName("buyerName").item(0).getTextContent());
						preparedStatement.setString(6, element.getElementsByTagName("buyerAddress").item(0).getTextContent());
						preparedStatement.setString(7, element.getElementsByTagName("buyerPIB").item(0).getTextContent());
						preparedStatement.setString(8, element.getElementsByTagName("accountNumber").item(0).getTextContent());
						preparedStatement.setString(9, element.getElementsByTagName("dateOfInvoice").item(0).getTextContent());
						preparedStatement.setString(10, element.getElementsByTagName("merchandiseValue").item(0).getTextContent());
						preparedStatement.setString(11, element.getElementsByTagName("servicesValue").item(0).getTextContent());
						preparedStatement.setString(12, element.getElementsByTagName("totalValue").item(0).getTextContent());
						preparedStatement.setString(13, element.getElementsByTagName("totalTax").item(0).getTextContent());
						preparedStatement.setString(14, element.getElementsByTagName("currency").item(0).getTextContent());
						preparedStatement.setString(15, element.getElementsByTagName("totalDue").item(0).getTextContent());
						preparedStatement.setString(16, element.getElementsByTagName("billingAccountNumber").item(0).getTextContent());
						preparedStatement.setString(17, element.getElementsByTagName("dateOfValue").item(0).getTextContent());
						preparedStatement.setString(18, element.getElementsByTagName("totalDiscount").item(0).getTextContent());
						preparedStatement.execute();
					} catch (SQLException e) {					
						System.out.println("Exception with Prepared Statement" + e.getMessage());
					} 	        			  
	  	  		}
	  	  	}
  	  	
	  	  	NodeList nodeItemList = doc.getElementsByTagName("invoiceItem");
	  	  	for (int i = 0; i < nodeItemList.getLength(); i++)
		  	{
		  		Node nNodeItem = nodeItemList.item(i);
			  
		  		System.out.println("\nCurrent Element :" + nNodeItem.getNodeName());
			  
		  		if (nNodeItem.getNodeType() == Node.ELEMENT_NODE) 
		  		{
	  	  			Element element = (Element) nNodeItem; 
	  	  			String queryItem = " insert into invoiceItem (itemNumber, itemName, itemAmount, measurmentUnit, "
	  					+ "unitPrice, value, discountPercent, totalDiscount, subtractedDiscount, "
	  					+ "totalTax) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  	  			try {
	  	  				preparedStatement = con.prepareStatement(queryItem);
		  	  			preparedStatement.setString(1, element.getElementsByTagName("number").item(0).getTextContent());
						preparedStatement.setString(2, element.getElementsByTagName("name").item(0).getTextContent());
						preparedStatement.setString(3, element.getElementsByTagName("amount").item(0).getTextContent());
						preparedStatement.setString(4, element.getElementsByTagName("measurementUnit").item(0).getTextContent());
						preparedStatement.setString(5, element.getElementsByTagName("unitPrice").item(0).getTextContent());
						preparedStatement.setString(6, element.getElementsByTagName("value").item(0).getTextContent());
						preparedStatement.setString(7, element.getElementsByTagName("discountPercent").item(0).getTextContent());
						preparedStatement.setString(8, element.getElementsByTagName("discountTotal").item(0).getTextContent());
						preparedStatement.setString(9, element.getElementsByTagName("subtractedDiscount").item(0).getTextContent());
						preparedStatement.setString(10, element.getElementsByTagName("taxTotal").item(0).getTextContent());
						preparedStatement.execute();
						con.close();
					} catch (SQLException e) {					
						System.out.println("Exception with Prepared Statement" + e.getMessage());
					} 	        
	  	  		}
		  	}
  	  	} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
