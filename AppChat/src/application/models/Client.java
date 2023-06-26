package application.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private int PORT;
	private String serverName; 
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String clientName;
	
	public Client(String servername, int PORT) {
		this.PORT = PORT;
		this.serverName = servername;
		try {
			socket = new Socket(this.serverName, this.PORT);
			System.out.println("Connect successfully.");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Initial client out put stream failed.");
			e.printStackTrace();
		}
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Initial client in put stream failed.");
			e.printStackTrace();
		}
	}
	
	
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public ObjectOutputStream getOut() {
		return out;
	}
	public ObjectInputStream getIn() {
		return in;
	}
	
}
