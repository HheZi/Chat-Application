package main;

import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JOptionPane;

import main.controller.ControllerServer;
import main.server.Server;
import main.view.ServerGUI;

//Автор: Цева Павло
public class ServerMain {
	public static void main(String[] args) {
		try {
			int port = 1007;
			
			ControllerServer controller = new ControllerServer();
			ServerSocket serverSocket = new ServerSocket(port);
			Server server = new Server(controller, serverSocket);
			ServerGUI serverGUI = new ServerGUI(controller); 
			server.start();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "This port already in use", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
