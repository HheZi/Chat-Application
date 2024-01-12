package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;

public class ClientHandler implements Runnable{

	public static ArrayList<ClientHandler> clients = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	
	public ClientHandler(Socket socket){
		try {
			this.socket = socket;
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			username = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			closeAll();
		}
		clients.add(this);
		brodcastMessage(username + " has joined the chat");
	}
	
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
	
	public void closeAll() {
		removeClient();
		try {
			socket.close();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void brodcastMessage(String message) {
		for (ClientHandler clientHandler : clients) {
			if(!clientHandler.username.equals(username)) {
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
	
	public void removeClient() {
		clients.remove(this);
		brodcastMessage(username + " has left the chat");
	}
}
