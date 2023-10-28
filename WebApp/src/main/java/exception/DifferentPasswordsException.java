package exception;

public class DifferentPasswordsException extends Exception {

	private static final long serialVersionUID = 1L;

	public DifferentPasswordsException() {
		super("Le password non corrispondono");
	}
	
}
