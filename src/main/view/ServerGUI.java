package main.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import main.controller.ControllerServer;

//Автор: Цева Павло
public class ServerGUI extends JFrame {

	private JTable table;
	private JTextField usernameField;
	private DefaultTableModel model;
	
	private ControllerServer controller;

	public ServerGUI(ControllerServer controller) {
		this.controller = controller;
		controller.setServerGUI(this);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Server");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		getContentPane().setLayout(new GridLayout(1,2));
		
		JPanel panelforTable= new JPanel(new BorderLayout()), panelforbuttons = new JPanel();
		panelforbuttons.setLayout(new BoxLayout(panelforbuttons, BoxLayout.Y_AXIS));
		
		usernameField = new JTextField("");
		usernameField.setEditable(false);
		
		JButton banButton = new JButton("Disconnect"), renameButton = new JButton("Rename"), 
				exitButton = new JButton("Exit");
		
		banButton.addActionListener(e -> disconnect());
		
		renameButton.addActionListener(e -> rename());
		
		exitButton.addActionListener(e -> exit());
		
		usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		banButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		renameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		usernameField.setMaximumSize(new Dimension(300, 100));
		banButton.setMaximumSize(new Dimension(300, 100));
		renameButton.setMaximumSize(new Dimension(300, 100));
		exitButton.setMaximumSize(new Dimension(300, 100));
		
		panelforbuttons.add(Box.createVerticalGlue());
		panelforbuttons.add(usernameField);
		panelforbuttons.add(Box.createRigidArea(new Dimension(0, 30)));
		panelforbuttons.add(banButton);
		panelforbuttons.add(Box.createRigidArea(new Dimension(0, 5)));
		panelforbuttons.add(renameButton);
		panelforbuttons.add(Box.createRigidArea(new Dimension(0, 5)));
		panelforbuttons.add(exitButton);
		panelforbuttons.add(Box.createVerticalGlue());
		
		
		JScrollPane scrollPane = new JScrollPane();
		model = new DefaultTableModel(new String[]{"IP", "Username"},0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setValues();
			}
		});
		
		
		panelforTable.add(scrollPane, BorderLayout.CENTER);
		
		add(panelforbuttons, BorderLayout.CENTER);
		add(panelforTable);
		
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void disconnect() {
		if(!usernameField.getText().equals("")) {
			controller.disconnect(usernameField.getText());
					
		}
	}

	private void exit() {
		controller.exit();
		dispose();
	}
	
	private void setValues(){
		int row = table.getSelectedRow();
		usernameField.setText((String) table.getModel().getValueAt(row, 1));
	}
	
	
	private void rename() {
		String username = usernameField.getText();
		
		if(!username.equals("")) {
			String newUsername = JOptionPane.showInputDialog(this, "Enter new username");
			controller.renameClient(newUsername, username);
			renameUsernameInTable(newUsername, username);
			usernameField.setText("");
		}
	}
	
	public void addRow(String ip, String username) {
		model.addRow(new String[] {ip, username});
	}
	
	private int getRowByUsername(String username) {	
		for (int i = 0; i < model.getRowCount(); i++) {
			if(model.getValueAt(i, 1).equals(username)) {
				return i;
			}
		}
		return -1;
	}
	
	private void renameUsernameInTable(String newUsername, String oldUsername) {
		int row = getRowByUsername(oldUsername);
		
		model.setValueAt(newUsername, row, 1);
	}
	
	public void removeRow(String username) {
		int row = getRowByUsername(username);
		if(usernameField.getText().equals(username)) {
			usernameField.setText("");	
		}
		model.removeRow(row);
	}
}
