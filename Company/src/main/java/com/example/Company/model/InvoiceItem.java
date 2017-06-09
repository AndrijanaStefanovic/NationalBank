package com.example.Company.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class InvoiceItem {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private int number;
	
	@Column
	private String name;
	
	@Column
	private double amount;
	
	@Column
	private String measurmentUnit;
	
	@Column
	private double unitPrice;
	
	@Column
	private double value;
	
	@Column
	private double discountPercent;
	
	@Column
	private double totalDiscount;
	
	@Column
	private double subtractedDiscount;
	
	@Column
	private double totalTax;
	
	@Column
	private String kind; //merchandise or service

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;
	
	public InvoiceItem(){}
	
	public InvoiceItem(int number, String name, double amount, String measurmentUnit, double unitPrice, double value,
			double discountPercent, double totalDiscount, double subtractedDiscount, double totalTax, String kind) {
		super();
		this.number = number;
		this.name = name;
		this.amount = amount;
		this.measurmentUnit = measurmentUnit;
		this.unitPrice = unitPrice;
		this.value = value;
		this.discountPercent = discountPercent;
		this.totalDiscount = totalDiscount;
		this.subtractedDiscount = subtractedDiscount;
		this.totalTax = totalTax;
		this.kind = kind;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMeasurmentUnit() {
		return measurmentUnit;
	}

	public void setMeasurmentUnit(String measurmentUnit) {
		this.measurmentUnit = measurmentUnit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public double getSubtractedDiscount() {
		return subtractedDiscount;
	}

	public void setSubtractedDiscount(double subtractedDiscount) {
		this.subtractedDiscount = subtractedDiscount;
	}

	public double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	
	
}
