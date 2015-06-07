package exceptions;

public class NoSuchUserException extends Exception {

	public String getMessage() {
		return "User with this username doesnt exists in database";
	}
}
