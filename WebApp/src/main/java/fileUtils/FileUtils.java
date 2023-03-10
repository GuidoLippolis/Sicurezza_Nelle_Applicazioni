package fileUtils;

import java.io.FileInputStream;
import java.io.IOException;

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

}
