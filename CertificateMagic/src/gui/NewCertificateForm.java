package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.CertificateModel;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import certificates.CertificateGenerator;
import security.IssuerData;
import security.SubjectData;

public class NewCertificateForm extends JDialog {

	private JLabel signature = new JLabel("Issuer: ");
	private JLabel name = new JLabel("Common Name (CN):");
	private JLabel oUnit = new JLabel("Surname (SN):");
	private JLabel sName = new JLabel("Given Name (GN):");
	private JLabel oName = new JLabel("Organisation Name (ON):");
	private JLabel lName = new JLabel("Locality Name (L):");
	private JLabel country = new JLabel("Country (C):");
	private JLabel email = new JLabel("Email (E):");
	
	private JComboBox issuer = new JComboBox();
	private JTextField name_text = new JTextField(20);
	private JTextField oUnit_text = new JTextField(20);
	private JTextField oName_text = new JTextField(20);
	private JTextField lName_text = new JTextField(20);
	private JTextField sName_text = new JTextField(20);
	private JTextField country_text = new JTextField(20);
	private JTextField email_text = new JTextField(20);
	
	private JCheckBox check_box = new JCheckBox();
	
	private JButton save = new JButton("Ok");
	private JButton cancel = new JButton("Cancel");
	
	private JPanel main_panel = new JPanel();
	private JPanel[] main_panels = new JPanel[10];
	
	
	private CertificateModel cm = new CertificateModel();
	//sertifikati koji ce biti ucitani iz fajla (za combo box)
	private Map<String, CertificateModel> certificateModels = new HashMap<String, CertificateModel>();
	
