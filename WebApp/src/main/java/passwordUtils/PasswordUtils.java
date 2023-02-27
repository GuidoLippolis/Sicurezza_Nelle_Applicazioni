package passwordUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

	public static byte[] generateHash(String data, String algorithm, byte[] salt) throws NoSuchAlgorithmException {
		
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		digest.reset();
		digest.update(salt);
		byte[] hash = digest.digest(data.getBytes());
		return hash;
		
	}
	
	public static byte[] createSalt(int length) {
		
		byte[] bytes = new byte[length];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bytes);
		return bytes;
		
	}
	
}
