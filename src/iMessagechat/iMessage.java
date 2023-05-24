package iMessagechat;

import java.io.*;
/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 */
public class iMessage implements Serializable {

	protected static final long serialVersionUID = 1L;

	// ONLINEUSER to receive the list of the users connected
	// MESSAGE the message
	// LOGOUT to disconnect from the Server
	static final int ONLINEUSER = 0, MESSAGE = 1, LOGOUT = 2;
	private int type;
	private String message;
	
	// constructor
	iMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	// getters
	int getType() {
		return type;
	}
	String getMessage() {
		return message;
	}
}
