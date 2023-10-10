package utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class Utils {
	
	public static String generateRandomToken(String input, int tokenLength) {
		
		return input + "#" + RandomStringUtils.randomAlphanumeric(tokenLength);
		
	}
    
    public static String getUsernameFromCookie(String cookieValue) {
    	
    	return cookieValue.split("#")[0];
    	
    }

}
