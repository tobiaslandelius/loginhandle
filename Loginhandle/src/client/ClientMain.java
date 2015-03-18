package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import encryption.SHA256;

public class ClientMain {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Username: ");
		String username = br.readLine();
		System.out.println("Password: ");
		String password = SHA256.encrypt(br.readLine());
		
		Client c = new Client("localhost", 6660, "INSERT:"+username+":"+password);
	}

}
