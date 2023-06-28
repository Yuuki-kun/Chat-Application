package server.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import accounttype.AccountType;
import request.LoginRequest;
import request.LoginSuccessfully;
import request.Request;
import request.RequestType;
import server.model.ServerModel;

//import application.models.request.Request;

public class ClientHandler implements Runnable {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// nên tìm cách gửi đối tượng mới, ví dụ như json
		Request rq;
		while (socket.isConnected()) {
			try {
				
				rq = (Request) in.readObject();

				switch (rq.getRqType()) {
				case LOGIN:

					System.out.println("SERVER DA NHAN YEU CAU LOGIN");
					checkLogin(((LoginRequest) rq).getUsername(), ((LoginRequest) rq).getPassword());
				default:
					break;
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				closeEverything();
				e.printStackTrace();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				closeEverything();
				e.printStackTrace();
				break;
			}
		}
	}

	public void checkLogin(String username, String password) { 

		String usernameFromDB  = ServerModel.getInstance().evaluateLoginType(username, password);
		
		if (ServerModel.getInstance().getLoginSuccessfully()) {
			if (ServerModel.getInstance().getLoginAccoutType() == AccountType.CLIENT) {
				Request loginSuccessfully = new LoginSuccessfully(RequestType.LOGIN_SUCCESSFULLY, AccountType.CLIENT, usernameFromDB);

				try {
					this.out.writeObject(loginSuccessfully);
					System.out.println("Da gui doi tuong login client.");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					closeEverything();
					e.printStackTrace();
					System.out.println("Send login successfully error.");
				}
			} else {
				System.out.println("ADMIN LOGIN.");
				Request loginSuccessfully = new LoginSuccessfully(RequestType.LOGIN_SUCCESSFULLY, AccountType.ADMIN, usernameFromDB);
				try {
					this.out.writeObject(loginSuccessfully);
					System.out.println("Da gui doi tuong login admin.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					closeEverything();
					e.printStackTrace();
					System.out.println("Send login successfully error.");
				}
			}
		} else {
			System.out.println("Login failed");
		}
	}

	public void closeEverything() {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("Error closing in.");
				e.printStackTrace();
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				System.out.println("Error closing out.");
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Error closing socket.");
				e.printStackTrace();
			}
		}
	}
	
	public ObjectOutputStream getOut() {
		return out;
	}
}
