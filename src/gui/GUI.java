package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel usernameLabel;
	private JPanel contentPane;
	private JTextArea textArea;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public GUI() {
		setTitle("Chat Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 859, 541);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTextToTextAria(textField.getText());
			}
		});
		sendButton.setFont(new Font("Arial", Font.PLAIN, 14));
		
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Arial", Font.PLAIN, 15));
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		String username = JOptionPane.showInputDialog(this, "Enter your username");
		usernameLabel = new JLabel(username);
		usernameLabel.setForeground(Color.RED);
		usernameLabel.setBackground(Color.WHITE);
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					setTextToTextAria(textField.getText());
				}
			}
		});
		textField.setFont(new Font("Arial", Font.PLAIN, 14));
		textField.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(0)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 76, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 806, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(usernameLabel, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
					.addGap(700))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
					.addGap(6)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
						.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		
		
	}
	
	
	public String getUsername() {
		return usernameLabel.getText();
	}
	
	public void setTextToTextAria(String text) {
		String allText = textArea.getText();
		textArea.setText(allText + "\n" + text);
		textField.setText("");
	}
}
