package utils;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

public class Utils {
	
	public static String generateRandomToken(int tokenLength) {
		
		return RandomStringUtils.randomAlphanumeric(tokenLength);
		
	}
    
    public static String getUsernameFromCookie(String cookieValue) {
    	
    	return cookieValue.split("#")[0];
    	
    }
    
    public static boolean isCookieValid(long currentTime, long expirationTime) {
    	
    	return expirationTime < currentTime;
    	
    }
    
    /*
     * Esempi:
     * 
     * - test.txt -> test_1697972596384.txt
     * - test.prova.txt -> test.prova_1697972655522.txt
     * 
     * */
    
    public static String transformFileName(String fileName) {
        
    	return fileName.substring(0, fileName.lastIndexOf(".")) + "_" + new Date().getTime() + "." + fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    	
    }
    
    public static int getUserIdFromCookieValue(String cookieValue) {
    	
    	return Integer.parseInt(cookieValue.split("@@@")[1]);
    	
    }
    
}
