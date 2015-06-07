package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import encryption.SHA256;

public class ClientMain {

	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static String host;
	private static int port;

	public static void main(String[] args) throws IOException {
		boolean run = true;
		serverOptions();
		while (run) {
			int option = getOption(br);
			switch (option) {
			case 1:
				addUser();
				break;
			case 2:
				login();
				break;
			case 3:
				System.exit(1);
				break;
			}
		}
	}

	private static void login() throws IOException {
		System.out.println("Username: ");
		String username = br.readLine();
		System.out.println("Password: ");
		String password = SHA256.encrypt(br.readLine());
		
		String message = "LOGIN:" +username+ ":" +password;
		Client c = connect();
		c.challange(message);
	}

	private static void addUser() throws IOException {
		System.out.println("Username: ");
		String username = br.readLine();
		System.out.println("Password: ");
		String password = SHA256.encrypt(br.readLine());
		
		String message = "INSERT:" +username+ ":" + password;
		Client c = connect();
		c.challange(message);
	}

	private static Client connect() {
		return new Client(host, port);
	}
	

	private static void serverOptions() {
		host = "192.168.0.101";
		port = 6660;
	}

	private static int getOption(BufferedReader br) throws IOException {
		System.out.println("Välj alternativ: ");
		System.out.println("   1. Lägg till användare");
		System.out.println("   2. Logga in");
		System.out.println("   3. Avsluta");
		return Integer.parseInt(br.readLine());
	}

}
