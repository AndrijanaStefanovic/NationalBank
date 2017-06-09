package com.example.Company.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Invoice {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String messageId;
	
	@Column
	private String supplierName;
	
	@Column
	private String supplierAddress;
	
	@Column
	private String supplierPIB;
	
	@Column
	private String buyerName;
	
	@Column
	private String buyerAddress;
	
	@Column
	private String buyerPIB;
	
	@Column
	private int accountNumber;
	
	@Column
	private Date dateOfInvoice;
	
	@Column 
	private double merchandiseValue;
	
	@Column
	private double servicesValue;
	
	@Column
	private double totalValue;
	
	@Column
	private double totalDiscount;
	
	@Column
	private double totalTax;
	
	@Column
	private String currency;
	
	@Column
	private double totalDue;
	
	@Column
	private double billingAccountNumber;
	
	@Column
	private Date dateOfValue;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
	private List<InvoiceItem> invoiceItems;
	
	public Invoice(){}
	
	public Invoice(String messageId, String supplierName, String supplierAdress, String supplierPIB, String buyerName,
			String buyerAdress, String buyerPIB, int accountNumber, Date dateOfInvoice, double merchandiseValue,
			double servicesValue, double totalValue, double totalDiscount, double totalTax, String currency,
			double totalDue, double billingAccountNumber, Date dateOfValue) {
		super();
		this.messageId = messageId;
		this.supplierName = supplierName;
		this.supplierAddress = supplierAdress;
		this.supplierPIB = supplierPIB;
		this.buyerName = buyerName;
		this.buyerAddress = buyerAdress;
		this.buyerPIB = buyerPIB;
		this.accountNumber = accountNumber;
		this.dateOfInvoice = dateOfInvoice;
		this.merchandiseValue = merchandiseValue;
		this.servicesValue = servicesValue;
		this.totalValue = totalValue;
		this.totalDiscount = totalDiscount;
		this.totalTax = totalTax;
		this.currency = currency;
		this.totalDue = totalDue;
		this.billingAccountNumber = billingAccountNumber;
		this.dateOfValue = dateOfValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAdress) {
		this.supplierAddress = supplierAdress;
	}

	public String getSupplierPIB() {
		return supplierPIB;
	}

	public void setSupplierPIB(String supplierPIB) {
		this.supplierPIB = supplierPIB;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAdress) {
		this.buyerAddress = buyerAdress;
	}

	public String getBuyerPIB() {
		return buyerPIB;
	}

	public void setBuyerPIB(String buyerPIB) {
		this.buyerPIB = buyerPIB;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getDateOfInvoice() {
		return dateOfInvoice;
	}

	public void setDateOfInvoice(Date dateOfInvoice) {
		this.dateOfInvoice = dateOfInvoice;
	}

	public double getMerchandiseValue() {
		return merchandiseValue;
	}

	public void setMerchandiseValue(double merchandiseValue) {
		this.merchandiseValue = merchandiseValue;
	}

	public double getServicesValue() {
		return servicesValue;
	}

	public void setServicesValue(double servicesValue) {
		this.servicesValue = servicesValue;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getTotalDue() {
		return totalDue;
	}

	public void setTotalDue(double totalDue) {
		this.totalDue = totalDue;
	}

	public double getBillingAccountNumber() {
		return billingAccountNumber;
	}

	public void setBillingAccountNumber(double billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}

	public Date getDateOfValue() {
		return dateOfValue;
	}

	public void setDateOfValue(Date dateOfValue) {
		this.dateOfValue = dateOfValue;
	}

	public List<InvoiceItem> getInvoiceItems() {
		return invoiceItems;
	}

	public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}
}
