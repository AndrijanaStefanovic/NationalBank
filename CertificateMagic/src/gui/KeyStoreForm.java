package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.CertificateModel;
import model.KeyStoreModel;
import security.KeyStoreWriter;

public class KeyStoreForm extends JDialog {
	
	private JLabel aliasName = new JLabel("Alias Name: ");
	private JLabel keyPass = new JLabel("Keystore password :");
	private JLabel aliasPass = new JLabel("Alias password");
	
	private JTextField alias_text = new JTextField(10);
	private JTextField key_pass_text = new JTextField(10);
	private JTextField alias_pass_text = new JTextField(10);
	
	private JPanel main_panel = new JPanel();
	private JPanel[] main_panels = new JPanel[4];
	
	private JButton btnOk = new JButton("Ok");
	
	private PrivateKey privateKey;
	private X509Certificate cert;
	private String alias;
	private String filePath;
	private CertificateModel subjectCertificate;
	private CertificateModel issuserCertificate;
	private CertificateModel helperCert = new CertificateModel();
	private Map<String, CertificateModel> certificateModels = new HashMap<String, CertificateModel>(); 
	
	public KeyStoreForm(KeyPair kp, X509Certificate c, String a, String ksFilePath, CertificateModel subject, CertificateModel issuer) {
		
		this.cert = c;
		this.privateKey = kp.getPrivate();
		this.alias = a;
		this.filePath = ksFilePath;
		this.issuserCertificate = issuer;
		this.subjectCertificate = subject;
		
		setupDialog();
		
		try {
			certificateModels = helperCert.load();
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(alias_text.getText().trim().equals("") || key_pass_text.getText().trim().equals("") || alias_text.getText().equals("")){
					JOptionPane.showMessageDialog(new JFrame(), "Empty field!", "Ok", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				//cert + private key
				KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
				
				if(filePath == null){ //u slucaju samopotpisanog pravimo key store sa unetim parametrima
					keyStoreWriter.loadKeyStore(filePath, key_pass_text.getText().toCharArray());
				}else{
					//u drugom slucaju otvaramo key store issuera
					keyStoreWriter.loadKeyStore(filePath, issuserCertificate.getKs().getPassKS().toCharArray());
				}
				
				//keyStoreWriter.loadKeyStore(null, alias_text.getText().toCharArray());
				keyStoreWriter.write(alias, privateKey, alias_pass_text.getText().toCharArray(), cert);
				keyStoreWriter.saveKeyStore("./data/"+alias_text.getText()+".jks", key_pass_text.getText().toCharArray());
				System.out.println("Zavrsio sa key storeom.");
				
			
				/*//citanje key storea: alias + alias pass + key pass
				KeyStoreReader keyStoreReader = new KeyStoreReader();
				keyStoreReader.readKeyStore("./data/"+alias_text.getText()+".jks", alias_pass_text.getText().toCharArray(),
						key_pass_text.getText().toCharArray(), alias_text.getText());*/
				
				
				//Za cuvanje u datoteci i postavljanje key storea za dati sertifikat
				KeyStoreModel keyStoreModel = new KeyStoreModel(alias_text.getText(), key_pass_text.getText(),
						alias, alias_pass_text.getText());
				
				for(Entry<String, CertificateModel> entry : certificateModels.entrySet()){
					if(subjectCertificate.getCommon_name().equals(entry.getKey())){
						entry.getValue().setKs(keyStoreModel);
						break;
					}
				}
				
				//Ako postoji izdavalac:
				if(issuserCertificate != null){
					for(Entry<String, CertificateModel> entry : certificateModels.entrySet()){
						if(issuserCertificate.getCommon_name().equals(entry.getKey())){
							entry.getValue().addKS(keyStoreModel);
							break;
						}
					}
				}
				
				try {
					helperCert.save(certificateModels);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				dispose();
			}
		});
	}
	 
	
	
	private void setupDialog() {
		
		setModal(true);
		setTitle("New keystore");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		for (int i = 0; i < main_panels.length-1; i++) {
			main_panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		
		main_panels[3] = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
		
		main_panels[0].add(aliasName);
		main_panels[0].add(alias_text);
		
		main_panels[1].add(keyPass);
		main_panels[1].add(key_pass_text);
		
		main_panels[2].add(aliasPass);
		main_panels[2].add(alias_pass_text);
		
		main_panels[3].add(btnOk);
		//main_panels[3].add(btnCancel);
		
		for (int i = 0; i < main_panels.length; i++) {
			main_panel.add(main_panels[i]);
		}
		
		
		add(main_panel);
		pack();
		setLocationRelativeTo(null);
		
		
	}
}

