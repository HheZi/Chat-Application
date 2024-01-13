package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	// contains port, we using port 1007
	private final int port;

	private ServerSocket serverSocket;
	
	public Server(int port) {
		this.port = port;
	}
	
	// start the server and waiting for connections
	public void start() {
		try {
			serverSocket = new ServerSocket(port);
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("New client has connected");
				
				// for every connection we will create a separate thread
				ClientHandler clientHandler = new ClientHandler(socket);
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// just closes the ServerSocket
	public void stopServer() {
		if(serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server(1007);
		server.start();
	}
}
