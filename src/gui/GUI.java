package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import client.Client;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JScrollPane;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel usernameLabel;
	private JPanel contentPane;
	private JTextArea textArea;
	private JTextField textField;
	private Client client;

	/**
	 * Create the frame.
	 */
	public GUI() {
		setTitle("Chat Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 859, 532);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessageAndWrite(textField.getText());
			}
		});
		sendButton.setFont(new Font("Arial", Font.PLAIN, 14));
	
		usernameLabel = new JLabel("Username");
		usernameLabel.setForeground(Color.RED);
		usernameLabel.setBackground(Color.WHITE);
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessageAndWrite(textField.getText());
				}
			}
		});
		textField.setFont(new Font("Arial", Font.PLAIN, 14));
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(0)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 89, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(usernameLabel, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
					.addGap(700))
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 833, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
						.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Arial", Font.PLAIN, 15));
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		contentPane.setLayout(gl_contentPane);
		
		
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public void setUsername(String username) {
		usernameLabel.setText(username);
	}
	
	public void setTextToTextAria(String text) {
		textArea.append(text + "\n");
		textField.setText("");
	}
	
	private void sendMessageAndWrite(String text) { 
		String date = DateTimeFormatter.ofPattern("HH:mm").format(LocalDateTime.now());	
		client.sendMessage(text, "(" + date + ") ");
		setTextToTextAria("(" + date + ") " + "Me>> " + text);
	}
}
