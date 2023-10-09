package utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class Utils {
	
	public static String generateRandomToken(String input, int tokenLength) {
		
		return input + "#" + RandomStringUtils.randomAlphanumeric(tokenLength);
		
	}
	
    public static int getUserIdFromCookie(String cookieValue) {
    	
        String[] parts = cookieValue.split("@@@");
        
        String lastPart = parts[parts.length - 1];
        
        String[] useridPart = lastPart.split("@@@");
        
        String userid = useridPart[0];
        
        userid = userid.replace("@", "");
        
        return Integer.parseInt(userid);
        
    }
    
    public static String getUsernameFromCookie(String cookieValue) {
    	
    	return cookieValue.split("#")[0];
    	
    }

}
