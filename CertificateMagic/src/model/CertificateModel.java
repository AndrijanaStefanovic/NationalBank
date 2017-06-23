package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CertificateModel implements Serializable {
	
	private String common_name;
	private String surname;
	private String givenname;
	private String oName;
	private String oUnit;
	private String country_text;
	private String email;
	private String uid;
	private boolean isCA;
	private int serialNumber;
	private boolean isValid;
	private KeyStoreModel ks;
	private Date endDate;
	private Map<Integer, KeyStoreModel> keyset = new HashMap<Integer, KeyStoreModel>();
	
	public CertificateModel() {
		super();
	}
	
	public CertificateModel(String common_name, String surname, String givenname, String oName,
			String oUnit, String country_text, String email, String uid, boolean isCA, int serialNumber, Date endDate) {
		super();
		this.common_name = common_name;
		this.surname = surname;
		this.givenname = givenname;
		this.oName = oName;
		this.oUnit = oUnit;
		this.country_text = country_text;
		this.email = email;
		this.uid = uid;
		this.isCA = isCA;
		this.serialNumber = serialNumber;
		this.isValid = true;
		this.endDate = endDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public boolean isCA() {
		return isCA;
	}

	public void setCA(boolean isCA) {
		this.isCA = isCA;
	}

	public KeyStoreModel getKs() {
		return ks;
	}

	public void setKs(KeyStoreModel ks) {
		this.ks = ks;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCommon_name() {
		return common_name;
	}
	
	public void setCommon_name(String common_name) {
		this.common_name = common_name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGivenname() {
		return givenname;
	}

	public void setGivenname(String givenname) {
		this.givenname = givenname;
	}
	
	public String getoName() {
		return oName;
	}
	
	public void setoName(String oName) {
		this.oName = oName;
	}
	
	public String getoUnit() {
		return oUnit;
	}
	
	public void setoUnit(String oUnit) {
		this.oUnit = oUnit;
	}
	
	public String getCountry_text() {
		return country_text;
	}
	
	public void setCountry_text(String country_text) {
		this.country_text = country_text;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "CertificateModel [common_name=" + common_name + ", surname="
				+ surname + ", givenname=" + givenname +", oName=" + oName + ", oUnit=" + oUnit
				+ ", country_text=" + country_text + ", email=" + email + "]";
	}
	
	public void save(Map<String, CertificateModel> certificates) throws FileNotFoundException, IOException{
		ObjectOutputStream out1 = new ObjectOutputStream(new FileOutputStream("./data/certificates.dat"));
		out1.writeObject(certificates);
		out1.flush();
		out1.close();
	}
	
	
	@SuppressWarnings("unchecked")
	public HashMap<String, CertificateModel> load() throws FileNotFoundException, IOException, ClassNotFoundException{
		Map<String, CertificateModel> certificates = new HashMap<String, CertificateModel>();
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("./data/certificates.dat"));
		certificates =  (Map<String, CertificateModel>) in.readObject();
		in.close();
		return (HashMap<String, CertificateModel>) certificates;
	}

	public void addKS(KeyStoreModel keyStoreModel) {
		keyset.put(keyset.size()+1, ks);
	}
}
