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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		String id = Long.toString(invoice.getId());
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
	    String received = String.valueOf(invoice.isReceived());
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
	         
	         Element receivedEl = doc.createElement("received");
	         receivedEl.appendChild(doc.createTextNode(received));
	         
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
	         topElement.appendChild(receivedEl);
	         
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
					 Element invoice_idEl = doc.createElement("invoice_id");
					 invoice_idEl.appendChild(doc.createTextNode(id));
					 
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
					 invoice_element.appendChild(invoice_idEl);
					 
					 topElement.appendChild(invoice_element);
				 }
	         }
	         
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult sr = new StreamResult(new File("invoices.xml"));
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
	 		
	 		sendPost(doc);	         
	      	      
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}
		
	private static void sendPost(Document doc) throws Exception {			
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
	
	private static PrivateKey readPrivateKey() {
		KeyStoreReader ksReader = new KeyStoreReader();		
		return ksReader.readPrivateKey("primer.jks", "primer", "primer", "primer");
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
  	  	String url = "jdbc:mysql://localhost/company2?useSSL=false&createDatabaseIfNotExist=true";
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
	  	  			String query = " insert into invoice (account_number, billing_account_number, buyer_address, buyer_name, buyerpib, "
				  					+ "currency, date_of_invoice, date_of_value, merchandise_value, message_id, "
				  					+ "received, services_value, supplier_address, supplier_name, supplierpib, "
				  					+ "total_discount, total_due, total_tax, total_value)"
				  					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  	  			try {
						preparedStatement = con.prepareStatement(query);
						preparedStatement.setInt(1, Integer.parseInt(element.getElementsByTagName("accountNumber").item(0).getTextContent()));
						preparedStatement.setString(2, element.getElementsByTagName("billingAccountNumber").item(0).getTextContent());
						preparedStatement.setString(3, element.getElementsByTagName("buyerAddress").item(0).getTextContent());
						preparedStatement.setString(4, element.getElementsByTagName("buyerName").item(0).getTextContent());
						preparedStatement.setString(5, element.getElementsByTagName("buyerPIB").item(0).getTextContent());
						preparedStatement.setString(6, element.getElementsByTagName("currency").item(0).getTextContent());
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
						LocalDate date1 = LocalDate.parse(element.getElementsByTagName("dateOfInvoice").item(0).getTextContent(), formatter);
						java.sql.Date dateOfInvoice = java.sql.Date.valueOf(date1);
						LocalDate date2 = LocalDate.parse(element.getElementsByTagName("dateOfValue").item(0).getTextContent(), formatter);
						java.sql.Date dateOfValue = java.sql.Date.valueOf(date2);
						preparedStatement.setDate(7, dateOfInvoice);
						preparedStatement.setDate(8, dateOfValue);
						preparedStatement.setDouble(9, Double.parseDouble(element.getElementsByTagName("merchandiseValue").item(0).getTextContent()));
						preparedStatement.setString(10, element.getElementsByTagName("messageId").item(0).getTextContent());
						preparedStatement.setBoolean(11, true);
						preparedStatement.setDouble(12, Double.parseDouble(element.getElementsByTagName("servicesValue").item(0).getTextContent()));
						preparedStatement.setString(13, element.getElementsByTagName("supplierAddress").item(0).getTextContent());
						preparedStatement.setString(14, element.getElementsByTagName("supplierName").item(0).getTextContent());
						preparedStatement.setString(15, element.getElementsByTagName("supplierPIB").item(0).getTextContent());
						preparedStatement.setDouble(16, Double.parseDouble(element.getElementsByTagName("totalDiscount").item(0).getTextContent()));
						preparedStatement.setDouble(17, Double.parseDouble(element.getElementsByTagName("totalDue").item(0).getTextContent()));
						preparedStatement.setDouble(18, Double.parseDouble(element.getElementsByTagName("totalTax").item(0).getTextContent()));
						preparedStatement.setDouble(19, Double.parseDouble(element.getElementsByTagName("totalValue").item(0).getTextContent()));
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
	  	  			String queryItem = " insert into invoice_item (amount, discount_percent, kind, measurment_unit, "
	  					+ "name, number, subtracted_discount, total_discount, total_tax, unit_price, value, "
	  					+ "invoice_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  	  			try {
	  	  				preparedStatement = con.prepareStatement(queryItem);
		  	  			preparedStatement.setDouble(1, Double.parseDouble(element.getElementsByTagName("amount").item(0).getTextContent()));
						preparedStatement.setDouble(2, Double.parseDouble(element.getElementsByTagName("discountPercent").item(0).getTextContent()));
						preparedStatement.setString(3, element.getElementsByTagName("kind").item(0).getTextContent());
						preparedStatement.setString(4, element.getElementsByTagName("measurementUnit").item(0).getTextContent());
						preparedStatement.setString(5, element.getElementsByTagName("name").item(0).getTextContent());
						preparedStatement.setInt(6, Integer.parseInt(element.getElementsByTagName("number").item(0).getTextContent()));
						preparedStatement.setDouble(7, Double.parseDouble(element.getElementsByTagName("subtractedDiscount").item(0).getTextContent()));
						preparedStatement.setDouble(8, Double.parseDouble(element.getElementsByTagName("discountTotal").item(0).getTextContent()));
						preparedStatement.setDouble(9, Double.parseDouble(element.getElementsByTagName("taxTotal").item(0).getTextContent()));
						preparedStatement.setDouble(10, Double.parseDouble(element.getElementsByTagName("unitPrice").item(0).getTextContent()));
						preparedStatement.setDouble(11, Double.parseDouble(element.getElementsByTagName("value").item(0).getTextContent()));
						preparedStatement.setLong(12, Long.parseLong(element.getElementsByTagName("invoice_id").item(0).getTextContent()));
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
