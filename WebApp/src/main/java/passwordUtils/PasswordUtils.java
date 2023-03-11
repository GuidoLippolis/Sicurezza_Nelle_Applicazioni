package passwordUtils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

	/*
	 * Il metodo generateHash(), dati:
	 * 
	 * 1. Password scelta dall'utente
	 * 2. Valore di salt generato casualmente dal metodo createSalt()
	 * 3. Funzione crittografica di hashing (es. SHA-256)
	 * 
	 * Genera il valore hash della password
	 * 
	 * */
	
	public static byte[] generateHash(byte[] password, byte[] salt, String algorithm) throws NoSuchAlgorithmException {
		
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

		return hashValue;
		
	}
	
	/*
	 * Il metodo appendByteArrays(), dati due array di byte,
	 * in questo caso la password e il salt, permette di
	 * concatenarli
	 * 
	 * */
	
	private static byte[] appendByteArrays(byte[] a, byte[] b) {
		
	    byte[] result = new byte[a.length + b.length];
	    
	    System.arraycopy(a, 0, result, 0, a.length);
	    System.arraycopy(b, 0, result, a.length, b.length);
	    
	    return result;
	    
	}

	/*
	 * Il metodo clearArray(), dato un array di byte, permette
	 * di cancellare ogni singola cella dell'array. In questo caso,
	 * la password viene gestita come array di byte, così facendo
	 * è possibile cancellare dalla memoria ogni singolo riferimento
	 * ai dati sensibili
	 * 
	 * */
	
	private static void clearArray(byte[] array) {
		
		for(int i=0; i<array.length; i++)
			array[i] = 0;
		
	}
	
	/*
	 * Il metodo createSalt() permette di generare un valore casuale
	 * di salt, dato un numero intero che rappresenta la sua lunghezza (in byte)
	 * 
	 * */
	
//	public static byte[] createSalt(int length) {
//		
//		byte[] bytes = new byte[length];
//		SecureRandom random = new SecureRandom();
//		random.nextBytes(bytes);
//		return bytes;
//		
//	}
	
    public static byte[] createSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] byteArray = new byte[length];
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        while (buffer.hasRemaining()) {
            if (buffer.remaining() >= 8) {
                buffer.putLong(random.nextLong());
            } else {
                buffer.put((byte) random.nextInt(256));
            }
        }
        return byteArray;
    }
	
	public static String bytesToHex(byte[] bytes) {
	    StringBuilder hexString = new StringBuilder();
	    for (byte b : bytes) {
	        String hex = Integer.toHexString(0xFF & b);
	        if (hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}

	
}
