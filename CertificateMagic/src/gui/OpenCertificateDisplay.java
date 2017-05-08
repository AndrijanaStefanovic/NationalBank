package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.CertificateModel;

public class OpenCertificateDisplay extends JDialog {

	private JLabel name = new JLabel("Common Name (CN):");
	private JLabel oUnit = new JLabel("Surname (SN):");
	private JLabel sName = new JLabel("Given Name (GN):");
	private JLabel oName = new JLabel("Organisation Name (ON):");
	private JLabel country = new JLabel("Country (C):");
	private JLabel email = new JLabel("Email (E):");
	private JLabel endDate = new JLabel("End date :");
	private JLabel isValid = new JLabel("Is valid :");
	
	private JLabel name2 = new JLabel();
	private JLabel oUnit2 = new JLabel();
	private JLabel sName2 = new JLabel();
	private JLabel oName2 = new JLabel();
	private JLabel country2 = new JLabel();
	private JLabel email2 = new JLabel();
	private JLabel endDate2 = new JLabel();
	private JLabel isValid2 = new JLabel();
	
	private JPanel main_panel = new JPanel();
	private JPanel[] main_panels = new JPanel[8];
	
	public OpenCertificateDisplay(CertificateModel certificate) {
		
		name2.setText(certificate.getCommon_name());
		oUnit2.setText(certificate.getSurname());
		sName2.setText(certificate.getGivenname());
		oName2.setText(certificate.getoUnit());
		country2.setText(certificate.getCountry_text());
		email2.setText(certificate.getEmail());
		endDate2.setText(certificate.getEndDate().toString());
		if(certificate.isValid())
			isValid2.setText("Yes");
		else
			isValid2.setText("No");
		
		for (int i = 0; i < main_panels.length; i++) {
			main_panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));

		main_panels[0].add(name);
		main_panels[0].add(name2);

		main_panels[1].add(oUnit);
		main_panels[1].add(oUnit2);

		main_panels[2].add(oName);
		main_panels[2].add(oName2);


		main_panels[3].add(sName);
		main_panels[3].add(sName2);

		main_panels[4].add(country);
		main_panels[4].add(country2);

		main_panels[5].add(email);
		main_panels[5].add(email2);

		main_panels[6].add(endDate);
		main_panels[6].add(endDate2);
		
		main_panels[7].add(isValid);
		main_panels[7].add(isValid2);
		
		for (int i = 0; i < main_panels.length; i++) {
			main_panel.add(main_panels[i]);
		}
		
		add(main_panel);
		pack();
		
		setModal(true);
		setLocationRelativeTo(null);
	}
}
