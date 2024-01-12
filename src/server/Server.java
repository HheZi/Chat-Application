package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.ClientHandler;

public class Server {
	private int port;
	
	
	private ServerSocket serverSocket;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void start() {
		try {
			serverSocket = new ServerSocket(port);
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("New client has connected");
				ClientHandler clientHandler = new ClientHandler(socket);
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
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
