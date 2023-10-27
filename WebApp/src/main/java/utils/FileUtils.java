package utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class FileUtils {
	
	public static boolean isFakeTxt(String fileName) throws IOException, SAXException, TikaException {
		
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

	public static byte[] getFileContent(File file) {
		
	    byte[] buffer = new byte[1024];
	    
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	    try (InputStream fileContent = new FileInputStream(file)) {
	    	
	        int bytesRead;

	        while ((bytesRead = fileContent.read(buffer)) != -1)
	            byteArrayOutputStream.write(buffer, 0, bytesRead);

	        return byteArrayOutputStream.toByteArray();
	        
	    } catch (IOException e) {
	    	
	        e.printStackTrace();
	        return new byte[0];
	        
	    }
	}
	
	public static byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
		
        // Create a BufferedInputStream for efficient reading.
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        // Create a byte array to store the read data.
        byte[] buffer = new byte[1024]; // You can adjust the buffer size as needed.

        int bytesRead;
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
            // Process the bytes read or write them to another stream, file, etc.
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        // Get the byte array containing the data read from the input stream.
        byte[] data = byteArrayOutputStream.toByteArray();

        // Close the input stream when you're done.
        bufferedInputStream.close();
        
        return data;
		
	}
	
}
