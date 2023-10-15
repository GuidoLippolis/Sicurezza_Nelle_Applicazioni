package utils;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

public class Utils {
	
	public static String generateRandomToken(String input, int tokenLength) {
		
		return input + "#" + RandomStringUtils.randomAlphanumeric(tokenLength);
		
	}
    
    public static String getUsernameFromCookie(String cookieValue) {
    	
    	return cookieValue.split("#")[0];
    	
    }
    
    public static boolean isCookieValid(long currentTime, long expirationTime) {
    	
    	return expirationTime < currentTime;
    	
    }
    
    public static String transformFileName(String fileName) {
        
    	return fileName.substring(0, fileName.lastIndexOf(".")) + "_" + new Date().getTime() + "." + fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    	
    }
    
}
