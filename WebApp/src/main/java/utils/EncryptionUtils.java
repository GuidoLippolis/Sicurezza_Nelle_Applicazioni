package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {
	
    private static SecretKeySpec secretKeySpec;

    private static void setKey(String secret) throws Exception {
        
    	MessageDigest sha = MessageDigest.getInstance("SHA-256");
        
    	byte[] keyBytes = sha.digest(secret.getBytes(StandardCharsets.UTF_8));

        keyBytes = Arrays.copyOf(keyBytes, 16);

        secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        
    }

    
    public static String encrypt(String plaintext, String passphrase) throws Exception {
    	
        setKey(passphrase);
        
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        
        return Base64.getEncoder().encodeToString(encryptedBytes);
        
    }

}
