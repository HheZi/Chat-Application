package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import gui.GUI;


public class Client {
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	
	public Client(Socket socket, String username){
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
		} catch (IOException e) {
			e.printStackTrace();
			closeAll();
		}
	}
	
	
	public void sendMessage() {
		try {
			// sending username first
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			// writing the message and send
			try(Scanner sc = new Scanner(System.in)){
				while(socket.isConnected()) {
					String message = sc.nextLine();
					bufferedWriter.write(username + ": " + message);
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
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
						System.out.println(msgFromGroupChat);
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
		frame.setVisible(true);
		Socket socket = new Socket("192.168.0.158", 1007);
		Client client = new Client(socket, frame.getUsername());
		client.listenForMessages();
		client.sendMessage();
		
	}
}
