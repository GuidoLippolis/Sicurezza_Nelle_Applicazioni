package exception;

public class UserAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException(String username) {
		super("L'utente con lo username " + username + " esiste già");
	}
	
}
