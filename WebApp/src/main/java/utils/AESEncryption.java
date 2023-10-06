package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {
	
    private static SecretKeySpec secretKeySpec;

    public static void setKey(String secret) throws Exception {
    	
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        
        byte[] keyBytes = sha.digest(secret.getBytes(StandardCharsets.UTF_8));

        keyBytes = Arrays.copyOf(keyBytes, 16);

        secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        
    }

    public static String encrypt(String plainText) throws Exception {
    	
        if (secretKeySpec == null) 
            throw new IllegalStateException("Key is not set. Call setKey() first.");

        Cipher cipher = Cipher.getInstance("AES");
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);

        return encryptedString;
        
    }

    public static String decrypt(String encryptedText) throws Exception {
    	
        if (secretKeySpec == null) 
            throw new IllegalStateException("Key is not set. Call setKey() first.");

        Cipher cipher = Cipher.getInstance("AES");
        
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        String decryptedString = new String(decryptedBytes, StandardCharsets.UTF_8);

        return decryptedString;
    }

}
