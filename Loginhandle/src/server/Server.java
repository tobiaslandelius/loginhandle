package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;


public class Server implements Runnable {

	private ServerSocket serverSocket = null;

	private DBConnect dbConnect = new DBConnect();
	private int numberOfClients;

	public Server(int port) {
		try {
			ServerSocket ss = new ServerSocket(port);
			new Server(ss);
			System.out.println("\nServer Started\n");
		} catch (IOException e) {
			System.out.println("Unable to start Server: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public Server(ServerSocket ss) throws IOException {
		serverSocket = ss;
		newListener();
	}

	public void run() {
		try {
			Socket clientSocket = serverSocket.accept();

			numberOfClients++;
			newListener();

			System.out.println("New client connected");
			printConnected();
			
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			String input = null;
			while ((input = in.readLine()) != null) {
				System.out.println("Got challange: "+input);
				parseChallange(input);
			}
			in.close();
			out.close();
			clientSocket.close();
			numberOfClients--;
			System.out.println("Client quit session");
		} catch (SocketException e) {
			System.out.println("Client died..");
			numberOfClients--;
			printConnected();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseChallange(String input) {
		String[] split = input.split(":");
		String type = split[0];
		
		switch (type) {
			case "INSERT":
				insertNewUser(split[1], split[2]);
		}
	}

	private void insertNewUser(String username, String userpass) {
		System.out.println("Inserting user "+username+" with password "+userpass);
		dbConnect.insert(username, userpass);
	}

	private void tryLogin(String input) {
		String[] s = input.split(":");
		System.out.println("Got message: "+input);
		dbConnect.tryLogin(s[0], s[1]);
	}

	private void printConnected() {
		System.out.println("Number of clients: " +numberOfClients);
	}
	private void newListener() {
		(new Thread(this)).start();
	} // calls run()
}
