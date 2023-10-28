package exception;

public class UsernameNotValidException extends Exception {

	private static final long serialVersionUID = 1L;

	public UsernameNotValidException(String username) {
		super("Lo username " + username + " non è valido");
	}
	
}
