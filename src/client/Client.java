package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	
	public Client(Socket socket, String username){
		try {
			this.socket = socket;
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
		} catch (IOException e) {
			e.printStackTrace();
			closeAll();
		}
	}
	
	public void sendMessage() {
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
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
		try(Scanner sc = new Scanner(System.in)){
			System.out.println("Enter your username: ");
			String username = sc.next();
			Socket socket = new Socket("localhost", 1007);
			Client client = new Client(socket, username);
			client.listenForMessages();
			client.sendMessage();
		}
	}
}
