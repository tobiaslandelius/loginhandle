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

	public Client(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		connect();
	}

	private void connect() {
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

	public void challange(String serverMessage) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		out.write(serverMessage + "\n");
		out.flush();

		System.out.println("Message sent: "+serverMessage);

		System.out.println("Waiting for response..");
		String response = in.readLine();
		System.out.println("Response: " +response);
		in.close();
		out.close();
		clientSocket.close();
	}
}
