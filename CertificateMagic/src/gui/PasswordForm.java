package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.CertificateModel;


public class PasswordForm extends JDialog{

	private JLabel pass = new JLabel("Password :");
	private JTextField pass_text = new JTextField(10);
	private JButton btnOk = new JButton("Ok");
	private JPanel main_panel = new JPanel();
	private JPanel[] main_panels = new JPanel[3];
	
	private String CAPass;
	private KeyPair keyPair;
	private X509Certificate certificate;
	private String alias;
	private CertificateModel subjectCertificate;
	private CertificateModel issuerCertificate;
	
	//KeyPair keyPair, X509Certificate cert, String a, Sertifikat sertifikatNovi, String passCA, Sertifikat sertifikatStari
	public PasswordForm(String CAPassword, KeyPair kp, X509Certificate cert, String a, CertificateModel subject, CertificateModel issuer){
		setupDialog();
		
		this.CAPass = CAPassword;
		this.keyPair = kp;
		this.certificate = cert;
		this.alias = a;
		this.subjectCertificate = subject;
		this.issuerCertificate = issuer;
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(pass_text.getText().trim().equals("")){
					JOptionPane.showMessageDialog(new JFrame(), "Please enter the password!", "Ok", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				if(pass_text.getText().equals(CAPass)){
					System.out.println("POKLAPAJU SE!");
					//Pravi se novi key store
					//X509Certificate c, PrivateKey pk, String a, CertificateModel subject,  String ksFilePath CertificateModel issuer
					KeyStoreForm ksf = new KeyStoreForm(keyPair, certificate, alias, "./data/" + issuerCertificate.getKs().getAlias()+".jks",subjectCertificate, issuerCertificate);
					ksf.setVisible(true);
					dispose();
					
				} else {
					System.out.println("NE POKLAPAJU SE!");
				}
				dispose();
			}
		});
			
			
	}
	
	
	private void setupDialog() {
		
		setModal(true);
		setTitle("Password check");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		for (int i = 0; i < main_panels.length-1; i++) {
			main_panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		
		main_panels[2] = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
		
		main_panels[0].add(pass);
		main_panels[0].add(pass_text);
		
		main_panels[1].add(btnOk);
		
		main_panel.add(main_panels[0]);
		main_panel.add(main_panels[1]);
		
		add(main_panel);
		pack();
		setLocationRelativeTo(null);
		
		for (int i = 0; i < main_panels.length; i++) {
			main_panel.add(main_panels[i]);
		}
	}
}
