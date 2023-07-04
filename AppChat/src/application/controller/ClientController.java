package application.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.models.ClientModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import request.GetFriendList;
import request.Message;
import request.Request;
import request.RequestType;
import request.SendMessage;

public class ClientController implements Initializable {

	@FXML
	private BorderPane clientBorderPane;

	@FXML
	private ScrollPane messScrollPane;

	@FXML
	private VBox messVBox;

	@FXML
	private AnchorPane friend_ib;

	@FXML
	private AnchorPane friend_book;

	@FXML
	private JFXButton friend_list_btn;

	@FXML
	private AnchorPane friend_list_view;

	@FXML
	private ScrollPane friend_list_view_scroll;

	@FXML
	private VBox friend_list_view_vbox;

    @FXML
    private ScrollPane message_view_sp;


    @FXML
    private Button sendMessageBtn;
	
    @FXML
    private TextField message_tf;
    
    @FXML
    private AnchorPane chat_view;
    
    private String sendToUserID;
    
    private VBox originVBox;
    
    private static final Duration FLASH_DURATION = Duration.seconds(0.5);
    private static final Duration STOP_DURATION = Duration.seconds(5);

    
    private Timeline flashTimeline;
    private Timeline stopTimeline;

    
    private ArrayList<ClientBoxController> clientInBoxFriend = new ArrayList<>();
    
    //Map chua danh danh ban be -> hien tai app chi cho phep nhan tin qua lai giua ban be voi nhau
    private Map<String, String> friendList;
    
	private boolean getListFriend =false;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.originVBox = new VBox();
		this.friend_ib.setVisible(true);
		this.chat_view.setVisible(true);
		this.friend_book.setVisible(false);
		this.friend_list_view.setVisible(false);
//		this.messScrollPane.setOnMouseClicked(event -> System.out.println("scroll pane clicked"));
//		this.messVBox.setOnMouseClicked(event -> System.out.println("vbox clicked"));
		
		messVBox.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				messScrollPane.setVvalue((Double) arg2);
			}
		});
		

