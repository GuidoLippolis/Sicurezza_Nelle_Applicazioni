package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

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
	
	public static void clearArray(byte[] array) {
		
		Arrays.fill(array, (byte)0);
		
	}
	
	/*
	 * Il metodo createSalt() permette di generare un valore casuale
	 * di salt, dato un numero intero che rappresenta la sua lunghezza (in byte)
	 * 
	 * */
	
	public static byte[] createSalt(int length) {
		
		byte[] bytes = new byte[length];
		
		SecureRandom random = new SecureRandom();
		
		random.nextBytes(bytes);
		
		return bytes;
		
	}
	
}
