package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import constants.Constants;

/*
 * Questa classe ha lo scopo di inizializzare l'oggetto prop
 * all'avvio dell'applicazione. In questo modo, all'interno
 * delle altre classi sarà possibile accedere alle properties
 * del file application.properties senza dover leggere ogni
 * volta il file. Il percorso al file è stato impostato come
 * variabile d'ambiente, in modo tale da non avere alcun
 * riferimento a esso nel codice sorgente
 * 
 * */

public class ApplicationPropertiesLoader {
	
    private static Properties prop = new Properties();

    static {
    	
        try {
        	
            FileInputStream in = new FileInputStream(System.getenv(Constants.PATH_TO_PROPERTIES_FILE));
            
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