	@SuppressWarnings("unchecked")
	public NewCertificateForm() {
		
		setupDialog();
		
		//ucitavanje CA sertifikata za combo box
		try {
			certificateModels = cm.load();
			//issuer.addItem("");
			for (String k : certificateModels.keySet()) {
				CertificateModel certMod = certificateModels.get(k);
				if(certMod.isCA() && certMod.isValid()) {
					Date now = new Date();
					if(now.before(certMod.getEndDate())){
						issuer.addItem(k);
					} else {
						certMod.setValid(false);
					}
				}
			}
		} catch (ClassNotFoundException | IOException e2) {
			e2.printStackTrace();
		}
		
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e1) {

				// preuzima se izabrano sertifikaciono telo
				String nameOfCA = (String) issuer.getSelectedItem();
				
				if(nameOfCA.equals("") && !check_box.isSelected()){
					JOptionPane.showMessageDialog(new JFrame(), "Please choose a CA certificate.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(name_text.getText().trim().equals("") || oUnit_text.getText().trim().equals("") || oName_text.getText().trim().equals("") ||
						lName_text.getText().trim().equals("") || sName_text.getText().trim().equals("") || country_text.getText().trim().equals("") || email_text.getText().trim().equals("")){
					JOptionPane.showMessageDialog(new JFrame(), "Please enter all values!", "Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// klasa koja ima metodu koja od IssuerData i SubjectData napravi sertifikat
				CertificateGenerator cg = new CertificateGenerator();
				// generise se par kljuceva za izdavaoca
				KeyPair keyPairIssuer = cg.generateKeyPair();

				// podaci za subjecta i issuera
				X500NameBuilder subjectBuilder = new X500NameBuilder(BCStyle.INSTANCE);
				X500NameBuilder issuerBuilder = new X500NameBuilder(BCStyle.INSTANCE);

				//U slucaju sertifikata koji nije samopotpisan bice potrebno ovo:
				CertificateModel issuerCertificate =  new CertificateModel();
				
				// Issuer se popunjava podacima iz forme u slucaju SAMOPOTPISANOG
				if(nameOfCA.equals("")) {
					issuerBuilder.addRDN(BCStyle.CN, name_text.getText());
					issuerBuilder.addRDN(BCStyle.SURNAME, oUnit_text.getText());
					issuerBuilder.addRDN(BCStyle.GIVENNAME, oUnit_text.getText());
					issuerBuilder.addRDN(BCStyle.O, oName_text.getText());
					issuerBuilder.addRDN(BCStyle.OU, oUnit_text.getText());
					issuerBuilder.addRDN(BCStyle.C, country_text.getText());
					issuerBuilder.addRDN(BCStyle.E, email_text.getText());
					// UID (USER ID) je ID korisnika
					String uid = UUID.randomUUID().toString();
					issuerBuilder.addRDN(BCStyle.UID, uid);
				} else {
					
					//NIJE SAMOPOTPISANI: Potrebno je pronaci ceo sertifikat koji je izabran u combo boxu
					for(String k : certificateModels.keySet()) {
						if(nameOfCA.equals(k)) {
							issuerCertificate = certificateModels.get(k);
							break;
						}
					}
					
					issuerBuilder.addRDN(BCStyle.CN, issuerCertificate.getCommon_name());
					issuerBuilder.addRDN(BCStyle.SURNAME, issuerCertificate.getSurname());
					issuerBuilder.addRDN(BCStyle.GIVENNAME, issuerCertificate.getGivenname());
					issuerBuilder.addRDN(BCStyle.O, issuerCertificate.getoName());
					issuerBuilder.addRDN(BCStyle.OU, issuerCertificate.getoUnit());
					issuerBuilder.addRDN(BCStyle.C, issuerCertificate.getCountry_text());
					issuerBuilder.addRDN(BCStyle.E, issuerCertificate.getEmail());
				    issuerBuilder.addRDN(BCStyle.UID, issuerCertificate.getUid());
				}
				
				// Subject se u svakom slucaju popunjava podacima iz forme
				subjectBuilder.addRDN(BCStyle.CN, name_text.getText());
				subjectBuilder.addRDN(BCStyle.SURNAME, oUnit_text.getText());
				subjectBuilder.addRDN(BCStyle.GIVENNAME, oUnit_text.getText());
				subjectBuilder.addRDN(BCStyle.O, oName_text.getText());
				subjectBuilder.addRDN(BCStyle.OU, oUnit_text.getText());
				subjectBuilder.addRDN(BCStyle.C, country_text.getText());
				subjectBuilder.addRDN(BCStyle.E, email_text.getText());
				// UID (USER ID) je ID korisnika
				String uid = UUID.randomUUID().toString();
				subjectBuilder.addRDN(BCStyle.UID, uid);

				// Datumi - pocetni je danasnji, krajnji za godinu dana
				//SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.YEAR, 1);
				Date endDate = c.getTime();

				// Serijski broj sertifikata
				int randomNum = 0 + (int) (Math.random() * 10000000);
				String sn = String.valueOf(randomNum);

				// Kreiraju se podaci za issuera i subjecta
				IssuerData issuerData = new IssuerData(keyPairIssuer.getPrivate(), issuerBuilder.build());
				SubjectData subjectData = new SubjectData(keyPairIssuer.getPublic(), subjectBuilder.build(), 
						sn, startDate, endDate);

				// generise se sertifikat na osnovu njihovih podataka
				X509Certificate cert = cg.generateCertificate(subjectData, issuerData);
				System.out.println("Napravio sertifikat!");
				
				// Moguce je proveriti da li je digitalan potpis sertifikata ispravan, upotrebom javnog kljuca izdavaoca
				try {
					cert.verify(keyPairIssuer.getPublic());
					System.out.println("\nValidacija uspesna :)");
				} catch (InvalidKeyException | CertificateException
						| NoSuchAlgorithmException | NoSuchProviderException
						| SignatureException e2) {
					e2.printStackTrace();
				}
				
				//Cuvanje sertifikata
				try {
					cg.saveCert(cert, name_text.getText());
				} catch (CertificateEncodingException e) {
					e.printStackTrace();
				}
				
				//razlika izmedju fizickih lica i sertifikacionih tela
				boolean isCA = false;
				if(check_box.isSelected()) {
					isCA = true;
				}
				
				//Pravljenje i cuvanje sertifikata za buduce ponude u combo boxu
				cm = new CertificateModel(name_text.getText(), oUnit_text.getText(), oUnit_text.getText(), 
						oName_text.getText(), oUnit_text.getText(), 
						country_text.getText(), email_text.getText(), uid, isCA, randomNum, endDate);
				certificateModels.put(name_text.getText(), cm);
				
				try {
					cm.save(certificateModels);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(randomNum);
				if(nameOfCA.equalsIgnoreCase("")){
					//ZA SAMOPOTPISANE - POTREBNA SAMO PRVA DVA PARAMETRA
					KeyStoreForm ksf = new KeyStoreForm(keyPairIssuer, cert, name_text.getText(), null, cm, null) ;
					ksf.setVisible(true);
				} else {
					PasswordForm pf = new PasswordForm(issuerCertificate.getKs().getPassAlias(), keyPairIssuer, cert, name_text.getText(), cm, issuerCertificate);
					pf.setVisible(true);
				}
				dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
	
	
	@SuppressWarnings("unchecked")
	private void setupDialog(){
		
		setModal(true);
		setTitle("New certificate");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		issuer.addItem("");
		
		for (int i = 0; i < main_panels.length-1; i++) {
			main_panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		//main_panels[8] = new JPanel(new FlowLayout(FlowLayout.CENTER));
		main_panels[9] = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));

		main_panels[0].add(signature);
		main_panels[0].add(issuer);

		main_panels[1].add(name);
		main_panels[1].add(name_text);

		main_panels[2].add(oUnit);
		main_panels[2].add(oUnit_text);

		main_panels[3].add(oName);
		main_panels[3].add(oName_text);

		main_panels[4].add(lName);
		main_panels[4].add(lName_text);

		main_panels[5].add(sName);
		main_panels[5].add(sName_text);

		main_panels[6].add(country);
		main_panels[6].add(country_text);

		main_panels[7].add(email);
		main_panels[7].add(email_text);

		main_panels[8].add(check_box);
		check_box.setText("for CA");
		
		main_panels[9].add(save);
		main_panels[9].add(cancel);
		
		for (int i = 0; i < main_panels.length; i++) {
			main_panel.add(main_panels[i]);
		}
		
		add(main_panel);
		pack();
		setLocationRelativeTo(null);
		
	}
}
