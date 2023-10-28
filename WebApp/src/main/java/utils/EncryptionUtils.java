package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import enumeration.PropertiesKeys;

public class EncryptionUtils {
	
    private static SecretKeySpec secretKeySpec;
    
    private static Properties prop = ApplicationPropertiesLoader.getProperties();

    public EncryptionUtils(String passphrase) throws Exception {
    	
        setKey(passphrase);
        
    }
    
    private static void setKey(String secret) throws Exception {
    	
    	byte[] keyBytes;
    	
        if (secretKeySpec == null) {
        	
            MessageDigest sha = MessageDigest.getInstance(prop.getProperty(PropertiesKeys.HASHING_ALGORITHM.toString()));
            
            keyBytes = sha.digest(secret.getBytes(StandardCharsets.UTF_8));
            
            keyBytes = Arrays.copyOf(keyBytes, 16);
            
            secretKeySpec = new SecretKeySpec(keyBytes, prop.getProperty(PropertiesKeys.SYMMETRIC_ENCRYPTION_ALGORITM.toString()));
            
        }
        
    }

    
    public String encrypt(String plaintext) throws Exception {
    	
        Cipher cipher = Cipher.getInstance(prop.getProperty(PropertiesKeys.CRYPTOGRAPHY_MODE.toString()));
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        
        return Base64.getEncoder().encodeToString(encryptedBytes);
        
    }

}
