package model;

import java.io.Serializable;

public class KeyStoreModel implements Serializable{

	private String alias;
	private String passKS;
	private String name;
	private String passAlias;
	
	public KeyStoreModel(String alias, String passKS, String name, String passID) {
		super();
		this.alias = alias;
		this.passKS = passKS;
		this.name = name;
		this.passAlias = passID;
	}

	public KeyStoreModel() {
		super();
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPassKS() {
		return passKS;
	}

	public void setPassKS(String passKS) {
		this.passKS = passKS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassAlias() {
		return passAlias;
	}

	public void setPassAlias(String passAlias) {
		this.passAlias = passAlias;
	}
}
