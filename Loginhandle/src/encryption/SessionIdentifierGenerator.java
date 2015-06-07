package encryption;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SessionIdentifierGenerator {

	private SecureRandom random = new SecureRandom();
	
	public String newCodeIdentifier() {
		return new BigInteger(130, random).toString(32);
	}
}
