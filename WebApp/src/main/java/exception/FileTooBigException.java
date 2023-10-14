package exception;

public class FileTooBigException extends Exception {

	public FileTooBigException(String fileName) {
		super("Il file " + fileName + " supera la dimensione massima consentita");
	}
	
}
