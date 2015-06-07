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

import util.DatabaseReturnMessage;
import exceptions.NoSuchUserException;
import exceptions.UserAlreadyExistsException;

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
				System.out.println("Got challange: " + input);
				String returnMessage = new ReturnMessageFactory().buildMessage(parseChallange(input));
				System.out.println(returnMessage);
				out.write(returnMessage);
				out.flush();
			}
			in.close();
			out.close();
			clientSocket.close();
			numberOfClients--;
			System.out.println("Client quit session");
		} catch (SocketException e) {
			System.out.println("Client died..");
			numberOfClients--;
		} catch (IOException e) {
			e.printStackTrace();
		}
		printConnected();
	}

	private DatabaseReturnMessage parseChallange(String input) {
		String[] split = input.split(":");
		String type = split[0];
		DatabaseReturnMessage drm = new DatabaseReturnMessage();
		
		switch (type) {
		case "INSERT":
			drm = insertNewUser(split[1], split[2]);
			break;
		case "LOGIN":
			drm = login(split[1], split[2]);
			break;
		case "CHECK_FOR_USERNAME":
			drm = checkForUser(split[1]);
			break;
		}
		return drm;
	}

	private DatabaseReturnMessage insertNewUser(String username, String userpass) {
		System.out.println("Inserting user " + username + " with password "
				+ userpass);
		return dbConnect.insert(username, userpass);
	}
	
	private DatabaseReturnMessage login(String username, String userpass) {
		System.out.println("Try login with " + username + " and password "
				+ userpass);
		return dbConnect.login(username, userpass);
	}
	
	private DatabaseReturnMessage checkForUser(String username) {
		System.out.println("Checking for user: " +username);
		return dbConnect.checkUser(username);
	}

	private void printConnected() {
		System.out.println("Number of clients: " + numberOfClients);
	}

	private void newListener() {
		(new Thread(this)).start();
	} // calls run()
}
