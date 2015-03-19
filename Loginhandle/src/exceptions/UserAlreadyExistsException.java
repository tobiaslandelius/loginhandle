package exceptions;

public class UserAlreadyExistsException extends Exception {

	public String getMessage() {
		return "User with this username already exists in database";
	}
}
