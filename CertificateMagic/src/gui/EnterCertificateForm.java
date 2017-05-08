package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
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

public class EnterCertificateForm extends JDialog{

	private JLabel serialNumber = new JLabel("Certificate serial number: ");
	private JTextField serialNumber_text = new JTextField(20);
	private JButton btnOk = new JButton("Ok");
	private JButton btnCancel = new JButton("Cancel");
	private JPanel main_panel = new JPanel();
	private JPanel[] main_panels = new JPanel[2];
	private CertificateModel helperCertificate = new CertificateModel();
	private HashMap<String, CertificateModel> certificateModels = new HashMap<String, CertificateModel>();
	private String mode;
	
	public EnterCertificateForm(String formMode) {
		this.mode = formMode;
		setupDialog();
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int sn = 0;
				try{
					sn = Integer.parseInt(serialNumber_text.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a valid serial number.", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				try {
					certificateModels = helperCertificate.load();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
					System.out.println("Unable to read certificates.");
					dispose();
				}
				
				boolean found = false;
				for (CertificateModel cm : certificateModels.values()) {
					if(cm.getSerialNumber() == sn) {
						found = true;
						if(mode.equals("OPEN")){
							OpenCertificateDisplay ocd = new OpenCertificateDisplay(cm);
							ocd.setVisible(true);
						} else if (mode.equals("WITHDRAW")){
							cm.setValid(false);
							try {
								helperCertificate.save(certificateModels);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							JOptionPane.showMessageDialog(new JFrame(), "Certificate successfully withdrawn.", "Success", JOptionPane.INFORMATION_MESSAGE);
						} else {
							if(cm.isValid()) {
								JOptionPane.showMessageDialog(new JFrame(), "Certificate is valid.", "Info", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(new JFrame(), "Certificate isn't valid.", "Info", JOptionPane.INFORMATION_MESSAGE);
							}
						}
						break;
					}
				}
				if(!found) {
					JOptionPane.showMessageDialog(new JFrame(), "Unable to find the certificate.", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				dispose();
			}
		});
		
	}
	
	private void setupDialog(){
		
		setModal(true);
		if(mode.equals("OPEN"))
			setTitle("Open certificate");
		else 
			setTitle("Withdraw certificate");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		for (int i = 0; i < main_panels.length-1; i++) {
			main_panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		main_panels[1] = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
		main_panels[0].add(serialNumber);
		main_panels[0].add(serialNumber_text);
		main_panels[1].add(btnOk);
		main_panels[1].add(btnCancel);
		
		for (int i = 0; i < main_panels.length; i++) {
			main_panel.add(main_panels[i]);
		}
		
		add(main_panel);
		pack();
		setLocationRelativeTo(null);
	}
}
