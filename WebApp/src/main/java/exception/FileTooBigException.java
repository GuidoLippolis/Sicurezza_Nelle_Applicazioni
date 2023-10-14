package exception;

public class FileTooBigException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileTooBigException(String fileName) {
		super("Il file " + fileName + " supera la dimensione massima consentita");
	}
	
}
