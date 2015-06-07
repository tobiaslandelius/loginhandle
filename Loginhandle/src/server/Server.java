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
				System.out.println("Got challange: " +input);
				try {
					parseChallange(input);
					out.write("true:\n");
				} catch (Exception e) {
					out.write("false:"+e.getMessage()+"\n");
				}
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

	/**
	 * @param input. Message from server
	 * @throws UserAlreadyExistsException if user already exists in database 
	 */
	private void parseChallange(String input) throws Exception {
		String[] split = input.split(":");
		String type = split[0];
		boolean response = false;
		
		switch (type) {
			case "INSERT":
				insertNewUser(split[1], split[2]);
				break;
			case "LOGIN":
				login(split[1], split[2]);
				break;
		}
	}

	private void login(String username, String userpass) throws Exception {
		System.out.println("Try login with "+username+" and password "+userpass);
		dbConnect.login(username, userpass);

	}

	private void insertNewUser(String username, String userpass) throws Exception {
		System.out.println("Inserting user "+username+" with password "+userpass);
		dbConnect.insert(username, userpass);
	}

	private void printConnected() {
		System.out.println("Number of clients: " +numberOfClients);
	}
	
	private void newListener() {
		(new Thread(this)).start();
	} // calls run()
}
