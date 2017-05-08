package certificates;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import data.IssuerData;
import data.SubjectData;
import keystores.KeyStoreWriter;

public class CertificateExample{
	public CertificateExample() {
		Security.addProvider(new BouncyCastleProvider());
	}
		
	public void testIt() {
		try {
			SubjectData subjectData = generateSubjectData();
			
			KeyPair keyPairIssuer = generateKeyPair();
			IssuerData issuerData = generateIssuerData(keyPairIssuer.getPrivate());
		    
			CertificateGenerator certificateGenerator = new CertificateGenerator();
			X509Certificate cert = certificateGenerator.generateCertificate(subjectData, issuerData);
			
			System.out.println("\n===== Podaci o izdavacu sertifikata =====");
			System.out.println(cert.getIssuerX500Principal().getName());
			System.out.println("\n===== Podaci o vlasniku sertifikata =====");
			System.out.println(cert.getSubjectX500Principal().getName());
			System.out.println("\n===== Sertifikat =====");
			System.out.println("-------------------------------------------------------");
			System.out.println(cert);
			System.out.println("-------------------------------------------------------");
			
			cert.verify(keyPairIssuer.getPublic());
			System.out.println("\nValidacija uspesna :)");
			
			KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
			keyStoreWriter.loadKeyStore(null, "test".toCharArray());
			keyStoreWriter.write("admin", keyPairIssuer.getPrivate(), "admin".toCharArray(), cert);
			keyStoreWriter.saveKeyStore("./data/test.jks", "admin".toCharArray());
			
//			KeyPair anotherPair = generateKeyPair();
//			cert.verify(anotherPair.getPublic());
			
		} catch(CertificateException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			System.out.println("\nValidacija neuspesna :(");
			e.printStackTrace();
		}
	}
	
	private KeyPair generateKeyPair() {
        try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	private SubjectData generateSubjectData() {
		try {
			KeyPair keyPairSubject = generateKeyPair();
			
			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse("2017-12-31");
			Date endDate = iso8601Formater.parse("2022-12-31");
			
			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
								    builder.addRDN(BCStyle.CN, "Goran Sladic");
								    builder.addRDN(BCStyle.SURNAME, "Sladic");
								    builder.addRDN(BCStyle.GIVENNAME, "Goran");
								    builder.addRDN(BCStyle.O, "UNS-FTN");
								    builder.addRDN(BCStyle.OU, "Katedra za informatiku");
								    builder.addRDN(BCStyle.C, "RS");
								    builder.addRDN(BCStyle.E, "sladicg@uns.ac.rs");
								    builder.addRDN(BCStyle.UID, "123456");
		    
		    return new SubjectData(keyPairSubject.getPublic(), builder.build(), "1", startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private IssuerData generateIssuerData(PrivateKey issuerKey) {
		
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
							    builder.addRDN(BCStyle.CN, "Nikola Luburic");
							    builder.addRDN(BCStyle.SURNAME, "Luburic");
							    builder.addRDN(BCStyle.GIVENNAME, "Nikola");
							    builder.addRDN(BCStyle.O, "UNS-FTN");
							    builder.addRDN(BCStyle.OU, "Katedra za informatiku");
							    builder.addRDN(BCStyle.C, "RS");
							    builder.addRDN(BCStyle.E, "nikola.luburic@uns.ac.rs");
							    builder.addRDN(BCStyle.UID, "654321");

		return new IssuerData(issuerKey, builder.build());
	}
	
	public static void main(String[] args) {
		CertificateExample example = new CertificateExample();
		example.testIt();
	}
}
