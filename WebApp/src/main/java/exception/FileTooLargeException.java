package exception;

public class FileTooLargeException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileTooLargeException(String fileName, long maxFileSize) {
		super("Il file " + fileName + " supera la dimensione massima consentita (" + maxFileSize + " MB)");
	}
	
}
