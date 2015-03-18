package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private String hostname;
	private int port;
	Socket clientSocket;

	public Client(String hostname, int port, String serverMessage) {
		this.hostname = hostname;
		this.port = port;
		connect();
		try {
			challange(serverMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		System.out.println("Attempting to connect to " + hostname + ":" + port);
		try {
			clientSocket = new Socket(hostname, port);
			System.out.println("Connection Established");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addNewUser(String username, String userpass) {
		
	}
	public void challange(String serverMessage) throws IOException {
		String userInput;

		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		out.write(serverMessage+"\n");
		System.out.println("Message sent!");
		out.flush();
		
		boolean go = true;
		String message;
		while (go) {
//			message = reader.readLine();
//			if (message.equals("quit"))
//				break;
//			
//			out.write(message + "\n");
//			out.flush();
//			System.out.println("Message sent!");
			System.out.println("Waiting for response..");
			String response = in.readLine();
		}
		in.close();
		out.close();
		reader.close();
		clientSocket.close();

	}
//
//	public static void main(String[] args) {
//		int port = 6000;
//		String host = "localhost";
//
//		Client client = new Client("localhost", port);
//
//		try {
//			client.connect();
//			client.challange();
//
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Done!");
//	}

}
