package main.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import main.controller.ControllerClient;

//Автор: Цева Павло
public class Client {
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	private ControllerClient controller;
	private String usernameInHex; 
	
	public Client(Socket socket, ControllerClient controller, String username){
		try {
			this.controller = controller;
			this.controller.setClient(this);
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username.equals("") ? "User" : username;
			usernameInHex = Integer.toHexString(this.username.hashCode());
			
			writeMessage(this.username);
			listenForMessages();
		} catch (IOException e) {
			e.printStackTrace();
			closeAll();
		}
	}
	
	
	public void writeMessage(String text) {
		try {
			bufferedWriter.write(text);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			closeAll();
		}
	}
	
	public void sendMessage(String message, String date) {
		if(socket.isConnected()) {
			writeMessage(date + this.username + ">> " + message);
		}
	}
	
	private void checkCommand(String[] arr) {
		String command = arr[1], value = arr[2];
		
		switch (command) {
			case "rename":
				setUsername(value);
				controller.setNewUsername(username);
				break;
			case "disconnect":
				closeAll();
				controller.disconnectMessage();
				break;
		}
			
	}
	
	public void listenForMessages(){
		new Thread() {
			@Override
			public void run() {
				while(socket.isConnected()) {
					try {
						String text = bufferedReader.readLine();
						if(text == null) {
							throw new IOException();
						}
						if(text.startsWith(usernameInHex)) {
							checkCommand(text.split(":"));
							continue;
						}
						controller.getMessage(text);
					} catch (IOException e) {
						closeAll();
						break;
					}
				}
			}
		}.start();
	}
	
	
	
	public void closeAll() {
		try {
			socket.close();
			bufferedReader.close();
			bufferedWriter.close();				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public Socket getSocket() {
		return socket;
	}
	
	public void setUsername(String username) {
		usernameInHex = Integer.toHexString(username.hashCode());
		this.username = username;
	}
}
