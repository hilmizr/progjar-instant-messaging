package iMessagechat;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


/*
 * This is the GUI for client message app
 */
public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// will first hold "Username:", later on "Enter message"
	private JLabel label;
	private JTextField tf;			 	// to hold the Username and later on to type the messages
	private JTextField tfServer, tfPort; // to hold the server address an the port number
	private JButton login, logout, onlineuser; //button for login,log out and see the online user
	private JTextArea ta; 				//// for the chat room
	private boolean connected;			// if it is for connection
	private Client client;				// the Client object
	private int defaultPort;			// the default port number that we've set :1500
	private String defaultHost;

	// then we make Constructor connection for receiving a socket number
	ClientGUI(String host, int port) {

		super("iMessage Client");
		defaultPort = port;
		defaultHost = host;
		
		// The NorthPanel with:
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		// the server name and the port number
		JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
		// the two JTextField with default value for server address and port number
		tfServer = new JTextField(host);
		tfPort = new JTextField("" + port);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);


		// The main chat room which is will show the entire conversation
		ta = new JTextArea("Welcome to iMessage\n", 20, 20);
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		ta.setFont(new Font("arial",Font.PLAIN,13));
		add(centerPanel, BorderLayout.NORTH);
		
		serverAndPort.add(new JLabel("Server Address:  "));
		serverAndPort.add(tfServer);
		serverAndPort.add(new JLabel("Port Number:  "));
		serverAndPort.add(tfPort);
		serverAndPort.add(new JLabel(""));
		// adds the Server an port field to the GUI
		northPanel.add(serverAndPort);

		// the Label and the TextField
		
		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		northPanel.add(label);
		tf = new JTextField("Anonymous");
		tf.setBackground(Color.LIGHT_GRAY );
		tf.setFont(new Font("calibri",Font.PLAIN,16));
		northPanel.add(tf);
		add(northPanel, BorderLayout.CENTER);

		// the 3 buttons (login, logout and online user)
		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		
		// we have to login first before being able to logout either to see online user
		logout.setEnabled(false);		
		onlineuser = new JButton("Online User");
		onlineuser.addActionListener(this);
		onlineuser.setEnabled(false);		

		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(onlineuser);
		add(southPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(450, 500);
		setVisible(true);
		tf.requestFocus();

	}

	// called by the Client to append text in the TextArea 
	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	// if the GUI  connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		onlineuser.setEnabled(false);
		label.setText("Enter your username below");
		tf.setText("Client");
		// reset port number and host name as a construction time
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		// let the user change them
		tfServer.setEditable(false);
		tfPort.setEditable(false);
		// don't react to a <CR> after the username
		tf.removeActionListener(this);
		connected = false;
	}
		
	/*
	* Button or JTextField clicked
	*/
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
		if(o == logout) {
			client.sendMessage(new iMessage(iMessage.LOGOUT, ""));
			return;
		}
		// button to check the online user in the room
		if(o == onlineuser) {
			client.sendMessage(new iMessage(iMessage.ONLINEUSER, ""));				
			return;
		}

		// if we have connnected then we just can start to chat
		if(connected) {
			client.sendMessage(new iMessage(iMessage.MESSAGE, tf.getText()));				
			tf.setText("");
			return;
		}
		

		if(o == login) {
			//  it is a connection request
			String username = tf.getText().trim();
			// if the username is empty just ignore it
			if(username.length() == 0)
				return;
			// empty serverAddress ,ignore it
			String server = tfServer.getText().trim();
			if(server.length() == 0)
				return;
			// empty or invalid port numer, ignore it
			String portNumber = tfPort.getText().trim();
			if(portNumber.length() == 0)
				return;
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			}
			catch(Exception en) {
				return;   
			}

			// creating a new Client with GUI
			client = new Client(server, port, username, this);
			if(!client.start()) 
				return;
			tf.setText("");
			label.setText("Enter your message below");
			connected = true;
			
			login.setEnabled(false); // disable login button
			logout.setEnabled(true); // enable the 2 buttons
			onlineuser.setEnabled(true); // disable the Server and Port JTextField
			tfServer.setEditable(false);
			tfPort.setEditable(false);
			tf.addActionListener(this); // Action listener for when the user enter a message
		}

	}

	// the main thing to start the  the server
	public static void main(String[] args) {
		new ClientGUI("localhost", 1500);
	}

}
