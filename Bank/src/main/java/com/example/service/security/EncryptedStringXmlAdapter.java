package com.example.service.security;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;

public class EncryptedStringXmlAdapter extends XmlAdapter<String,String> {
	
	private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	
	private static String k;
	
	public static void setKey(String key){
		k = key;
	}

	public EncryptedStringXmlAdapter() {
        encryptor.setPassword(k);
      //  encryptor.setAlgorithm("PBEWITHHMACSHA256ANDAES_256");
	}
	 
	 @Override
     public String marshal(String plaintext) throws Exception {
		 if(!k.equals("inv"))
             return PropertyValueEncryptionUtils.encrypt(plaintext, encryptor);
		 else return plaintext;
     }

	 @Override
     public String unmarshal(String cyphertext) throws Exception {
		 	if (PropertyValueEncryptionUtils.isEncryptedValue(cyphertext))
		 		if(!k.equals("inv"))
                    return PropertyValueEncryptionUtils.decrypt(cyphertext, encryptor);
		 		else
		 			return cyphertext;
		 	return cyphertext;
     }

}
