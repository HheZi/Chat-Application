package main.controller;

import main.client.Client;
import main.view.ClientGUI;

public class ControllerClient {
	private ClientGUI gui;
	private Client client;
	
	public boolean isSocketConnected() {
		return client.getSocket().isClosed();
	}
	public void disconnectMessage() {
		gui.messageKickedAndExit();
	}
	
	public void getMessage(String text) {
		gui.setTextToTextAria(text);
	}

	public void sendMessage(String text, String date) {
		client.sendMessage(text, date);
	}
	
	public void setNewUsername(String newUsername) {
		gui.renameClient(newUsername);
	}
	
	public void exit() {
		client.closeAll();
	}
	
	public void setClientGUI(ClientGUI clientGUI) {
		this.gui = clientGUI;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
}
