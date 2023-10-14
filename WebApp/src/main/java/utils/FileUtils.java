package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Part;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class FileUtils {
	
	public static boolean isFileTypeForbidden(String fileName) throws IOException, SAXException, TikaException {
		
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
	
//	public static byte[] getFileContent(Part filePart) {
//		
//        byte[] buffer = new byte[1024];
//        
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        
//        try (InputStream fileContent = filePart.getInputStream()) {
//        	
//            int bytesRead;
//            
//            while ((bytesRead = fileContent.read(buffer)) != -1) 
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//            
//            return byteArrayOutputStream.toByteArray();
//            
//        } catch (IOException e) {
//        	
//			e.printStackTrace();
//			return new byte[0];
//			
//		}
//		
//	}

}
