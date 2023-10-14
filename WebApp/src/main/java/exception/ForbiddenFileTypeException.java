package exception;

public class ForbiddenFileTypeException extends Exception {

	public ForbiddenFileTypeException(String fileName) {
		super("Il tipo del file " + fileName + " non è consentito");
	}
	
}
