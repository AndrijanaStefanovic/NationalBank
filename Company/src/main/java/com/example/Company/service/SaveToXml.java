package com.example.Company.service;

import java.io.File;
import java.util.List;
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
	         StreamResult result = new StreamResult(new File("invoice.xml"));     

	         transformer.transform(source, result);	        
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}
}
