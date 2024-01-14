package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	// contains port, we using port 1007
	private final int port;

	private ServerSocket serverSocket;
	
	public Server(int port) {
		this.port = port;
	}
	
	//using this method just to close ServerSocket
	public void waitingForQuit() {
		new Thread() {
			@Override
			public void run() {
				try(Scanner sc = new Scanner(System.in)){
					System.out.println("If you want to quit print \"/quit\"");
					String str = "";
					while(!str.equals("/quit")) {
						str = sc.next();
					}
			
				}
				stopServer();
				System.exit(0);
			}
		}.start();
	}
	
	
	// start the server and waiting for connections
	public void start() {
		try {
			serverSocket = new ServerSocket(port);
			waitingForQuit();
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("New client has connected");
				
				// for every connection we will create a separate thread
				ClientHandler clientHandler = new ClientHandler(socket);
				new Thread(clientHandler).start();
						
			}
		} catch (IOException e) {
			stopServer();
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// just closes the ServerSocket
	public void stopServer() {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static void main(String[] args) {
		new Server(1007).start();
	}
	
}
