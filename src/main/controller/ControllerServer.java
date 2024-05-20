package main.controller;

import main.server.ClientHandler;
import main.server.Server;
import main.view.ServerGUI;

//Автор: Цева Павло
public class ControllerServer {
	private Server server;
	
	private ServerGUI gui;
	public void exit() {
		server.stopServer();
	}
	
	public void addClient(ClientHandler clientHandler) {
		String ip = clientHandler.getSocket().getInetAddress().getHostAddress();
		String username = clientHandler.getUsername();
		gui.addRow(ip, username);
	}
	
	public void disconnect(String username) {
		server.disconnectClient(username);
	}
	
	public void renameClient(String newUsername, String username) {
		server.renameClient(newUsername, username);
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public void setServerGUI(ServerGUI serverGUI) {
		this.gui = serverGUI;
	}

	public void removeClient(String username) {
		gui.removeRow(username);
	}
}
