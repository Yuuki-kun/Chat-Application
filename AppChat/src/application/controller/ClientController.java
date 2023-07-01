package application.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.models.ClientModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import request.GetFriendList;
import request.Request;
import request.RequestType;

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

	
	private boolean getListFriend =false;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		this.friend_ib.setVisible(true);
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

	}

	// add inbox box vao friend inbox view
	public void addNewMessage(String name, String message, String time) {

		HBox h = null;
		try {
			ClientBoxController clientBoxController = new ClientBoxController();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/guiobjectjson/testHBox.fxml"));
			loader.setController(clientBoxController);
			h = loader.load();
			System.out.println(clientBoxController.getHave());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// lay con vbox ra tu hbox
		VBox v = (VBox) h.getChildren().get(1);

		// lay con label ra tu vbox
		Label l = (Label) v.getChildren().get(0);
		l.setText(name);

		Label t = (Label) v.getChildren().get(1);
		t.setText(message);

		Label i = (Label) v.getChildren().get(2);

		i.setText(time);

		messVBox.getChildren().add(h);

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
		this.friend_book.setVisible(false);
		this.friend_list_view.setVisible(false);
		
	}

	public void showfriendBookView() {
		this.friend_ib.setVisible(false);
		this.friend_book.setVisible(true);
		this.friend_list_view.setVisible(true);
		
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
	
	public void addFriendToListView(ArrayList<String> friendList) {
		
//		Label a = new Label("TONG CONG MINH");
//		this.friend_list_view_vbox.getChildren().add(a);
//		System.out.println(friendList.get(0));
//		for(String friend : friendList) {
//			System.out.println(friend);
//			Label l = new Label(friend);
//			this.friend_list_view_vbox.getChildren().add(l);
//		}
		
		for(String friendName : friendList) {
			HBox h = null;
			try {
				FriendBoxViewController friendBoxView = new FriendBoxViewController();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/guiobjectjson/friendBoxView.fxml"));
				loader.setController(friendBoxView);
				h = loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// lay con vbox ra tu hbox

			Label friendNameLabel = (Label)h.getChildren().get(1);
			friendNameLabel.setText(friendName);

			friend_list_view_vbox.getChildren().add(h);
		}
		
	}
	
	

}
