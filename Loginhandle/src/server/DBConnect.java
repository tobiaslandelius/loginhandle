package server;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class DBConnect {

	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	private static final int PBKDF2_ITERATIONS = 1000;
	private static final int HASH_BYTE_SIZE = 32;
	private static final int SALT_BYTE_SIZE = 32;

	private Connection con;
	private Statement st;
	private ResultSet rs;

	public DBConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/login_service", "root",
					"password");
			st = con.createStatement();
			System.out.println("Connected!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean insert(String username, String userpass) {
		final String[] newHashes = getHashedPass(userpass.toCharArray(),
				null);
		try {
			PreparedStatement posted = con
					.prepareStatement("INSERT INTO userinfo (username, userpass, usersalt) VALUES ('"
							+ username
							+ "', '"
							+ newHashes[0]
							+ "', '"
							+ newHashes[1] + "')");
			posted.executeUpdate();
		} catch (MySQLIntegrityConstraintViolationException e) {
			return false;  // Username already exists;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Inserted!");
		return true;
		
	}

	public void tryLogin(String username, String userpass) {
		final String[] hashResponse = getHashedPass(userpass.toCharArray(),
				null);
		// =(UF=QE(H)) // h�mta salt from db sen g� viidare em hash � j�mf�ra...
	}

	public String[] getHashedPass(char[] userpass, String usersalt) {
		byte[] salt;
		if (usersalt == null) { // Check if there is salt to use or if new
								// should be created
			Random r = new SecureRandom();
			salt = new byte[SALT_BYTE_SIZE];
			r.nextBytes(salt);
		} else {
			salt = fromHex(usersalt);
		}

		byte[] hash = null;
		hash = pbkdf2(userpass, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);

		String[] response = new String[2];
		response[0] = toHex(hash);
		response[1] = toHex(salt);

		return response;

	}

	private byte[] pbkdf2(char[] userpass, byte[] salt, int pbkdf2Iterations,
			int hashByteSize) {

		PBEKeySpec spec = new PBEKeySpec(userpass, salt, pbkdf2Iterations,
				hashByteSize * 8);
		SecretKeyFactory skf;
		byte[] hash = null;
		try {
			skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
			hash = skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return hash;
	}

	/**
	 * Converts a byte array into a hexadecimal string.
	 * 
	 * @param array
	 *            the byte array to convert
	 * @return a length*2 character string encoding the byte array
	 */
	private static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0)
			return String.format("%0" + paddingLength + "d", 0) + hex;
		else
			return hex;
	}

	/**
	 * Converts a string of hexadecimal characters into a byte array.
	 *
	 * @param hex
	 *            the hex string
	 * @return the hex string decoded into a byte array
	 */
	private static byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(
					hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}
}
