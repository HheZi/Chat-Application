package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import gui.GUI;


public class Client {
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	private GUI gui;
	
	public Client(Socket socket, GUI gui , String username){
		try {
			this.gui = gui;
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
			
			//sending the username to ClientHandler to invoke "username has joined the chat"
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
			closeAll();
		}
	}
	
	
	public void sendMessage(String message, String date) {
		try {
			// writing the message and send
			if(!socket.isClosed()) {
				bufferedWriter.write(date + username + ">> " + message);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			closeAll();
		}
	}
	
	// Listening for the messages
	public void listenForMessages(){
		new Thread() {
			@Override
			public void run() {
				while(socket.isConnected()) {
					try {
						String msgFromGroupChat = bufferedReader.readLine();
						gui.setTextToTextAria(msgFromGroupChat);
					//fix that	
					} catch (IOException e) {
						e.printStackTrace();
						closeAll();
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
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		GUI frame = new GUI();
		String username = JOptionPane.showInputDialog(frame, "Enter your username");
		frame.setUsername(username);
		frame.setVisible(true);
		try {
			Socket socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 1007);
			Client client = new Client(socket, frame, username);
			frame.setClient(client);
			client.listenForMessages();
		}catch (ConnectException e) {
			JOptionPane.showMessageDialog(frame, "Can't connect to server", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		
	}
}
