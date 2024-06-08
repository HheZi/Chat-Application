package main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.controller.ControllerClient;

//Автор: Цева Павло
public class ClientGUI extends JFrame {

	private JLabel usernameLabel;
	private JTextArea textArea;
	private JTextField textField;
	private ControllerClient controller;

	private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	public ClientGUI(String username, ControllerClient controller) {
		super("Chat Application");
        this.controller = controller;
        this.controller.setClientGUI(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(0,10));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernameLabel = new JLabel((username.isEmpty()) ? "User" : username);
        topPanel.add(usernameLabel);
        usernameLabel.setForeground(Color.RED);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        add(topPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 15));
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setForeground(Color.blue);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5,5));
        add(bottomPanel, BorderLayout.SOUTH);

        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setColumns(30);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessageAndWrite(textField.getText());
                }
            }
        });
        bottomPanel.add(textField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessageAndWrite(textField.getText().trim()));
        sendButton.setFont(new Font("Arial", Font.PLAIN, 14));
        bottomPanel.add(sendButton, BorderLayout.EAST);

        setMinimumSize(new Dimension(300, 300));
        setSize(1000,500);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	public void renameClient(String newUsername) {
		usernameLabel.setText(newUsername);
	}
	
	public void setTextToTextAria(String text) {
		textArea.append(text + "\n");
		textField.setText("");
	}
	
	private void sendMessageAndWrite(String text) { 
		if(controller.isSocketConnected()) {
			JOptionPane.showMessageDialog(this, "The server has stopped working", "Server stops", JOptionPane.INFORMATION_MESSAGE);
			exit();
		}
		else if(!text.equals("")) {
			String date = "(" + FORMATTER.format(LocalDateTime.now()) + ") ";	
			controller.sendMessage(text,  date);				
			setTextToTextAria(date + "Me>> " + text);			
		}
	}
	
	public void messageKickedAndExit() {
		JOptionPane.showMessageDialog(this, "You are kicked from server", "Kicked", JOptionPane.WARNING_MESSAGE);
		exit();
	}
	
	private void exit() {
		controller.exit();
		dispose();
	}
}
