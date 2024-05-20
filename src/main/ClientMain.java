package main;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

import javax.swing.JOptionPane;

import main.client.Client;
import main.controller.ControllerClient;
import main.view.ClientGUI;

// Автор: Цева Павло
public class ClientMain {
	public static void main(String[] args){
		try {
			int port = 1007;
			String ipOfServer = Inet4Address.getLocalHost().getHostAddress();
			
			String username = JOptionPane.showInputDialog(null,"Enter your username");
			Socket socket = new Socket(ipOfServer, port);
			
			ControllerClient controller = new ControllerClient();
			ClientGUI clientGUI = new ClientGUI(username, controller);
			Client client = new Client(socket, controller, username);
		}catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Can't connect to server", "Error", JOptionPane.ERROR_MESSAGE);
		}
	
	}
}
