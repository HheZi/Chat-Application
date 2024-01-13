package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
				new Thread(clientHandler).start();
						
			}
		} catch (IOException e) {
			stopServer();
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

	private class ClientHandler implements Runnable{
		// The list of all connections of users
		public static ArrayList<ClientHandler> clients = new ArrayList<>();
		
		private Socket socket;
		private BufferedReader bufferedReader;
		private BufferedWriter bufferedWriter;
		private String username;
		
		public ClientHandler(Socket socket){
			try {
				this.socket = socket;
				this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.username = bufferedReader.readLine();
				clients.add(this);
				brodcastMessage(username + " has joined the chat");
			} catch (IOException e) {
				e.printStackTrace();
				closeAll();
			}
		}
		
		// This method is responsible for writing a messages 
		// in client and sending them to brodcastMessage method
		@Override
		public void run() {
			while(socket.isConnected()) {
				try {
					String messageFromClient = bufferedReader.readLine();
					brodcastMessage(messageFromClient);
				} catch (IOException e) {
					closeAll();
					break;
				}
			}
			
		}
		
		// Closing all and removing this client from the list of connections
		public void closeAll() {
			try {
				clients.remove(this);
				socket.close();
				bufferedReader.close();
				bufferedWriter.close();
				brodcastMessage(username + " has left the chat");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// This method responsible for transmit a messages
		public void brodcastMessage(String message) {
			for (ClientHandler clientHandler : clients) {
				// Transmitting message to all users except this object
				if(!clientHandler.username.equals(this.username)) {
					try {
						clientHandler.bufferedWriter.write(message);
						clientHandler.bufferedWriter.newLine();
						clientHandler.bufferedWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		new Server(1007).start();
	}
	
}
