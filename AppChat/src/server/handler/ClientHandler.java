package server.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import accounttype.AccountType;
import request.GetFriendList;
import request.LoginRequest;
import request.LoginSuccessfully;
import request.Message;
import request.Request;
import request.RequestType;
import request.SendMessage;
import request.SignUp;
import server.model.ServerModel;

//import application.models.request.Request;

public class ClientHandler implements Runnable {

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String clientName;
	private String clientID;
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			
			//add client handler thread to array list
			clientHandlers.add(this);
			
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
					break;
				case GET_FRIEND_LIST:
					System.out.println("SERVER DA NHAN YEU CAU LAY DANH SACH BAN BE!");
					try {
						getFriendListFromDB();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println("get friend list error");
						e.printStackTrace();
					}
					break;
				case SEND_MESSAGE:
					if(((SendMessage)rq).getSendID()!=null){
						sendMessageTo(((SendMessage)rq).getSendID(), ((SendMessage)rq).getMessage(), ((SendMessage)rq).getTimeSend());
					}
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

	public void getFriendListFromDB() throws SQLException{
		
		Request sendFriendListForClient = new GetFriendList(RequestType.GET_FRIEND_LIST, ServerModel.getInstance().getFriendList(clientID));
//		System.out.println("FRIEND LIST = "+ServerModel.getInstance().getFriendList(clientID));
		try {
			this.out.writeObject(sendFriendListForClient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Send friend list for client error!");
			e.printStackTrace();
			closeEverything();
		}
	}
	
	public void checkLogin(String username, String password) { 

		ArrayList<String>  IDandName = ServerModel.getInstance().evaluateLoginType(username, password);
		
		clientID = IDandName.get(0);
		clientName = IDandName.get(1);
		
		
		if (ServerModel.getInstance().getLoginSuccessfully()) {
			if (ServerModel.getInstance().getLoginAccoutType() == AccountType.CLIENT) {
				Request loginSuccessfully = new LoginSuccessfully(RequestType.LOGIN_SUCCESSFULLY, AccountType.CLIENT, clientName);

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
				Request loginSuccessfully = new LoginSuccessfully(RequestType.LOGIN_SUCCESSFULLY, AccountType.ADMIN, clientName);
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

	
	public void signUp(Request signUpRequest) {
		
		SignUp signupRequest = (SignUp)signUpRequest;
		String username = signupRequest.getUsername(),
				   password = signupRequest.getPassword(),
				   name = signupRequest.getName(),
				   city = signupRequest.getCity(),
				   district = signupRequest.getDistrict(),
				   district2 = signupRequest.getDistrict2(),
				   street = signupRequest.getStreet();
		
		boolean checkSiggup = checkSignUp(username, password, name,city, district ,district2,street);
		

//		if(checkSiggup) {
//			
//			// soluongtk = dem tat cac cac tai khoang trong table account
//			//id = soluongtk + 1;         
//			int id=;
//			
//			String userId = "u"+ id;
//			
//			/*
//			 *viet vao cac truong tuong ung trong csdl users va account ( đăng ký tk lên sql )
//			 * 
//			 * */
//		}
		if (checkSiggup) {
		    try { 
		        // Lấy số lượng tài khoản bằng cách đếm tất cả các tài khoản có trong bảng account
		        Statement countStatement = ServerModel.getInstance().getDatadriver().getConn().createStatement();
		        String countQuery = "SELECT COUNT(*) FROM account";
		        ResultSet countResult = countStatement.executeQuery(countQuery);
		        int accountCount = 0;
		        if (countResult.next()) {
		            accountCount = countResult.getInt(1);
		        }
		        countResult.close();
		        countStatement.close();

		        // Tính toán id và userId mới
		        int id = accountCount + 1;
		        String userId = "u" + id;

		        // Thêm userId vào cột userid của bảng account
		        PreparedStatement updateAccountStatement = ServerModel.getInstance().getDatadriver().getConn().prepareStatement("UPDATE account SET userid = ? WHERE username = ?");
		        updateAccountStatement.setString(1, userId);
		        updateAccountStatement.setString(2, username);
		        updateAccountStatement.executeUpdate();
		        updateAccountStatement.close();

		        // Thêm userId vào cột userid của bảng "user"
		        PreparedStatement updateUserStatement = ServerModel.getInstance().getDatadriver().getConn().prepareStatement("UPDATE \"user\" SET userid = ?");
		        updateUserStatement.setString(1, userId);
		        updateUserStatement.executeUpdate();
		        updateUserStatement.close();

		        System.out.println("Đã thêm thành công userId: " + userId + " vào bảng account và user.");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		} else {
		    System.out.println("thêm không thành công");

		}
			

		

		
	}
	
	
	/*
	 * Kiem tra false
	 * */
	public boolean checkSignUp(String username, String password, String name, String city, String district, String district2, String street) {

		/*kiem tra username cua chua " " (space) hay khong -> neu co return false
		tiep tuc kiem tra tat cac cac truong con lai neu co bat ky truong nao co do dai > 30 hoac co bat ky truong nao bang "" -> return false
		tiep tuc kiem tra existed username
		
		viet ham checkExistedUsername
		*/
		
		 // Kiểm tra user có chứa space hay không nếu có return false
	    if (username.contains(" ") || password.contains(" ")) {
	        return false;
	    }

	    // Tiếp tục kiểm tra các trường còn lại nếu có bất kỳ trường nào có độ dài lớn hơn 30 hoặc bằng rỗng thì return false
	    if (username.isEmpty() || username.length() > 30 ||
	        password.isEmpty() || password.length() > 30 ||
	        name.isEmpty() || name.length() > 30 ||
	        city.isEmpty() || city.length() > 30 ||
	        district.isEmpty() || district.length() > 30 ||
	        district2.isEmpty() || district2.length() > 30 ||
	        street.isEmpty() || street.length() > 30) {
	        return false;
	    }
		
		
		boolean isExisted = ServerModel.getInstance().getDatadriver().checkExistedUsername(username);
		
		//neu username da ton tai -> return false, nguoc lai true -> !isExisted
		return !isExisted;
		
	}
	
	public void sendMessageTo(String userid, String message, String time) {
		for(ClientHandler client : clientHandlers) {
			if(userid.equals(client.getClientID())) {
				Request sendMessageToUserID = new Message(RequestType.MESSAGE, message, userid, time, this.clientID);
				try {
					client.getOut().writeObject(sendMessageToUserID);
					System.out.println("da gui tin nhan qua +"+userid);
				} catch (IOException e) {
					System.out.println("from "+clientID+" send message to user "+userid+" error.");
					closeEverything();
					e.printStackTrace();
				}
			}
		}
	}
	
	public void removeClientHandler() {
		clientHandlers.remove(this);
	}
	
	public void closeEverything() {
		removeClientHandler();
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
	
	public String getClientID() {
		return clientID;
	}
	public ObjectOutputStream getOut() {
		return out;
	}
}
