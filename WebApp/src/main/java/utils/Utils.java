package utils;

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
    
    public static int extractUserId(String input) {
    	
        String[] parts = input.split("@@@");
        
        String lastPart = parts[parts.length - 1];
        
        String[] useridPart = lastPart.split("@@@");
        
        String userid = useridPart[0];
        
        userid = userid.replace("@", "");
        
        return Integer.parseInt(userid);
    }

}
