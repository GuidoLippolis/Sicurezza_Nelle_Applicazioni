package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import enumeration.PropertiesKeys;

public class ApplicationPropertiesLoader {
	
    private static Properties prop = new Properties();

    static {
    	
        try {
        	
            FileInputStream in = new FileInputStream(System.getenv(PropertiesKeys.PATH_TO_PROPERTIES_FILE.toString()));
            
            prop.load(in);
            
            in.close();
            
        } catch (IOException e) {
        	
            e.printStackTrace();
            
        }
    }

    public static Properties getProperties() {
        return prop;
    }
    
}