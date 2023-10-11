package utils;

import org.apache.commons.lang3.RandomStringUtils;

public class Utils {
	
	public static String generateRandomToken(String input, int tokenLength) {
		
		return input + "#" + RandomStringUtils.randomAlphanumeric(tokenLength);
		
	}
    
    public static String getUsernameFromCookie(String cookieValue) {
    	
    	return cookieValue.split("#")[0];
    	
    }
    
    public static boolean isCookieExpired(long currentTime, long expirationTime) {
    	
    	return expirationTime < currentTime;
    	
    }

}
