package exception;

public class LoginFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoginFailedException() {
		super("Errore nel login: username e/o password errati");
	}
	
}
