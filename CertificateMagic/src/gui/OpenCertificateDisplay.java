package gui;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import model.CertificateModel;

public class OpenCertificateDisplay extends JDialog {

	private JTextArea textArea = new JTextArea();
	
	public OpenCertificateDisplay(CertificateModel certificate) {
		textArea.setText(certificate.toString());
		textArea.setSize(new Dimension(300,300));
		add(textArea);
		setModal(true);
		setSize(new Dimension(300,300));
		setLocationRelativeTo(null);
	}
}
