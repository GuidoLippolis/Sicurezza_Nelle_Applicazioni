package exception;

public class ForbiddenFileTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ForbiddenFileTypeException(String fileName) {
		super("Il tipo del file " + fileName + " non è consentito");
	}
	
}
