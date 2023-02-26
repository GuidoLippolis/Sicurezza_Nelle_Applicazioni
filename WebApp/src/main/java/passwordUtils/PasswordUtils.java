package passwordUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

//	public static String generateHash(String data, String algorithm, byte[] salt) throws NoSuchAlgorithmException {
	public static byte[] generateHash(String data, String algorithm, byte[] salt) throws NoSuchAlgorithmException {
		
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		digest.reset();
		digest.update(salt);
		byte[] hash = digest.digest(data.getBytes());
		return hash;
//		return bytesToStringHex(hash);
		
	}
	
//	private static String bytesToStringHex(byte[] bytes) {
//		
//		return new BigInteger(1, bytes).toString(16);
//		
//	}
	
	public static byte[] createSalt(int length) {
		
		byte[] bytes = new byte[length];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bytes);
		return bytes;
		
	}
	
}
