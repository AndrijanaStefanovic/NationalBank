package com.example.Company.service;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.Company.model.Invoice;
import com.example.Company.model.InvoiceItem;
import com.example.Company.repository.InvoiceItemRepository;
import com.example.Company.repository.InvoiceRepository;
import com.example.Company.service.XMLsecurity.KeyStoreReader;
import com.example.Company.service.XMLsecurity.XMLEncryptionUtility;
import com.example.Company.service.XMLsecurity.XMLSigningUtility;

public class ValidateXMLAndSaveToDB {

	public static void decryptXMLAndSaveToDB(Document document, 
											 InvoiceRepository invoiceRepository, 
											 InvoiceItemRepository ivoiceItemRepository) {
		KeyStoreReader ksReader = new KeyStoreReader();		
		PrivateKey privateKey = ksReader.readPrivateKey("primer.jks", "primer", "primer", "primer");
		XMLEncryptionUtility encUtility = new XMLEncryptionUtility();
		XMLSigningUtility sigUtility = new XMLSigningUtility();
        
		boolean res = sigUtility.verifySignature(document);
 		if (res) {
 			System.out.println("\n Verifyed signature!");
 			document = encUtility.decrypt(document, privateKey);
 			System.out.println("\n Decryption is completed! Company B has reveived XML. ");
 			File schemaFile = new File("invoice.xsd"); 
 			SchemaFactory schemaFactory = SchemaFactory
 					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
 			
 	  	    Source xmlFile = new DOMSource(document);
 	  	    xmlFile.setSystemId("invoice");
 			try {
 				Schema schema = schemaFactory.newSchema(schemaFile);
 				Validator validator = schema.newValidator();
 				validator.validate(xmlFile);
 				validateXML(document, invoiceRepository, ivoiceItemRepository);
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
	
	public static void validateXML(Document doc, 
								   InvoiceRepository invoiceRepository, 
								   InvoiceItemRepository ivoiceItemRepository) {
		NodeList nodeList = doc.getElementsByTagName("invoice");
		Invoice newInvoice = new Invoice();
		for (int i = 0; i < nodeList.getLength(); i++)
  	  	{
  	  		Node nNode = nodeList.item(i);
  		  
  	  		System.out.println("\nCurrent Element :" + nNode.getNodeName());
  		  
  	  		if (nNode.getNodeType() == Node.ELEMENT_NODE) 
  	  		{
  	  			Element element = (Element) nNode; 
	  			newInvoice.setAccountNumber(Integer.parseInt(element.getElementsByTagName("accountNumber").item(0).getTextContent()));
	  			newInvoice.setBillingAccountNumber(element.getElementsByTagName("billingAccountNumber").item(0).getTextContent());
	  			newInvoice.setReceived(true);
	  			newInvoice.setBuyerAddress(element.getElementsByTagName("buyerAddress").item(0).getTextContent());
	  			newInvoice.setBuyerName(element.getElementsByTagName("buyerName").item(0).getTextContent());
	  			newInvoice.setBuyerPIB(element.getElementsByTagName("buyerPIB").item(0).getTextContent());
	  			newInvoice.setCurrency(element.getElementsByTagName("currency").item(0).getTextContent());
	  			newInvoice.setMerchandiseValue(Double.parseDouble(element.getElementsByTagName("merchandiseValue").item(0).getTextContent()));
	  			newInvoice.setMessageId(element.getElementsByTagName("messageId").item(0).getTextContent());
	  			newInvoice.setServicesValue(Double.parseDouble(element.getElementsByTagName("servicesValue").item(0).getTextContent()));
	  			newInvoice.setSupplierAddress(element.getElementsByTagName("supplierAddress").item(0).getTextContent());
	  			newInvoice.setSupplierName(element.getElementsByTagName("supplierName").item(0).getTextContent());
	  			newInvoice.setSupplierPIB(element.getElementsByTagName("supplierPIB").item(0).getTextContent());
	  			newInvoice.setTotalDiscount(Double.parseDouble(element.getElementsByTagName("totalDiscount").item(0).getTextContent()));
	  			newInvoice.setTotalDue(Double.parseDouble(element.getElementsByTagName("totalDue").item(0).getTextContent()));
	  			newInvoice.setTotalTax(Double.parseDouble(element.getElementsByTagName("totalTax").item(0).getTextContent()));
	  			newInvoice.setTotalValue(Double.parseDouble(element.getElementsByTagName("totalValue").item(0).getTextContent()));
  	  		} 	  		
  	  	}
		
		invoiceRepository.save(newInvoice);
		
		NodeList nodeItemList = doc.getElementsByTagName("invoiceItem");
		ArrayList<InvoiceItem> list = new ArrayList<InvoiceItem>();
  	  	for (int i = 0; i < nodeItemList.getLength(); i++)
	  	{
	  		Node nNodeItem = nodeItemList.item(i);
		  
	  		System.out.println("\nCurrent Element :" + nNodeItem.getNodeName());
		  
	  		if (nNodeItem.getNodeType() == Node.ELEMENT_NODE) 
	  		{
  	  			Element element = (Element) nNodeItem;
  	  			
	  	  		InvoiceItem invoiceItem2 = new InvoiceItem();
	  	  		invoiceItem2.setNumber(Integer.parseInt(element.getElementsByTagName("number").item(0).getTextContent()));
				invoiceItem2.setAmount(Double.parseDouble(element.getElementsByTagName("amount").item(0).getTextContent()));
				invoiceItem2.setDiscountPercent(Double.parseDouble(element.getElementsByTagName("discountPercent").item(0).getTextContent()));
				invoiceItem2.setKind(element.getElementsByTagName("kind").item(0).getTextContent());			
				invoiceItem2.setMeasurmentUnit(element.getElementsByTagName("measurementUnit").item(0).getTextContent());
				invoiceItem2.setName(element.getElementsByTagName("name").item(0).getTextContent());
				invoiceItem2.setSubtractedDiscount(Double.parseDouble(element.getElementsByTagName("subtractedDiscount").item(0).getTextContent()));
				invoiceItem2.setTotalDiscount(Double.parseDouble(element.getElementsByTagName("discountTotal").item(0).getTextContent()));
				invoiceItem2.setTotalTax(Double.parseDouble(element.getElementsByTagName("taxTotal").item(0).getTextContent()));
				invoiceItem2.setUnitPrice(Double.parseDouble(element.getElementsByTagName("unitPrice").item(0).getTextContent()));
				invoiceItem2.setValue(Double.parseDouble(element.getElementsByTagName("value").item(0).getTextContent()));
				list.add(invoiceItem2);
				invoiceItem2.setInvoice(newInvoice);
				ivoiceItemRepository.save(invoiceItem2);
	  		}
	  	}
		newInvoice.setInvoiceItems(list);
		invoiceRepository.save(newInvoice);
		
	}
}
