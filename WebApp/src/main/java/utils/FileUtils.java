package utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import enumeration.PropertiesKeys;
import servlets.SignInServlet;

public class FileUtils {
	
	private static final Logger log = Logger.getLogger(SignInServlet.class);
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();
	
	public static boolean isFakeTxt(String fileName) throws IOException, SAXException, TikaException {
		
		BodyContentHandler bodyContentHandler = new BodyContentHandler();
		
		Metadata metadata = new Metadata();
		
		FileInputStream content = new FileInputStream(fileName);

		Parser parser = new AutoDetectParser();
		
		parser.parse(content, bodyContentHandler, metadata, new ParseContext());

		String fileType = null;
		
		for(String name : metadata.names()) {
			
			if(name.equals("Content-Type")) {
				
				fileType = metadata.get(name);
				break;
				
			}
			
		}
	
		return !fileType.equals("text/plain");
	
	}	
		
		
	public static boolean isImage(String fileName) throws IOException, SAXException, TikaException {
		
		BodyContentHandler bodyContentHandler = new BodyContentHandler();
		
		Metadata metadata = new Metadata();
		
		FileInputStream content = new FileInputStream(fileName);

		Parser parser = new AutoDetectParser();
		
		parser.parse(content, bodyContentHandler, metadata, new ParseContext());

		String fileType = null;
		
		for(String name : metadata.names()) {
			
			System.out.println(name + "\t" + metadata.get(name));
			
			if(name.equals("Content-Type")) {
				
				fileType = metadata.get(name);
				break;
				
			}
			
		}
	
		return fileType.equals("image/png") || fileType.equals("image/jpeg") || fileType.equals("image/jpg");
	
	}
	
	public static boolean isFileTooLarge(String fileName) throws IOException, SAXException, TikaException {
		
		long MAX_FILE_SIZE_MB = Long.parseLong(prop.getProperty(PropertiesKeys.MAX_FILE_SIZE_MB.toString()));
		
		BodyContentHandler bodyContentHandler = new BodyContentHandler();
		
		Metadata metadata = new Metadata();
		
		FileInputStream content = new FileInputStream(fileName);

		Parser parser = new AutoDetectParser();
		
		parser.parse(content, bodyContentHandler, metadata, new ParseContext());

		return content.getChannel().size() > MAX_FILE_SIZE_MB * 1024 * 1024;
		
	}

	public static byte[] getFileContent(File file) {
		
	    byte[] buffer = new byte[1024];
	    
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	    try (InputStream fileContent = new FileInputStream(file)) {
	    	
	        int bytesRead;

	        while ((bytesRead = fileContent.read(buffer)) != -1)
	            byteArrayOutputStream.write(buffer, 0, bytesRead);

	        return byteArrayOutputStream.toByteArray();
	        
	    } catch (IOException e) {
	    	
	        log.error("Eccezione in FileUtils: " + e.getMessage());
	        return new byte[0];
	        
	    }
	    
	}
	
	public static byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
		
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        byte[] buffer = new byte[1024];

        int bytesRead;
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        while ((bytesRead = bufferedInputStream.read(buffer)) != -1)
            byteArrayOutputStream.write(buffer, 0, bytesRead);

        byte[] data = byteArrayOutputStream.toByteArray();

        bufferedInputStream.close();
        
        return data;
		
	}
	
    public static String getFileName(Part part) {
    	
        String contentDisposition = part.getHeader("content-disposition");
        
        String[] tokens = contentDisposition.split(";");
        
        for (String token : tokens) {
        	
            if (token.trim().startsWith("filename")) 
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            
        }
        
        return "";
    }
	
}