//		
		this.sendMessageBtn.setOnAction(event -> {
			try {
				sendMessage(sendToUserID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		sendgetfriendlistrequest();
	}

	public void sendMessage(String sendToUserID) throws IOException {
		
		
		String messToSend = this.message_tf.getText();
		
		//add message text to chat area
		System.out.println("chat = "+messToSend);
		if(messToSend!=null) {
			HBox messbox = new HBox();
			
			messbox.setAlignment(Pos.TOP_RIGHT);
			messbox.setPadding(new Insets(5, 5, 5, 10));

			
			Text text = new Text(messToSend);
			TextFlow textFlow = new TextFlow(text);
			textFlow.setTextAlignment(TextAlignment.JUSTIFY);
			
			textFlow.setStyle("-fx-color: rgb(239, 242, 255);"+
					"-fx-background-color: rgb(15, 125, 242);"+
					"-fx-background-radius: 20px;"
					+ "-fx-max-width: 330;"
					
					);
			textFlow.setPadding(new Insets(10, 15, 10, 15));
			text.setFill(Color.color(0.934, 0.945, 0.996));
//			textFlow.setPrefSize(350, 50);
			messbox.getChildren().addAll(textFlow);
			message_tf.clear();
			
			writeSendMessageRequest(sendToUserID, messToSend);
			originVBox.getChildren().add(messbox);
			
			
		}
	}
	
	//sau khi nhan message tu user -> hien thi vao chat view
	public void displayReceiveMessage(String receiveFromID, String message) throws IOException {
		HBox receiveMess = new HBox();
		receiveMess.setAlignment(Pos.TOP_LEFT);
		receiveMess.setPadding(new Insets(5, 5, 5, 10));
		
		ImageView imageView = new ImageView();
		imageView.setFitWidth(50);
		imageView.setFitHeight(50);
        Image image = new Image(getClass().getResourceAsStream("/application/resources/images/logo_small.png"));
        imageView.setImage(image);
		
		Text text = new Text(message);
		TextFlow textFlow = new TextFlow(text);
		textFlow.setTextAlignment(TextAlignment.JUSTIFY);
		
		textFlow.setStyle("-fx-color: rgb(239, 242, 255);"+
				"-fx-background-color: rgb(15, 125, 242);"+
				"-fx-background-radius: 20px;"
				+ "-fx-max-width: 330;"
				
				);
		textFlow.setPadding(new Insets(10, 15, 10, 15));
		text.setFill(Color.color(0.934, 0.945, 0.996));
		receiveMess.getChildren().addAll(imageView, textFlow);

		addNewMessage(receiveFromID, message, "time receive here", true);

		
		originVBox.getChildren().add(receiveMess);
		
		
		//neu chua bam vao xem (clicked) thi se chop chop do do
		
	}
	
	public void writeSendMessageRequest(String toUserId, String message) throws IOException {
		Request sendMessageRequest = new SendMessage(RequestType.SEND_MESSAGE, message, toUserId);
		try {
			ClientModel.getInstance().getClient().getOut().writeObject(sendMessageRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Send message request to "+toUserId+" to server error");
			e.printStackTrace();
		}
		//sau khi gui tin nhan qua ben kia -> add vao new friend box view
		addNewMessage(toUserId, message, "time send here", false);
//		resetVBox(originVBox);
	}
	
	// add inbox box vao friend inbox view
	public void addNewMessage(String userID, String message, String time, boolean isReceive) throws IOException {
		String name;
		if(userID.equals("server")) {
			 name = "SERVER";
		}else {
			 name = friendList.get(userID);

		}
		
		//tao mot arraylist luu cac clientboxcontroller lai
		//neu doi tuong inbox da co trong list -> khong them vao nua
		if(!clientInBoxFriend.isEmpty()) {
			for(ClientBoxController clientbox: clientInBoxFriend) {
				System.out.println("client box = id = "+clientbox.getUserID());
				//found
				if(clientbox.getUserID().equals(userID)){
					resetVBox(clientbox.getChatVBox());					
					if(isReceive) {
						startFlashing(clientbox.getFriend_box_hbox());
						startStopTimer((clientbox.getFriend_box_hbox()));
					}
					return;
				}
			}
		}
		
		
		
		HBox h;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/guiobjectjson/testHBox.fxml"));
		h = loader.load();
		
		h.setOnMouseEntered(event ->{
            h.setStyle("-fx-background-color: #cccccc;");

        });
        
        h.setOnMouseExited(event ->{
            h.setStyle("-fx-background-color: #ffffff;");

        });
		
		VBox viewb = new VBox();
		this.originVBox = viewb;
		
		resetVBox(viewb);
		
		ClientBoxController clientBoxController = new ClientBoxController(name, userID, h, viewb);
		loader.setController(clientBoxController);

		
		viewb.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				message_view_sp.setVvalue((Double)arg2);
			}
		});
		
		
		clientInBoxFriend.add(clientBoxController);
		
		
		// lay con vbox ra tu hbox
		VBox v = (VBox) h.getChildren().get(1);

		// lay con label ra tu vbox
		Label l = (Label) v.getChildren().get(0);
		l.setText(name);

		Label t = (Label) v.getChildren().get(1);
		t.setText(message);

		Label i = (Label) v.getChildren().get(2);

		i.setText(time);
		
		
		if(isReceive) {
	        startFlashing(h);
	        startStopTimer(h);

		}
		
//		h.setOnMouseClicked(event ->{
//			this.resetVBox(this.originVBox);
//		});

		messVBox.getChildren().add(h);
				
	}
	
	private void startFlashing(HBox hbox) {
        flashTimeline = new Timeline(
                new KeyFrame(FLASH_DURATION, event -> toggleFlashColor(hbox))
        );
        
        //chay vinh vien
        flashTimeline.setCycleCount(Animation.INDEFINITE);
        
        flashTimeline.play();
    }
	
    private void startStopTimer(HBox h) {
        stopTimeline = new Timeline(
                new KeyFrame(STOP_DURATION, event -> stopFlashing(h))
        );
        stopTimeline.setCycleCount(1);
        stopTimeline.play();
    }

	
	 private void toggleFlashColor(HBox hbox) {
	        Color currentColor = hbox.getBackground() != null ? (Color) hbox.getBackground().getFills().get(0).getFill() : Color.TRANSPARENT;
	        
	        Color flashColor = currentColor.equals(Color.RED) ? Color.WHITE : Color.RED;
	        hbox.setStyle("-fx-background-color: " + toHex(flashColor) + ";");
	  }

	   private void stopFlashing(HBox hbox) {
	        if (flashTimeline != null) {
	            flashTimeline.stop();
	            flashTimeline = null;
	        }
	        hbox.setStyle("-fx-background-color: #ffffff;");
	    }
	    
	    private String toHex(Color color) {
	        return String.format("#%02X%02X%02X",
	                (int) (color.getRed() * 255),
	                (int) (color.getGreen() * 255),
	                (int) (color.getBlue() * 255));
	    }
	
	public ArrayList<String> searchIboxByName(String searchName, ArrayList<String> inboxF) {
		ArrayList<String> resultList = new ArrayList<>();

		// sắp xếp theo thứ tự từ điển
		Collections.sort(inboxF);

		int n = inboxF.size();
		int step = (int) Math.floor(Math.sqrt(n));
		int prev = 0;

		// so sánh nếu vị trí cuối cùng của đoạn lớn hơn searchName thì bắt đầu tìm kiếm
		// cụ thể (loại bỏ các đoạn nhỏ hơn searchName)
		while (inboxF.get(Math.min(step, n) - 1).compareTo(searchName) < 0) {
			prev = step;
			step += (int) Math.floor(Math.sqrt(n));
			if (prev >= n)
				return resultList;
		}

		// Tìm vị trí đầu tiên lớn hơn hoặc bằng searchName trong phạm vi đã xác định.
		while (inboxF.get(prev).compareTo(searchName) < 0) {
			prev++;
			if (prev == Math.min(step, n))
				return resultList;
		}

		// tim kiếm cụ thể (Tìm các vị trí có giá trị là searchName trong đoạn đã xác
		// định)
		while (inboxF.get(prev).equals(searchName)) {
			resultList.add(inboxF.get(prev));
			prev++;
			if (prev == Math.min(step, n))
				return resultList;
		}

		// Kiểm tra các phần tử trước prev trong danh sách inboxF
		while (prev > 0 && inboxF.get(prev - 1).equals(searchName)) {
			prev--;
			resultList.add(inboxF.get(prev));
		}

		return resultList;
	}

	public void showMessageView() {
//		clientBorderPane.getCenter().setVisible(false);
//		clientBorderPane.getCenter();
		
		this.friend_ib.setVisible(true);
		this.chat_view.setVisible(true);
		this.friend_book.setVisible(false);
		this.friend_list_view.setVisible(false);
		
	}

	//danh ba ban be
	public void showfriendBookView() {
		this.friend_ib.setVisible(false);
		this.chat_view.setVisible(false);
		this.friend_book.setVisible(true);
		this.friend_list_view.setVisible(true);
		
		
	}
	
	private void sendgetfriendlistrequest() {
		// TODO Auto-generated method stub
		if(getListFriend==false) {
			Request rq = new GetFriendList(RequestType.GET_FRIEND_LIST, null);
			try {
				ClientModel.getInstance().getClient().getOut().writeObject(rq);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Gui doi tuong get friend loi");
				e.printStackTrace();
			}
			getListFriend =  true;
		}
	}
	
	//add danh sach ban be vao danh ba ban be
	public void addFriendToListView(Map<String, String> friendList) {
		
//		Label a = new Label("TONG CONG MINH");
//		this.friend_list_view_vbox.getChildren().add(a);
//		System.out.println(friendList.get(0));
//		for(String friend : friendList) {
//			System.out.println(friend);
//			Label l = new Label(friend);
//			this.friend_list_view_vbox.getChildren().add(l);
//		}
		this.friendList = friendList;
		
		for(Map.Entry<String, String> entry: friendList.entrySet()) {
			
            System.out.println(entry.getKey() + ": " + entry.getValue());
			
			HBox h = null;
			try {
				FriendBoxViewController friendBoxView = new FriendBoxViewController();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/guiobjectjson/friendBoxView.fxml"));
				loader.setController(friendBoxView);
				h = loader.load();
				friendBoxView.setUserID(entry.getKey());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// lay con vbox ra tu hbox

			Label friendNameLabel = (Label)h.getChildren().get(1);
			friendNameLabel.setText(entry.getValue());

			friend_list_view_vbox.getChildren().add(h);
		}
		
	}
	
	//DOI VBOX
	//VOI MOI USER SE CO MOT MESSAGE VIEW KHAC NHAU
	//KHI BAM VAO USER TREN FRIEND IN BOX -> MO VBOX TUONG UNG GAN VAO MESSAGE SCROLL PANE
	public void resetVBox(VBox messageVbox) {
		this.message_view_sp.setContent(messageVbox);
	}
	
	
	public void setOriginVBox(VBox originVBox) {
		this.originVBox = originVBox;
	}
	
	public void setSendToUserID(String sendToUserID) {
		this.sendToUserID = sendToUserID;
	}
	public String getSendToUserID() {
		return sendToUserID;
	}
}
