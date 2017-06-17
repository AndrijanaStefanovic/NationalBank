package com.example.Company.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.example.Company.model.BusinessPartner;
import com.example.Company.model.Invoice;
import com.example.Company.model.InvoiceItem;
import com.example.Company.repository.BusinessPartnerRepository;
import com.example.Company.repository.InvoiceItemRepository;
import com.example.Company.repository.InvoiceRepository;

@Service
public class InvoiceServiceImpl implements InvoiceService{

	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private InvoiceItemRepository ivoiceItemRepository;
	
	@Autowired
	private BusinessPartnerRepository businessPartnerRepository;
	
	@Override
	public String createInvoice(Invoice invoice) {
		invoice.setReceived(false);
		invoiceRepository.save(invoice);
		return "200";
	}

	@Override
	public Collection<Invoice> getAllInvoices() {
		return invoiceRepository.findAll(null).getContent();
	}

	@Override
	public String deleteInvoice(Long id) {
		Invoice invoice = invoiceRepository.findOne(id);
		if(invoice.getInvoiceItems() != null && !invoice.getInvoiceItems().isEmpty()) {
			return "HasItems";
		}
		invoiceRepository.delete(invoice);
		return "200";
	}

	@Override
	public Collection<Invoice> getReceivedInvoices() {
		return invoiceRepository.findByReceived(true);
	}

	@Override
	public Collection<Invoice> getSentInvoices() {
		return invoiceRepository.findByReceived(false);
	}

	@Override
	public Invoice getInvoice(Long id) {
		return invoiceRepository.findOne(id);
	}

	@Override
	public List<String> getXML() {
		Connection conn = null;
		List<String> results = null;
  	  	try {
			Class.forName("com.mysql.jdbc.Driver") ;
		
		  	conn = DriverManager.getConnection("jdbc:mysql://localhost/company2?"
		  			+ "useSSL=false&createDatabaseIfNotExist=true", 
		  			"root", "tasha1994") ;
		  	Statement stmt = conn.createStatement() ;
		  	String query = "SELECT * FROM company2.invoice ORDER BY id DESC LIMIT 1";
		  	ResultSet resultSet = stmt.executeQuery(query);
		  	results = new ArrayList<String>();
		  	while(resultSet.next()) {
		  		for (int i = 2; i < 21; i++)
		  			results.add(resultSet.getString(i));	  	    
		  	}
		  	String queryItems = "SELECT * FROM company2.invoice_item ORDER BY id DESC LIMIT 1";
		  	ResultSet resultSetItem = stmt.executeQuery(queryItems);
		  	while(resultSetItem.next()) {
		  		for (int i = 2; i < 14; i++)
		  			results.add(resultSetItem.getString(i));	  	    
		  	}		  	
  	  	} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  return results;
	}
	
	@Override
	public String receiveInvoice(com.example.service.invoice.Invoice invoice) {
		Invoice i = new Invoice();
		i.setAccountNumber(invoice.getAccountNumber());
		i.setReceived(true);
		i.setBillingAccountNumber(invoice.getBillingAccountNumber());
		i.setBuyerName(invoice.getBuyerName());
		i.setBuyerAddress(invoice.getBuyerAddress());
		i.setBuyerPIB(invoice.getBuyerPIB());
		i.setSupplierName(invoice.getSupplierName());
		i.setSupplierAddress(invoice.getSupplierAddress());
		i.setSupplierPIB(invoice.getSupplierPIB());
		i.setCurrency(invoice.getCurrency());
		
		i.setDateOfInvoice(invoice.getDateOfInvoice().toGregorianCalendar().getTime());
		i.setDateOfValue(invoice.getDateOfValue().toGregorianCalendar().getTime());
		
		i.setMerchandiseValue(invoice.getMerchandiseValue().doubleValue());
		i.setMessageId(invoice.getMessageId());
		i.setServicesValue(invoice.getServicesValue().doubleValue());
		i.setTotalValue(invoice.getTotalValue().doubleValue());
		i.setTotalDue(invoice.getTotalDue().doubleValue());
		i.setTotalTax(invoice.getTotalTax().doubleValue());
		i.setTotalDiscount(invoice.getTotalDiscount().doubleValue());
		invoiceRepository.save(i);
		
		ArrayList<InvoiceItem> list = new ArrayList<InvoiceItem>();
		for(com.example.service.invoice.Invoice.InvoiceItem iiXML : invoice.getInvoiceItem()){
			InvoiceItem ii = new InvoiceItem();
			ii.setNumber(iiXML.getNumber());
			ii.setAmount(iiXML.getAmount().doubleValue());
			ii.setDiscountPercent(iiXML.getDiscountPercent().doubleValue());
			ii.setKind(iiXML.getKind());
			ii.setMeasurmentUnit(iiXML.getMeasurementUnit());
			ii.setName(iiXML.getName());
			ii.setSubtractedDiscount(iiXML.getSubtractedDiscount().doubleValue());
			ii.setDiscountPercent(iiXML.getDiscountPercent().doubleValue());
			ii.setTotalTax(iiXML.getTaxTotal().doubleValue());
			ii.setUnitPrice(iiXML.getUnitPrice().doubleValue());
			ii.setValue(iiXML.getValue().doubleValue());
			list.add(ii);
			ii.setInvoice(i);
			ivoiceItemRepository.save(ii);
		}
			
		i.setInvoiceItems(list);
		invoiceRepository.save(i);
		return "OK";
	}

