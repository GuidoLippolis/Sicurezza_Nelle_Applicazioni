package utils;

import org.apache.commons.lang3.RandomStringUtils;

public class Utils {
	
	private static int tokenLength = 20;
	
	public static String generateRandomToken(String input) {
		
		return input + "#" + RandomStringUtils.randomAlphanumeric(tokenLength);
		
	}

}
