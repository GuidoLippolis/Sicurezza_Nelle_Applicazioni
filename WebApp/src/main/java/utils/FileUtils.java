package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.Part;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class FileUtils {
	
	public static void printMetadata(String fileName) throws IOException, SAXException, TikaException {
		
		BodyContentHandler bodyContentHandler = new BodyContentHandler();
		
		Metadata metadata = new Metadata();
		
		FileInputStream content = new FileInputStream(fileName);

		Parser parser = new AutoDetectParser();
		
		parser.parse(content, bodyContentHandler, metadata, new ParseContext());
		
		for(String name : metadata.names())
			System.out.println(name + "\t" + metadata.get(name));
		
	}
	
	public static byte[] getFileContent(Part filePart) {
		
        byte[] buffer = new byte[1024];
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        
        try (InputStream fileContent = filePart.getInputStream()) {
        	
            int bytesRead;
            
            while ((bytesRead = fileContent.read(buffer)) != -1) 
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            
            return byteArrayOutputStream.toByteArray();
            
        } catch (IOException e) {
        	
			e.printStackTrace();
			return new byte[0];
			
		}
		
	}

}
