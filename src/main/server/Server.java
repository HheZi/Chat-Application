package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.controller.ControllerServer;

//Автор: Цева Павло
public class Server {
	private ServerSocket serverSocket;
	protected static ControllerServer controller;
	
	public Server(ControllerServer controller, ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		Server.controller = controller;
		controller.setServer(this);
	}


	public void start() {
		try {
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();				
				ClientHandler clientHandler = new ClientHandler(socket);
				controller.addClient(clientHandler);
				
				Thread thread = new Thread(clientHandler);
				thread.setDaemon(true);
				thread.start();
						
			}
		} catch (IOException e) {}
	}
	
	
	public void disconnectClient(String username) {
		ClientHandler clientHandler = ClientHandler.findClientHandlerByUsername(username);
		clientHandler.disconnect();
	}


	public void renameClient(String newUsername, String username) {
		ClientHandler clientHandler = ClientHandler.findClientHandlerByUsername(username);
		clientHandler.rename(newUsername);
	}
	
	public void stopServer() {
		try {
			ClientHandler.closeAll();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