	@Override
	public String exportInvoiceToXML(Long id) {
		Invoice invoice = invoiceRepository.findOne(id);
		List<InvoiceItem> items = ivoiceItemRepository.findByInvoice(invoice);
		BusinessPartner bp = businessPartnerRepository.findByPartnerPIB(invoice.getBuyerPIB()).get(0);
		SaveToXml.generateXMLDocument(invoice, items, bp);	         
		return "OK";
	}

	@Override
	public String receiveXML(String response) {
		System.out.println("-------------------------------");
		System.out.println("Result of response: " + response);
		System.out.println("-------------------------------");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();    
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder; 
        Document document = null;
        try  
        {  
            builder = factory.newDocumentBuilder();  
            InputSource iSource = new InputSource();
            iSource.setCharacterStream(new StringReader(response));            
            document = builder.parse(iSource); 

        } catch (Exception e) {  
            e.printStackTrace();  
        }
        
        ValidateXMLAndSaveToDB.decryptXMLAndSaveToDB(document, invoiceRepository, ivoiceItemRepository);
       
 		return "OK";
	}
	
	@Override
	public String sendInvoice(Long id){
		Invoice invoiceToSend = invoiceRepository.findOne(id);
		List<InvoiceItem> invoiceItems = ivoiceItemRepository.findByInvoice(invoiceToSend);
		com.example.service.invoice.Invoice invoiceXML = new com.example.service.invoice.Invoice();
		
		BusinessPartner bp = businessPartnerRepository.findByPartnerPIB(invoiceToSend.getBuyerPIB()).get(0);
		
		invoiceXML.setAccountNumber(invoiceToSend.getAccountNumber());
		invoiceXML.setBillingAccountNumber(invoiceToSend.getBillingAccountNumber());
		invoiceXML.setBuyerAddress(invoiceToSend.getBuyerAddress());
		invoiceXML.setBuyerName(invoiceToSend.getBuyerName());
		invoiceXML.setBuyerPIB(invoiceToSend.getBuyerPIB());
		invoiceXML.setCurrency(invoiceToSend.getCurrency());
		//otkomentarisati na kraju
		try {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(invoiceToSend.getDateOfInvoice());
			XMLGregorianCalendar dateOfInvoice = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			invoiceXML.setDateOfInvoice(dateOfInvoice);
			
			c.setTime(invoiceToSend.getDateOfValue());
			XMLGregorianCalendar dateOfValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			invoiceXML.setDateOfValue(dateOfValue);
		} catch (DatatypeConfigurationException e1) {
			e1.printStackTrace();
		}

		invoiceXML.setMerchandiseValue(new BigDecimal(invoiceToSend.getMerchandiseValue()));
		invoiceXML.setMessageId(invoiceToSend.getMessageId());
		invoiceXML.setServicesValue(new BigDecimal(invoiceToSend.getServicesValue()));
		invoiceXML.setSupplierAddress(invoiceToSend.getSupplierAddress());
		invoiceXML.setSupplierName(invoiceToSend.getSupplierName());
		invoiceXML.setSupplierPIB(invoiceToSend.getSupplierPIB());
		invoiceXML.setTotalDiscount(new BigDecimal(invoiceToSend.getTotalDiscount()));
		invoiceXML.setTotalDue(new BigDecimal(invoiceToSend.getTotalDue()));
		invoiceXML.setTotalTax(new BigDecimal(invoiceToSend.getTotalTax()));
		invoiceXML.setTotalValue(new BigDecimal(invoiceToSend.getTotalValue()));
		
		for (InvoiceItem invoiceItem : invoiceItems) {
			com.example.service.invoice.Invoice.InvoiceItem invoiceItemXML = new  com.example.service.invoice.Invoice.InvoiceItem();
			invoiceItemXML.setNumber(invoiceItem.getNumber());
			invoiceItemXML.setAmount(new BigDecimal(invoiceItem.getAmount()));
			invoiceItemXML.setDiscountPercent(new BigDecimal(invoiceItem.getDiscountPercent()));
			invoiceItemXML.setKind(invoiceItem.getKind());
			invoiceItemXML.setMeasurementUnit(invoiceItem.getMeasurmentUnit());
			invoiceItemXML.setName(invoiceItem.getName());
			invoiceItemXML.setSubtractedDiscount(new BigDecimal(invoiceItem.getSubtractedDiscount()));
			invoiceItemXML.setDiscountTotal(new BigDecimal(invoiceItem.getTotalDiscount()));
			invoiceItemXML.setTaxTotal(new BigDecimal(invoiceItem.getTotalTax()));
			invoiceItemXML.setUnitPrice(new BigDecimal(invoiceItem.getUnitPrice()));
			invoiceItemXML.setValue(new BigDecimal(invoiceItem.getValue()));
			invoiceXML.getInvoiceItem().add(invoiceItemXML);
		}		
		
		try {
			URL url = new URL(bp.getUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/xml");
			System.out.println("after contentt type");

			JAXBContext jaxbContext = JAXBContext.newInstance(com.example.service.invoice.Invoice.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(invoiceXML, System.out);

			OutputStream os = conn.getOutputStream();
			jaxbMarshaller.marshal(invoiceXML, os);
			os.flush();
					
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} 
		
		return "OK";
	}

}
