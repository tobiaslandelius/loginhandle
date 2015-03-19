package exceptions;

public class WrongPasswordException extends Exception {

	public String getMessage() {
		return "Wrong password!";
	}
}
