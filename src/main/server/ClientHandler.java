package main.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


//Автор: Цева Павло
public class ClientHandler implements Runnable{
	protected static final List<ClientHandler> CLIENTS = new ArrayList<ClientHandler>();
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	private String usernameInHex;
	
	protected ClientHandler(Socket socket){
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = bufferedReader.readLine();
			usernameInHex = Integer.toHexString(username.hashCode());
			
			
			synchronized (CLIENTS) {
				CLIENTS.add(this);
			}
			sendMessageToAllClients(username + " has joined the chat");
		} catch (IOException e) {
			e.printStackTrace();
			removeClient();
		}
	}
	
	@Override
	public void run() {
		while(socket.isConnected()) {
			try {
				String text = bufferedReader.readLine();
				if(text == null) {
					throw new IOException();
				}
				sendMessageToAllClients(text);
			} catch (IOException e) {
				removeClient();
				break;
			}
		}
		
	}
	
	private void writeMessage(String text) {
		try {
			if(!socket.isClosed()) {
				bufferedWriter.write(text);
				bufferedWriter.newLine();
				bufferedWriter.flush();				
			}
		} catch (IOException e) {
			e.printStackTrace();
			removeClient();
		}
	}
	
	protected void removeClient() {
			synchronized (CLIENTS) {
				CLIENTS.remove(this);
				close();
				Server.controller.removeClient(this.username);
				sendMessageToAllClients(this.username + " has left the chat");
			}

	}
	
	protected static ClientHandler findClientHandlerByUsername(String username) {
		synchronized (CLIENTS) {
			for (ClientHandler clientHandler : CLIENTS) {
				if(clientHandler.username.equals(username)) {
					return clientHandler;
				}
			}
			return null;
		}
	}
	
	private void sendMessageToAllClients(String message) {
		synchronized (CLIENTS) {
			for (ClientHandler clientHandler : CLIENTS) {
				if(clientHandler != this) {
					clientHandler.writeMessage(message);
				}
			}
		}
	}
	
	protected void rename(String newUsername) {
		String text = "%s:rename:%s".formatted(this.usernameInHex, newUsername);
		writeMessage(text);
		setUsername(newUsername);
	}
	
	protected void disconnect() {
		String text = "%s:disconnect:%b".formatted(this.usernameInHex, true);
		writeMessage(text);
	}
	
	protected void close() {
		try {
			socket.close();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {}
	}
	
	
	protected static void closeAll() { 
		synchronized (CLIENTS) {
			for (ClientHandler clientHandler : CLIENTS) {
				clientHandler.close();
			}			
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public String getUsername() {
		return username;
	}
	
	private void setUsername(String username) {
		usernameInHex = Integer.toHexString(username.hashCode());
		this.username = username;
	}
}
