package com.example.Company.service;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import com.example.Company.model.Invoice;
import com.example.Company.model.InvoiceItem;
import com.example.Company.repository.InvoiceItemRepository;
import com.example.Company.repository.InvoiceRepository;

@Service
public class InvoiceServiceImpl implements InvoiceService{

	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private InvoiceItemRepository ivoiceItemRepository;

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
	
	public String export(Long id) {
		if (id != null) {
			Invoice invoice = invoiceRepository.findOne(id);
			List<InvoiceItem> invoiceItems = ivoiceItemRepository.findByInvoice(invoice);
			SaveToXml.saveToXML(invoice, invoiceItems);
			return "200";
		} else 
			return "500";
	}

	@Override
	public void getBody(String response) {
		System.out.println("-------------------------------");
		System.out.println("Result of response: " + response);
		System.out.println("-------------------------------");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();    
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder; 
        Document doc = null;
        try  
        {  
            builder = factory.newDocumentBuilder();  
            InputSource iSource = new InputSource();
            iSource.setCharacterStream(new StringReader(response));            
            doc = builder.parse(iSource); 

        } catch (Exception e) {  
            e.printStackTrace();  
        }
        
        SaveToXml.saveXMltoDB(doc);
	}

	@Override
	public List<String> getXML() {
		Connection conn = null;
		List<String> results = null;
  	  	try {
			Class.forName("com.mysql.jdbc.Driver") ;
		
		  	conn = DriverManager.getConnection("jdbc:mysql://localhost/company2?useSSL=false&createDatabaseIfNotExist=true", 
		  			"root", "root") ;
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


}
