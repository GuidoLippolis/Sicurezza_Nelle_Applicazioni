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
    
    public static boolean isUsernameValid(String username) {

        return username.matches("^[a-zA-Z0-9]*$");
        
    }
    
    public static int calculateMinutesFromNow(int minutes) {

    	Date now = new Date();
    	
    	Date expirationTime = new Date(now.getTime() + minutes * 60 * 1000);
    	
    	return (int) (expirationTime.getTime() - now.getTime()) / 1000;
    	
    }
    
    public static boolean isCookieExpired(int seconds) {
    	
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + seconds * 1000);

        return now.after(expirationTime);
        
    }
    
}
