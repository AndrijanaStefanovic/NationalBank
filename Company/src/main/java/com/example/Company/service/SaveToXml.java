package com.example.Company.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

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

import com.example.Company.model.Invoice;
import com.example.Company.model.InvoiceItem;


public abstract class SaveToXml {
	
	static void saveToXML(Invoice invoice, List<InvoiceItem> items) {
		String messageId= invoice.getMessageId();
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
	    String totalTax = Double.toString(invoice.getTotalTax());
	    String currency = invoice.getCurrency();
	    String totalDue = Double.toString(invoice.getTotalDue());
	    String billingAccountNumber = Double.toString(invoice.getBillingAccountNumber());
	    String dateOfValue = DateFormatUtils.format(invoice.getDateOfValue(), "yyyy-MM-dd");

	  try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.newDocument();
	   
	         Element topElement = doc.createElement("invoices");	         
	         doc.appendChild(topElement);

	         Element rootElement = doc.createElement("invoices_data");
	         topElement.appendChild(rootElement);
	                  	         
	         Element messageIdEl = doc.createElement("messageId");
	         messageIdEl.appendChild(doc.createTextNode(messageId));
	         
	         Element supplierNameEl = doc.createElement("supplierName");
	         supplierNameEl.appendChild(doc.createTextNode(supplierName));
	         
	         Element supplierAddressEl = doc.createElement("supplierAddress");
	         supplierAddressEl.appendChild(doc.createTextNode(supplierAddress));
	         
	         Element supplierPIBEl = doc.createElement("supplierPIB");
	         supplierPIBEl.appendChild(doc.createTextNode(supplierPIB));
	         
	         Element buyerNameEl = doc.createElement("buyerName");
	         buyerNameEl.appendChild(doc.createTextNode(buyerName));
	         
	         Element buyerAddressEl = doc.createElement("buyerAddress");
	         buyerAddressEl.appendChild(doc.createTextNode(buyerAddress));
	         
	         Element buyerPIBEl = doc.createElement("buyerPIB");
	         buyerPIBEl.appendChild(doc.createTextNode(buyerPIB));
	         
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
	         
	         Element totalTaxEl = doc.createElement("totalTax");
	         totalTaxEl.appendChild(doc.createTextNode(totalTax));
	         
	         Element currencyEl = doc.createElement("currency");
	         currencyEl.appendChild(doc.createTextNode(currency));         
	         
	         Element totalDueEl = doc.createElement("totalDue");
	         totalDueEl.appendChild(doc.createTextNode(totalDue));
	         
	         Element billingAccountNumberEl = doc.createElement("billingAccountNumber");
	         billingAccountNumberEl.appendChild(doc.createTextNode(billingAccountNumber));
	         
	         Element dateOfValueEl= doc.createElement("dateOfValue");
	         dateOfValueEl.appendChild(doc.createTextNode(dateOfValue));
	         
	         rootElement.appendChild(messageIdEl);
	         rootElement.appendChild(supplierNameEl);
	         rootElement.appendChild(supplierAddressEl);
	         rootElement.appendChild(supplierPIBEl);
	         rootElement.appendChild(buyerNameEl);
	         rootElement.appendChild(buyerAddressEl);
	         rootElement.appendChild(buyerPIBEl);
	         rootElement.appendChild(accountNumberEl);
	         rootElement.appendChild(dateOfInvoiceEl);
	         rootElement.appendChild(merchandiseValueEl);
	         rootElement.appendChild(servicesValueEl);
	         rootElement.appendChild(totalValueEl);
	         rootElement.appendChild(totalTaxEl);
	         rootElement.appendChild(currencyEl);
	         rootElement.appendChild(totalDueEl);
	         rootElement.appendChild(billingAccountNumberEl);
	         rootElement.appendChild(dateOfValueEl);
	         
	         if (items != null) {
				 for (InvoiceItem item : items) {
					 Element invoice_element =  doc.createElement("item_list");
					 Element number =  doc.createElement("itemNumber");
					 number.appendChild(doc.createTextNode(Integer.toString(item.getNumber())));
					 Element name = doc.createElement("itemName");
					 name.appendChild(doc.createTextNode(item.getName()));
					 Element amountItem = doc.createElement("itemAmount");
					 amountItem.appendChild(doc.createTextNode(Double.toString(item.getAmount())));
					 Element measurmentUnit = doc.createElement("measurmentUnit");
					 measurmentUnit.appendChild(doc.createTextNode(item.getMeasurmentUnit()));
					 Element unitPrice = doc.createElement("unitPrice");
					 unitPrice.appendChild(doc.createTextNode(Double.toString(item.getUnitPrice())));
					 Element value = doc.createElement("value");
					 value.appendChild(doc.createTextNode(Double.toString(item.getValue())));
					 Element discountPercent = doc.createElement("discountPercent");
					 discountPercent.appendChild(doc.createTextNode(Double.toString(item.getDiscountPercent())));
					 Element totalDiscount = doc.createElement("totalDiscount");
					 totalDiscount.appendChild(doc.createTextNode(Double.toString(item.getTotalDiscount())));
					 Element subtractedDiscount = doc.createElement("subtractedDiscount");
					 subtractedDiscount.appendChild(doc.createTextNode(Double.toString(item.getSubtractedDiscount())));
					 Element totalTaxItem = doc.createElement("totalTax");
					 totalTaxItem.appendChild(doc.createTextNode(Double.toString(item.getTotalTax()))); 		 
					 invoice_element.appendChild(number);
					 invoice_element.appendChild(name);
					 invoice_element.appendChild(amountItem);
					 invoice_element.appendChild(measurmentUnit);
					 invoice_element.appendChild(unitPrice);
					 invoice_element.appendChild(value);
					 invoice_element.appendChild(discountPercent);
					 invoice_element.appendChild(totalDiscount);
					 invoice_element.appendChild(subtractedDiscount);
					 invoice_element.appendChild(totalTaxItem);
					 topElement.appendChild(invoice_element);
				 }
	         }	         	         
	 		  		  		
	         TransformerFactory transformerFactory = TransformerFactory.newInstance();
	         Transformer transformer = transformerFactory.newTransformer();
	         DOMSource source = new DOMSource(doc);
	         
	         StringWriter writer = new StringWriter();
	         StreamResult res = new StreamResult(writer);

	         transformer.transform(source, res);	
	         sendPost(writer.toString());	         
	      	      
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	
	private static void sendPost(String urlParametars) throws Exception {
		String url = "https://localhost:8444/invoice/getBody";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/xml");
		con.setDoOutput(true);
		DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
		outputStream.writeBytes(urlParametars);
		outputStream.flush();
		outputStream.close();
		
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' xml document over URL: " + url );
		System.out.println("Post parameters: " + urlParametars);
		System.out.println("Response code: " + responseCode);
		
		BufferedReader in = new BufferedReader (new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
		
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
