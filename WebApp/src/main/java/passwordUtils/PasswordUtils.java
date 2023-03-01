package passwordUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

	public static byte[] generateHash(byte[] password, String algorithm) throws NoSuchAlgorithmException {
		
		/*
		 * Generazione del valore di salt da concatenare alla password
		 * 
		 * TODO: Recuperare il parametro del metodo createSalt() da un file .properties
		 * 
		 * */
		
		byte[] salt = createSalt(10);
		
		/*
		 * Concatenazione dell'array di byte corrispondente alla password con
		 * quello corrispondente al salt
		 * 
		 * */
		
		byte[] passwordAndSalt = appendByteArrays(password, salt);
		
		/*
		 * Applicazione dell'algoritmo di hashing scelto
		 * 
		 * */
		
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		
		byte[] hashValue = messageDigest.digest(passwordAndSalt);
		
		/*
		 * Pulizia dei dati sensibili
		 * 
		 * */
		
		clearArray(password);
		clearArray(passwordAndSalt);
		clearArray(salt);

		return hashValue;
		
	}
	
	private static byte[] appendByteArrays(byte[] a, byte[] b) {
		
	    byte[] result = new byte[a.length + b.length];
	    
	    System.arraycopy(a, 0, result, 0, a.length);
	    System.arraycopy(b, 0, result, a.length, b.length);
	    
	    return result;
	    
	}
	
	private static byte[] createSalt(int length) {
		
		byte[] bytes = new byte[length];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bytes);
		return bytes;
		
	}
	
	private static void clearArray(byte[] array) {
		
		for(int i=0; i<array.length; i++)
			array[i] = 0;
		
	}
	
}
