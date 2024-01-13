package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable{
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
			synchronized (clients) {
				clients.add(this);
			}
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
			synchronized (clients) {
				clients.remove(this);
			}
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
		synchronized (clients) {
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
}
