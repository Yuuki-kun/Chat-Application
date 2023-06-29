package application.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientController implements Initializable {

	@FXML
	private BorderPane clientBorderPane;

	@FXML
	private ScrollPane messScrollPane;

	@FXML
	private VBox messVBox;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		messVBox.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				messScrollPane.setVvalue((Double) arg2);
			}
		});

	}

	//add inbox box vao friend inbox view
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

	public ArrayList<String> searchIboxByName(String searchName, ArrayList<String> inboxF){
		ArrayList<String> resultList = new ArrayList<>();
		return resultList;
	}
	
	public void hide() {
		clientBorderPane.getCenter().setVisible(false);
		
	}

	public void show() {
		clientBorderPane.getCenter().setVisible(true);

	}

}
