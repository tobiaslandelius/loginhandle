package exceptions;

public class UserAlreadyExistsException extends Exception {

	public UserAlreadyExistsException() {
		
	}
	
	public String getMessage() {
		return "User with this username already exists in database";
	}
}
