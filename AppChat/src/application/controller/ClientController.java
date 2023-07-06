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
    private AnchorPane chat_view;

    @FXML
    private BorderPane clientBorderPane;

    @FXML
    private Label clientNameLabel;

    @FXML
    private JFXButton friendBook_btn;

    @FXML
    private AnchorPane friend_book;

    @FXML
    private AnchorPane friend_ib;

    @FXML
    private JFXButton friend_list_btn;

    @FXML
    private AnchorPane friend_list_view;

    @FXML
    private ScrollPane friend_list_view_scroll;

    @FXML
    private VBox friend_list_view_vbox;

    @FXML
    private JFXButton group_btn;

    @FXML
    private JFXButton logout_btn;

    @FXML
    private ScrollPane messScrollPane;

    @FXML
    private VBox messVBox;

    @FXML
    private JFXButton message_btn;

    @FXML
    private TextField message_tf;

    @FXML
    private ScrollPane message_view_sp;

    @FXML
    private JFXButton profile_btn;

    @FXML
    private Button sendMessageBtn;

    @FXML
    private JFXButton setting_btn;

	private String sendToUserID;
	
	private String sendToName;

	private VBox originVBox;
	
	private String clientID;
	
	private String clientName;
	

	private static final Duration FLASH_DURATION = Duration.seconds(0.5);
	private static final Duration STOP_DURATION = Duration.seconds(5);

	private Timeline flashTimeline;
	private Timeline stopTimeline;

	private ArrayList<ClientBoxController> clientInBoxFriend = new ArrayList<>();

	// Map chua danh danh ban be -> hien tai app chi cho phep nhan tin qua lai giua
	// ban be voi nhau
	private Map<String, String> friendList;

	private boolean getListFriend = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.originVBox = new VBox();
		this.friend_ib.setVisible(true);
		this.chat_view.setVisible(true);
		this.friend_book.setVisible(false);
		this.friend_list_view.setVisible(false);
