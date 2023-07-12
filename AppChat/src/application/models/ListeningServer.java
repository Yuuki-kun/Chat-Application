package application.models;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.video.IConverter;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;



import accounttype.AccountType;
import javafx.application.Platform;
import request.FriendRequest;
import request.GetFriendList;
import request.GetSearchList;
import request.LoginSuccessfully;
import request.Message;
import request.UpdateFStatus;
import request.VideoRequest;
import request.Request;
import request.ResponeFriendRq;
import request.SeenStatus;
import request.SendMessageStatus;
import request.ServerMessage;
import request.TypingRequest;

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
				System.out.println("Da nhan doi tuong login client " + ((LoginSuccessfully) rq).getUsernameFromDB());
				String clientNameFromDB = ((LoginSuccessfully) rq).getUsernameFromDB();
				String clientID = ((LoginSuccessfully) rq).getId();

				if (((LoginSuccessfully) rq).getAccountType() == AccountType.CLIENT) {

					Platform.runLater(() -> ClientModel.getInstance().getViewFactory().showClientWindow(clientID, clientNameFromDB));
					Platform.runLater(() -> ClientModel.getInstance().getViewFactory().closeLoginWindow());
				}

				break;
			case SERVER_MESSAGE:
				System.out.print("DA NHAN MESSAGE + " + ((ServerMessage) rq).getMessage().toString());
				if (!((ServerMessage) rq).getMessage().equals(null)) {
					Platform.runLater(() -> {
						try {
							ClientModel.getInstance().getViewFactory().getClientController().addNewMessage("server", ((ServerMessage) rq).getMessage().toString(), ((ServerMessage) rq).getTimeSend(),
									true);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
				}
				break;
			case GET_FRIEND_LIST:
				System.out.print("Da nhan danh sach ban be!");
				Platform.runLater(() -> {
					try {
						ClientModel.getInstance().getViewFactory().getClientController().addFriendToListView(((GetFriendList) rq).getFriendList(), ((GetFriendList) rq).getFriendListOnlineStatus());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				break;
			case MESSAGE:
				System.out.print("Da nhan tin nhan");
				System.out.print("From: " + ((Message) rq).getFromID() + "\nMessage: " + ((Message) rq).getMessage() + " to my id = " + ((Message) rq).getSendID());
//				Platform.runLater(()->ClientModel.getInstance().getViewFactory().getClientController().addNewMessage(((Message)rq).getSendID(), ((Message)rq).getMessage(), ((Message)rq).getTimeSend()));
				Platform.runLater(() -> {
					try {
						ClientModel.getInstance().getViewFactory().getClientController().displayReceiveMessage(((Message) rq).getFromID(), ((Message) rq).getMessage(), ((Message) rq).getTimeSend());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				break;
			case GET_SEARCH_ADDFRIEND_LIST:
				try {
					ClientModel.getInstance().getViewFactory().getClientController().addFriendResultSearch(((GetSearchList) rq).getSearchList());
				} catch (IOException e) {
					System.out.println("Error display list friend");
					e.printStackTrace();
				}
				break;
			case FRIEND_REQUEST:
				// hien thi vao friend request
				System.out.println("DA NHAN YC KET BAN");
				try {
					ClientModel.getInstance().getViewFactory().getClientController().addFriendRequest(((FriendRequest) rq).getFriendId(), ((FriendRequest) rq).getName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case ACCEPT_FR:
				// received accepted message from server
				// accepted by id and name
				Platform.runLater(() -> ClientModel.getInstance().getViewFactory().getClientController().displayAcceptedFR(((ResponeFriendRq) rq).getName()));
				break;
			case SEND_MESSAGE_STATUS:
				if (((SendMessageStatus) rq).getSent()) {
					Platform.runLater(() -> ClientModel.getInstance().getViewFactory().getClientController().getSendMessageStatus().setText("✓ received"));
				}
				break;
			case SEEN_STATUS:
//				if (((SeenStatus) rq).getSeen()) {
//					Platform.runLater(() -> ClientModel.getInstance().getViewFactory().getClientController().getSendMessageStatus().setText("✓ seen"));
//				}
				break;
			case UPDATE_F_STATUS:
				Platform.runLater(() -> ClientModel.getInstance().getViewFactory().getClientController().updateFriendOnline(((UpdateFStatus) rq).getId(), ((UpdateFStatus) rq).getStatus()));
				break;
			case TYPING:
				Platform.runLater(() -> ClientModel.getInstance().getViewFactory().getClientController().playTypingMessage(((TypingRequest) rq).getDur()));
				break;
			case SEND_VIDEO:
				// tai tao lai video va ghi vao file
				System.out.println("Da nhan video tu "+((VideoRequest)rq).getSenderID()+", bat dau write");
				System.out.println(((VideoRequest)rq).getBuffer().length);


			        String outputFilePath = "video-form"+((VideoRequest)rq).getSenderID()+".mp4";

			        byte[] buffer = (byte[])((VideoRequest)rq).getBuffer(); /* your video data buffer */;

			        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
		                fos.write(buffer);
		            }catch(Exception e) {
		            		e.printStackTrace();
		            }
			    
			        Platform.runLater(()->							{
						try {
							ClientModel.getInstance().getViewFactory().getClientController().displayReceiveVideo(((VideoRequest)rq).getSenderID(), outputFilePath, "time");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
						
				
				
				System.out.println("da nhan video");

				break;
			default:
				break;
			}
		}
	}


}
