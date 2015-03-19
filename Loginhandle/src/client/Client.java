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
	private String errorMessage;
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
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean challange(String serverMessage) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		out.write(serverMessage + "\n");
		out.flush();
		String response = in.readLine();
		
		in.close();
		out.close();
		clientSocket.close();
		
		return handleRespons(response);
	}

	private boolean handleRespons(String response) {
		String[] splitResponse = response.split(":");
		if (splitResponse[0].equals("true")) {
			return true;
		}
		errorMessage = splitResponse[1];
		return false;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
