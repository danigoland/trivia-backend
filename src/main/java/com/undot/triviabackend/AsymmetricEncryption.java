package com.undot.triviabackend;


import java.io.IOException;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;


public class AsymmetricEncryption {

    static final String TAG = "AsymmetricAlgorithmRSA";

	// Generate key pair for 1024-bit RSA encryption and decryption
	 PublicKey publicKey = null;
	 PrivateKey privateKey = null;
	 PublicKey ServerPublicKey = null;
	 PrivateKey ServerPrivateKey = null;

	public PublicKey getPublicKey(){
		return publicKey;
	}
	public PrivateKey getPrivateKey(){
		return privateKey;
	}
	
	 String publicKeyS;
	 String privateKeyS;
	 String ServerPublicKeyS;
	 String ServerPrivateKeyS;

	static String algorithm = "RSA";
	
	
	public AsymmetricEncryption() {
    	 
    }
    // we want to call this function only when receiving sms
    public void makeNewKeys(){
   	try {
   		 KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
   		 kpg.initialize(1024);
   		 KeyPair kp = kpg.genKeyPair();
   	 	 publicKey = kp.getPublic();
   	  	 privateKey = kp.getPrivate();   	
   	  	 
   		//String base64String = Base64.byteArrayToBase64(currentString.getBytes("UTF-8"));
   		
   		
   	/*	
   		 KeyPair kp2 = kpg.genKeyPair();
		// ServerPublicKey = kp2.getPublic();
		 ServerPrivateKey = kp2.getPrivate();
		 //ServerPublicKey.fromString
		 PublicKey pubKey = kp2.getPublic();
		 String SS = savePublicKey(pubKey);		 
   	     SaveServerKey(SS);
   	     ServerPublicKey = LoadServerKey();*/


   	   } 
    	
   	   catch (Exception e) {
    		 Debug.rsa("RSA key pair error" + e.toString());
       }
   	
    }
 
    
    ///////////////////////////////////////////////////////////////////// 	

    public static String decrypt(String encrypted, PrivateKey key) throws Exception{
			Cipher c = Cipher.getInstance(algorithm);
			//c =Cipher.getInstance("RSA/NONE/NoPadding");
			c.init(Cipher.DECRYPT_MODE, key);	//byte[] bts = fromHexString(encrypted);			
    		byte[] bts = Base64.base64ToByteArray(encrypted);
    		byte[] decrypted = blockCipher(bts,Cipher.DECRYPT_MODE,c);
    		return new String(decrypted,"UTF-8");

    	}
    
   //////////////////////////////////////////////////////////////////// 	
    	public static String encrypt(String plaintext, PublicKey key) throws Exception{
    		Cipher c = Cipher.getInstance(algorithm);
		//	c =Cipher.getInstance("RSA/NONE/NoPadding");
    		
			c.init(Cipher.ENCRYPT_MODE,key);
    		    
    		byte[] bytes = plaintext.getBytes("UTF-8");
    		byte[] encrypted = blockCipher(bytes,Cipher.ENCRYPT_MODE,c);   		
    		String encryptedTranspherable =  Base64.byteArrayToBase64(encrypted) ;
    		//String encryptedTranspherable = asHex (encrypted); 
    		return  encryptedTranspherable;
    	}
    	   ///////////////////////////////////////////////////////////////////// 	
    	public String encrypt(String plaintext, Key ServerPublicKey) throws Exception{
    		Cipher c = Cipher.getInstance(algorithm);
		//	c =Cipher.getInstance("RSA/NONE/NoPadding");

    		c.init(Cipher.ENCRYPT_MODE, ServerPublicKey);
 
    		byte[] bytes = plaintext.getBytes("UTF-8");
    		byte[] encrypted = blockCipher(bytes,Cipher.ENCRYPT_MODE,c);
    		String encryptedTranspherable =  Base64.byteArrayToBase64(encrypted) ;
    		//String encryptedTranspherable = asHex (encrypted); 
    		return  encryptedTranspherable;
    	}
    /////////////////////////////////////////////////////////////////////	
    	private static byte[] blockCipher(byte[] bytes, int mode, Cipher c) throws IllegalBlockSizeException, BadPaddingException{
    		// string initialize 2 buffers.
    		// scrambled will hold intermediate results
    		byte[] scrambled = new byte[0];

    		// toReturn will hold the total result
    		byte[] toReturn = new byte[0];
    		// if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
    		int length = (mode == c.ENCRYPT_MODE)? 100 : 128;

    		// another buffer. this one will hold the bytes that have to be modified in this step
    		byte[] buffer = new byte[length];

    		for (int i=0; i< bytes.length; i++){

    			// if we filled our buffer array we have our block ready for de- or encryption
    			if ((i > 0) && (i % length == 0)){
    				//execute the operation
    				scrambled = c.doFinal(buffer);
    				// add the result to our total result.
    				toReturn = append(toReturn,scrambled);
    				// here we calculate the length of the next buffer required
    				int newlength = length;

    				// if newlength would be longer than remaining bytes in the bytes array we shorten it.
    				if (i + length > bytes.length) {
    					 newlength = bytes.length - i;
    				}
    				// clean the buffer array
    				buffer = new byte[newlength];
    			}
    			// copy byte into our buffer.
    			buffer[i%length] = bytes[i];
    		}

    		// this step is needed if we had a trailing buffer. should only happen when encrypting.
    		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
    		scrambled = c.doFinal(buffer);

    		// final step before we can return the modified data.
    		toReturn = append(toReturn,scrambled);

    		return toReturn;
    	}
    	
    	
    	private static byte[] append(byte[] prefix, byte[] suffix){
    		byte[] toReturn = new byte[prefix.length + suffix.length];
    		for (int i=0; i< prefix.length; i++){
    			toReturn[i] = prefix[i];
    		}
    		for (int i=0; i< suffix.length; i++){
    			toReturn[i+prefix.length] = suffix[i];
    		}
    		return toReturn;
    	}
    	
