package server.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import request.Request;


//import application.models.request.Request;

public class ClientHandler implements Runnable{

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//nên tìm cách gửi đối tượng mới, ví dụ như json
		Request rq;
			while(socket.isConnected()) {
				try {
					rq = ((Request)in.readObject());
					
					switch(rq.getRqType()) {
					case LOGIN:
						System.out.println("SERVER DA NHAN YEU CAU LOGIN");
					}
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
	}
	
	public void checkLogin() {
	
	}

}
