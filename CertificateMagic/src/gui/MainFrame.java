package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {

	public static MainFrame instance;
	private JMenuBar menuBar;
	
	public MainFrame() {
		setTitle("Certificate generator");
		setSize(new Dimension(500,300));
		setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		JMenuItem newSertificate = new JMenuItem("New Certificate");
		
		newSertificate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				NewCertificateForm f = new NewCertificateForm();
				f.setVisible(true);
			}
		});

		file.add(newSertificate);
		menuBar.add(file);
		setJMenuBar(menuBar);
	}
	
	public static MainFrame getInstance(){
		if (instance==null)
			instance=new MainFrame();
		return instance;
	}
}