    	//////////////////////////////////////
  
    	public void SaveKeyPair(KeyPair keyPair) throws IOException {
    		PrivateKey privateKey = keyPair.getPrivate();
    		PublicKey publicKey = keyPair.getPublic();     
    		// Store Public Key.
    		X509EncodedKeySpec x509EncodedKeySpec   = new X509EncodedKeySpec(
    													publicKey.getEncoded());    		
    		publicKeyS = x509EncodedKeySpec.getEncoded().toString();
    		// Store Private Key.
    		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
    													  privateKey.getEncoded());   		
    		privateKeyS = pkcs8EncodedKeySpec.getEncoded().toString();
    		// saving to andriod

    	//NEED TO SAVE
    			//
    			//
    			
    	}
     
    	public KeyPair LoadKeyPair()
    			throws IOException, NoSuchAlgorithmException,
    			InvalidKeySpecException {
    		// Read Public Key.    		
        	//NEED TO LOAD

    		publicKeyS = publicKeyS;
    		privateKeyS = privateKeyS;
			if (publicKeyS == "") return null;
			if (privateKeyS == "") return null;

    		byte[] encodedPublicKey = publicKeyS.getBytes() ;   		
    		// Read Private Key.    	
    		byte[] encodedPrivateKey = privateKeyS.getBytes() ;
    		    
    		// Generate KeyPair.
    		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
    		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
    				encodedPublicKey);
    		PublicKey publicKey12 = keyFactory.generatePublic(publicKeySpec);
     
    		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
    				encodedPrivateKey);
    		PrivateKey privateKey12 = keyFactory.generatePrivate(privateKeySpec);
     
    		return new KeyPair(publicKey12, privateKey12);
    	}
    	
    ////////////////////////  SERVER  ////////////////////////////////////////// 	
   /////////////////////////  SERVER  ///////////////////////////////////////// 	

    	
    	public void SaveServerKey(String publickey_String) throws Exception {
    		
    		try{
	    		ServerPublicKeyS = publickey_String;
	    		// saving to DB !!!!!!!!!!!!

			}
			catch (Exception e) {
				Debug.rsa("SaveServerKey: " + e.toString());
				throw e;
			}
    	}
    	
    	
    	public PublicKey LoadServerKey()
    			throws Exception {
    		// Read Public Key. 
    		try{
	    		ServerPublicKeyS = ServerPublicKeyS;
	    		PublicKey a = loadPublicKeyS(ServerPublicKeyS);
	    		return a;	
    		}catch (Exception e) 
	    	{
				Debug.rsa("LoadServerKey: " + e.toString());
				throw e;
		    }
    	}
    	
   // 	http://stackoverflow.com/questions/9755057/converting-strings-to-encryption-keys-and-vice-versa-java
    	public static PrivateKey loadPrivateKeyS(String key64) throws Exception {
    		try{
	    		byte[] clear = Base64.base64ToByteArray(key64);	
	    	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
	    	    KeyFactory fact = KeyFactory.getInstance(algorithm);
	    	    PrivateKey priv = fact.generatePrivate(keySpec);
	    	    Arrays.fill(clear, (byte) 0);
	    	    return priv;
    		}catch (Exception e) 
	    	{
				Debug.rsa("loadPrivateKey" + e.toString());
				throw e;

		    }
    	}
        	public static PrivateKey loadPrivateKey(byte[] clear) throws Exception {
        		try{
    	    			
    	    	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
    	    	    KeyFactory fact = KeyFactory.getInstance(algorithm);
    	    	    PrivateKey priv = fact.generatePrivate(keySpec);
    	    	   // Arrays.fill(clear, (byte) 0);
    	    	    return priv;
        		}catch (Exception e) 
    	    	{
    				Debug.rsa("loadPrivateKey byte" + e.toString());
    				throw e;

    		    }
        	}
    	public static PublicKey loadPublicKeyS(String key64) throws Exception {
    		try{
	    	    byte[] data = Base64.base64ToByteArray(key64);	
	    	    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
	    	    KeyFactory fact = KeyFactory.getInstance(algorithm);
	    	    return fact.generatePublic(spec);
	    	}catch (Exception e) 
	    	{
				Debug.rsa("loadPublicKey string" + e.toString());
				throw e;

		    }

    	}
    	public static PublicKey loadPublicKey(byte[] data) throws Exception {
    		try{
	    	   // byte[] data = Base64.base64ToByteArray(key64);	
	    	    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
	    	    KeyFactory fact = KeyFactory.getInstance(algorithm);
	    	    return fact.generatePublic(spec);
	    	}catch (Exception e) 
	    	{
				Debug.rsa("loadPublicKey" + e.toString());
				throw e;

		    }

    	}
    	public static String savePrivateKey1(PrivateKey priv) throws Exception{
    		try{
	    	    KeyFactory fact = KeyFactory.getInstance(algorithm);
	    	    PKCS8EncodedKeySpec spec = fact.getKeySpec(priv,
	    	            PKCS8EncodedKeySpec.class);
	    	    byte[] packed = spec.getEncoded();
	    	    String key64 = Base64.byteArrayToBase64(packed) ;
	    	  
	    	    Arrays.fill(packed, (byte) 0);
	    	    return key64;
	    	}catch (Exception e) 
	    	{
				Debug.rsa("savePrivateKey" + e.toString());
				throw e;

		    }
	    }

       	public static String savePublicKey1(PublicKey publ) throws Exception {
    		try{
    			KeyFactory fact = KeyFactory.getInstance(algorithm);
    			X509EncodedKeySpec spec = fact.getKeySpec(publ,
    	            X509EncodedKeySpec.class);
    			   	    
    			return Base64.byteArrayToBase64(spec.getEncoded()) ;
    		}catch (Exception e) {
    				Debug.rsa("savePublicKey" + e.toString());
    				throw e;
			    }
    	}
       	
    	public static byte[] savePublicKeyB(PublicKey publ) throws Exception {
    		try{
    			KeyFactory fact = KeyFactory.getInstance(algorithm);
    			X509EncodedKeySpec spec = fact.getKeySpec(publ,
    	            X509EncodedKeySpec.class);
    			   	    
    			return spec.getEncoded() ;
    		}catch (Exception e) {
    				Debug.rsa("savePublicKey" + e.toString());
    				throw e;
			    }
    	}

    	public static byte[] savePrivateKeyB(PrivateKey priv) throws Exception{
    		try{
	    	    KeyFactory fact = KeyFactory.getInstance(algorithm);
	    	    PKCS8EncodedKeySpec spec = fact.getKeySpec(priv,
	    	            PKCS8EncodedKeySpec.class);
	    	    return  spec.getEncoded();
	    	    
	    	}catch (Exception e) 
	    	{
				Debug.rsa("savePrivateKey" + e.toString());
				throw e;

		    }
	    }


 

}