//		this.messScrollPane.setOnMouseClicked(event -> System.out.println("scroll pane clicked"));
//		this.messVBox.setOnMouseClicked(event -> System.out.println("vbox clicked"));
//		AnchorPane clientMenu = (AnchorPane)this.clientBorderPane.getLeft();
//		BorderPane clientMenuBorder = (BorderPane)clientMenu.getChildren().get(0);
//		VBox clientmenuvbox = (VBox)clientMenuBorder.getTop();
//		Label nameLabel = (Label)clientmenuvbox.getChildren().get(1);
//		nameLabel.setText(clientName);
		
		//getcontroller cua clientmenu

		
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
		
		message_btn.setOnAction(event -> showMessageView());
		friendBook_btn.setOnAction(event -> showfriendBookView());

		sendgetfriendlistrequest();
	}

	public void sendMessage(String sendToUserID) throws IOException {

		String messToSend = this.message_tf.getText();

		// add message text to chat area
		System.out.println("chat = " + messToSend);
		if (messToSend != null) {
			HBox messbox = new HBox();

			messbox.setAlignment(Pos.TOP_RIGHT);
			messbox.setPadding(new Insets(5, 5, 5, 10));

			Text text = new Text(messToSend);
			TextFlow textFlow = new TextFlow(text);
			textFlow.setTextAlignment(TextAlignment.JUSTIFY);

			textFlow.setStyle("-fx-color: rgb(239, 242, 255);" +
					"-fx-background-color: rgb(15, 125, 242);" +
					"-fx-background-radius: 20px;"
					+ "-fx-max-width: 330;"

			);
			textFlow.setPadding(new Insets(10, 15, 10, 15));
			text.setFill(Color.color(0.934, 0.945, 0.996));
//			textFlow.setPrefSize(350, 50);
			messbox.getChildren().addAll(textFlow);
			message_tf.clear();

			String timeSend = writeSendMessageRequest(sendToUserID, messToSend);
			addNewMessage(sendToUserID, messToSend, timeSend, false);
			
			
			
			originVBox.getChildren().add(messbox);

		}
	}

	// sau khi nhan message tu user -> hien thi vao chat view
	public void displayReceiveMessage(String receiveFromID, String message, String timeReceive) throws IOException {
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

		textFlow.setStyle("-fx-color: rgb(239, 242, 255);" +
				"-fx-background-color: rgb(15, 125, 242);" +
				"-fx-background-radius: 20px;"
				+ "-fx-max-width: 330;");
		textFlow.setPadding(new Insets(10, 15, 10, 15));
		text.setFill(Color.color(0.934, 0.945, 0.996));
		receiveMess.getChildren().addAll(imageView, textFlow);

		addNewMessage(receiveFromID, message, timeReceive, true);
		
		
		originVBox.getChildren().add(receiveMess);

		// neu chua bam vao xem (clicked) thi se chop chop do do

	}

	public String writeSendMessageRequest(String toUserId, String message) throws IOException {
		String timeSend;
		Request sendMessageRequest = new SendMessage(RequestType.SEND_MESSAGE, message, toUserId);
		timeSend = ((SendMessage)sendMessageRequest).getTimeSend();
		try {
			ClientModel.getInstance().getClient().getOut().writeObject(sendMessageRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Send message request to " + toUserId + " to server error");
			e.printStackTrace();
		}
		return timeSend;
		// sau khi gui tin nhan qua ben kia -> add vao new friend box view
	}

	// add inbox box vao friend inbox view
	public void addNewMessage(String userID, String message, String time, boolean isReceive) throws IOException {
	
		

		// tao mot arraylist luu cac clientboxcontroller lai
		// neu doi tuong inbox da co trong list -> khong them vao nua
		
		boolean had = false;
		
		if (!clientInBoxFriend.isEmpty()) {
			for (ClientBoxController clientbox : clientInBoxFriend) {
				System.out.println("client box = id = " + clientbox.getUserID());
				// found
				if (clientbox.getUserID().equals(userID)) {
					System.out.println("userIDVBOX = "+userID);
					had = true;
					setIn4InboxHBox(clientbox.getFriend_box_hbox(), clientbox.getClientname(), message, time);

					resetVBox(clientbox.getChatVBox());
					if (isReceive) {
						startFlashing(clientbox.getFriend_box_hbox());
						startStopTimer((clientbox.getFriend_box_hbox()));
					}
					break;
				}
			}
		}

		if (!had) {
			
			String name;
			if (userID.equals("server")) {
				name = "SERVER";
			} else {
				name = friendList.get(userID);

			}
			
			
			HBox h;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/guiobjectjson/testHBox.fxml"));
			h = loader.load();
			h.setOnMouseEntered(event -> {
				h.setStyle("-fx-background-color: #cccccc;");

			});
			h.setOnMouseExited(event -> {
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
					message_view_sp.setVvalue((Double) arg2);
				}
			});
			clientInBoxFriend.add(clientBoxController);
			// lay con vbox ra tu hbox
			
			//set thong tin cho hbox
			setIn4InboxHBox(h, name, message, time);
			
			if (isReceive) {
				startFlashing(h);
				startStopTimer(h);

			}
			messVBox.getChildren().add(h);
		}

	}

	private void startFlashing(HBox hbox) {
		flashTimeline = new Timeline(
				new KeyFrame(FLASH_DURATION, event -> toggleFlashColor(hbox)));

		// chay vinh vien
		flashTimeline.setCycleCount(Animation.INDEFINITE);

		flashTimeline.play();
	}

	private void startStopTimer(HBox h) {
		stopTimeline = new Timeline(
				new KeyFrame(STOP_DURATION, event -> stopFlashing(h)));
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

	// danh ba ban be
	public void showfriendBookView() {
		this.friend_ib.setVisible(false);
		this.chat_view.setVisible(false);
		this.friend_book.setVisible(true);
		this.friend_list_view.setVisible(true);

	}

	private void sendgetfriendlistrequest() {
		// TODO Auto-generated method stub
		if (getListFriend == false) {
			Request rq = new GetFriendList(RequestType.GET_FRIEND_LIST, null);
			try {
				ClientModel.getInstance().getClient().getOut().writeObject(rq);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Gui doi tuong get friend loi");
				e.printStackTrace();
			}
			getListFriend = true;
		}
	}

	// add danh sach ban be vao danh ba ban be
	public void addFriendToListView(Map<String, String> friendList) {

		this.friendList = friendList;

		for (Map.Entry<String, String> entry : friendList.entrySet()) {

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

			Label friendNameLabel = (Label) h.getChildren().get(1);
			friendNameLabel.setText(entry.getValue());

			friend_list_view_vbox.getChildren().add(h);
		}

	}

	// DOI VBOX
	// VOI MOI USER SE CO MOT MESSAGE VIEW KHAC NHAU
	// KHI BAM VAO USER TREN FRIEND IN BOX -> MO VBOX TUONG UNG GAN VAO MESSAGE
	// SCROLL PANE
	public void resetVBox(VBox messageVbox) {
		this.message_view_sp.setContent(messageVbox);
	}

	public void setOriginVBox(VBox originVBox) {
		this.originVBox = originVBox;
	}

	public void setSendToUserID(String sendToUserID) {
		this.sendToUserID = sendToUserID;
		//set vbox cua id nay
		if (!clientInBoxFriend.isEmpty()) {
			for (ClientBoxController clientbox : clientInBoxFriend) {
				// found
				if (clientbox.getUserID().equals(sendToUserID)) {
					setOriginVBox(clientbox.getChatVBox());
					resetVBox(clientbox.getChatVBox());
					break;
				}
			}
		}
	}

	public void setIn4InboxHBox(HBox h, String name, String message, String time) {
		VBox v = (VBox) h.getChildren().get(1);
		// lay con label ra tu vbox
		Label l = (Label) v.getChildren().get(0);
		l.setText(name);
		Label t = (Label) v.getChildren().get(1);
		t.setText(message);
		Label i = (Label) v.getChildren().get(2);
		i.setText(time);
	}
	
	public String getSendToUserID() {
		return sendToUserID;
	}
	
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientID() {
		return clientID;
	}
	public String getClientName() {
		return clientName;
	}
	
	public void setNameLabel() {
		this.clientNameLabel.setText(clientName);
	}
	
	public void setSendToName(String sendToName) {
		this.sendToName = sendToName;
	}
	

}
