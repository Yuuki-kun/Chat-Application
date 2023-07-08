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
import java.util.HashMap;
import java.util.Map;

import accounttype.AccountType;
import application.models.ClientModel;
import request.FriendRequest;
import request.GetFriendList;
import request.GetSearchList;
import request.LoginRequest;
import request.LoginSuccessfully;
import request.Message;
import request.Request;
import request.RequestType;
import request.ResponeFriendRq;
import request.SeenStatus;
import request.SendMessage;
import request.SendMessageStatus;
import request.SignUp;
import searchalgorithm.Search;
import server.model.ServerModel;

//import application.models.request.Request;

public class ClientHandler implements Runnable {

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String clientName;
	private String clientID;
	private Map<String, String> friendList = new HashMap<>();
	
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
					
					
					
					break;
				case GET_SEARCH_ADDFRIEND_LIST:
					String inputToSearch = ((GetSearchList)rq).getInput();
					
					ArrayList<String> clientNameList = new ArrayList<>();
					Map<String, String> clientIDAndName = new HashMap<>();
				
					setClientMap(clientIDAndName);
					
					 for(Map.Entry<String, String> entry : clientIDAndName.entrySet()) {
						 clientNameList.add(entry.getValue());
					 }
					 
					 //tim kiem client trong client name list -> tra ve list tim thay
					 ArrayList<String> result = Search.searchIboxByName(inputToSearch, clientNameList);
					 
					 Map<String, String> resultMap = new HashMap<>();
					 
					 //neu ket qua tra ve k rong
					 
					 if(!result.isEmpty()) {
						 for(String name : result) {
							 for(Map.Entry<String, String> entry : clientIDAndName.entrySet()) {
								 if(entry.getValue().equals(name) && !name.equals(clientName)) {
									 resultMap.put(entry.getKey(), entry.getValue());
								 }
							 }	
						 }
					 }
					 
					 //loai tru ket qua tra ve voi list ban be
					 
					 for(Map.Entry<String, String> entri : resultMap.entrySet()) {
						 for(Map.Entry<String, String> entry : friendList.entrySet()) {
							 if(entri.getKey().equals(entry.getKey())) {
								 resultMap.remove(entri.getKey());
							 }
						 }
					 }
					 
					 //send to client
					 ((GetSearchList)rq).setSearchList(resultMap);
					 
					 this.out.writeObject(rq);
					 
					break;
				case SEND_FRIEND_REQUEST:
					//gui yc ket ban qua ben kia
					System.out.println(clientID+"Yeu cau ket ban voi + "+((FriendRequest)rq).getFriendId());
					friendRequestHandler(((FriendRequest)rq).getFriendId());
					break;
				case ACCEPT_FR:
					//accept fq with id
					makeFriendWith(((ResponeFriendRq)rq).getId());
					try {
						getFriendListFromDB();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//send accepted message to id
					//clientid accepted id
					
					for(ClientHandler client : clientHandlers) {
						if(((ResponeFriendRq)rq).getId().equals(client.getClientID())) {
							Request accepted = new ResponeFriendRq(RequestType.ACCEPT_FR, clientID, clientName);
							try {
								client.getOut().writeObject(accepted);
								System.out.println("da gui tin nhan qua +"+((ResponeFriendRq)rq).getId());
							} catch (IOException e) {
								System.out.println("from "+clientID+" send accepted friend message to user "+((ResponeFriendRq)rq).getId()+" error.");
								closeEverything();
								e.printStackTrace();
							}
						}
					}
					
					break;
				case SEEN_STATUS:

					String sendToID = (((SeenStatus)rq).getId());
					sendSeenStatusToId(sendToID, rq);
					
					break;
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
	
	public synchronized void sendSeenStatusToId(String sendToID, Request rq) {

		for(ClientHandler client : clientHandlers) {
			if(sendToID.equals(client.getClientID())) {
				try {
					client.getOut().writeObject(rq);
				} catch (IOException e) {
					System.out.println("Send seen status to "+sendToID+" error.");
					e.printStackTrace();
				}
				break;
			}
		}
	}
	
	public void makeFriendWith(String id) {
		ServerModel.getInstance().writeFriend(clientID, id);
	}
	
	public synchronized void setClientMap(Map<String, String> clientIdAndName) {
		for(ClientHandler client : clientHandlers) {
			clientIdAndName.put(client.getClientID(), client.getClientName());
		}
	}
	
	public synchronized void friendRequestHandler(String friendId) {
		for(ClientHandler client : clientHandlers) {
			if(client.getClientID().equals(friendId)) {
				//send friend request to friendId with sender's id
				Request friendRq = new FriendRequest(RequestType.FRIEND_REQUEST, clientID, clientName);
				try {
					client.getOut().writeObject(friendRq);
				} catch (IOException e) {
					System.out.println("friend request error.");
					e.printStackTrace();
				}
			}
		}
	}

	public void getFriendListFromDB() throws SQLException{
		
		friendList = ServerModel.getInstance().getFriendList(clientID);
		
		Request sendFriendListForClient = new GetFriendList(RequestType.GET_FRIEND_LIST, friendList);
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
				Request loginSuccessfully = new LoginSuccessfully(RequestType.LOGIN_SUCCESSFULLY, AccountType.CLIENT, clientName, clientID);

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
				Request loginSuccessfully = new LoginSuccessfully(RequestType.LOGIN_SUCCESSFULLY, AccountType.ADMIN, clientName, clientID);
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
					
					Request sendMessageSuccessfully = new SendMessageStatus(RequestType.SEND_MESSAGE_STATUS, true);	
					this.out.writeObject(sendMessageSuccessfully);
					
				} catch (IOException e) {
					
					System.out.println("from "+clientID+" send message to user "+userid+" error.");
					Request sendMessageSuccessfully = new SendMessageStatus(RequestType.SEND_MESSAGE_STATUS, false);	
					
					try {
						this.out.writeObject(sendMessageSuccessfully);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
	
	public String getClientName() {
		return clientName;
	}
	public ObjectOutputStream getOut() {
		return out;
	}
}
