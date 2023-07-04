package application.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import accounttype.AccountType;
import javafx.application.Platform;
import request.GetFriendList;
import request.LoginSuccessfully;
import request.Message;
import request.Request;
import request.ServerMessage;

public class ListeningServer implements Runnable {

	private ObjectOutputStream out;
	private ObjectInputStream in;
	Request rq = null;
	public ListeningServer(ObjectOutputStream out, ObjectInputStream in) {
		this.out = out;
		this.in = in;
	}

	// listening message from server and handle it
	@Override
	public void run() {
		
		while (ClientModel.getInstance().getClient().getSocket().isConnected()) {
			
			try {
				rq = (Request) in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				ClientModel.getInstance().getClient().closeEverything();
				e.printStackTrace();
				break;
			}
			
			switch (rq.getRqType()) {
			case LOGIN_SUCCESSFULLY:
				System.out.println("Da nhan doi tuong login client "+((LoginSuccessfully)rq).getUsernameFromDB());
				String clientNameFromDB = ((LoginSuccessfully)rq).getUsernameFromDB();
				
				if (((LoginSuccessfully) rq).getAccountType() == AccountType.CLIENT) {

					Platform.runLater(() ->ClientModel.getInstance().getViewFactory().showClientWindow(clientNameFromDB));
					Platform.runLater(() -> ClientModel.getInstance().getViewFactory().closeLoginWindow());
				}
				
				break;
			case SERVER_MESSAGE:
				System.out.print("DA NHAN MESSAGE + "+((ServerMessage)rq).getMessage().toString());
				if(!((ServerMessage)rq).getMessage().equals(null)) {
					Platform.runLater(()->{
						try {
							ClientModel.getInstance().getViewFactory().getClientController().addNewMessage("server",((ServerMessage)rq).getMessage().toString(), ((ServerMessage)rq).getTimeSend(), true);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
				}
				break;
			case GET_FRIEND_LIST:
				System.out.print("Da nhan danh sach ban be!");
				Platform.runLater(()->ClientModel.getInstance().getViewFactory().getClientController().addFriendToListView(((GetFriendList)rq).getFriendList()));
				break;
			case MESSAGE:
				System.out.print("Da nhan tin nhan");
				System.out.print("From: "+((Message)rq).getFromID()+"\nMessage: "+((Message)rq).getMessage()+" to my id = "+((Message)rq).getSendID());
//				Platform.runLater(()->ClientModel.getInstance().getViewFactory().getClientController().addNewMessage(((Message)rq).getSendID(), ((Message)rq).getMessage(), ((Message)rq).getTimeSend()));
				Platform.runLater(()->{
					try {
						ClientModel.getInstance().getViewFactory().getClientController().displayReceiveMessage(((Message)rq).getFromID(), ((Message)rq).getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				break;
			default:
				break;
			}
		}
	}
	

}
