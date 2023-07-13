package application.controller;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.jfoenix.controls.JFXButton;

import application.FileData;
import application.models.ClientModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import request.AudioRequest;
import request.GetFriendList;
import request.GetSearchList;
import request.Message;
import request.Request;
import request.RequestType;
import request.SendMessage;
import request.SendMessageStatus;
import request.TypingRequest;
import request.VideoRequest;

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

	@FXML
	private TextField searchFriendTf;

	@FXML
	private Button addFriendBtn;

	@FXML
	private Button addGroupBtn;

	@FXML
	private AnchorPane addFriendAnchor;

	@FXML
	private ScrollPane addFriendSP;

	@FXML
	private VBox addFriendViewVBox;

	@FXML
	private ScrollPane friendRequestSP;

	@FXML
	private VBox friendRequestVB;

	@FXML
	private AnchorPane friendRequestView;

	@FXML
	private JFXButton friendRequestBtn;

	@FXML
	private Label typing_lbl;

	@FXML
	private Button sendVideoBtn;

	private String sendToUserID;

	private String sendToName;

	private VBox originVBox;

	private String clientID;

	private String clientName;

	private HBox currentHboxNewIb;

	@FXML
	private Button picture_btn;

	@FXML
	private Button imoji_btn;

	@FXML
	private Button sticker_btn;

	@FXML
	private Button sendAudio_btn;
	@FXML
	private Button audioRecord_btn;

	
	private static final long DOUBLE_CLICK_TIME_DELTA = 300;
	private long lastClickTime = 0;

	private static final Duration FLASH_DURATION = Duration.seconds(0.5);
	private static final Duration STOP_DURATION = Duration.seconds(5);

	private Timeline flashTimeline;
	private Timeline stopTimeline;

	private ArrayList<ClientBoxController> clientInBoxFriend = new ArrayList<>();

	// Map chua danh danh ban be -> hien tai app chi cho phep nhan tin qua lai giua
	// ban be voi nhau
	private Map<String, String> friendList = new HashMap<>();

	private boolean getListFriend = false;
	boolean typing = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.originVBox = new VBox();
		this.friend_ib.setVisible(true);
		this.chat_view.setVisible(true);
		this.friend_book.setVisible(false);
		this.friend_list_view.setVisible(false);

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

		addFriendBtn.setOnAction(event -> addFriend());

		friendRequestBtn.setOnAction(event -> showFriendRequestView());

		// listening message tf
		message_tf.textProperty().addListener((obsv, oldv, newv) -> {
			// textfield dang duoc nhap
			// thong bao cho server biet la dang nhap

			if (this.typing == false) {
				System.out.println("SEND");
				String mess = message_tf.getText();

				Request typing = new TypingRequest(RequestType.TYPING, clientID, sendToUserID, 3);

				try {
					ClientModel.getInstance().getClient().getOut().writeObject(typing);
					this.typing = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
				pauseTransition.setOnFinished(event -> {
					this.typing = false;
				});

				pauseTransition.play();
			}

		});

		sendVideoBtn.setOnAction(event -> chooseVideo((Stage) sendVideoBtn.getScene().getWindow()));

		sendAudio_btn.setOnAction(event -> sendAudioFile());

		audioRecord_btn.setOnAction(event -> {System.out.println("REC");
									ClientModel.getInstance().getViewFactory().showAudioRecording();	
		});
//		sendgetfriendlistrequest();
	}	
	// send mp3 or wav to server
	public void sendAudioFile() {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			try {
				byte[] fileData = readBytesFromFile(selectedFile);
				
				Request sendAudio = new AudioRequest(RequestType.SEND_AUDIO, sendToUserID, selectedFile.getName(), fileData);
				// write fileDataObject

				ClientModel.getInstance().getClient().getOut().writeObject(sendAudio);

				//display
				if (originVBox.getChildren().size() >= 2) {
					if (originVBox.getChildren().get(originVBox.getChildren().size() - 2) instanceof HBox) {
						// xoa 1 cai
						System.out.println("Xoa 1 cai label time cua ben kia");
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					} else {
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					}
				}
				
				HBox messbox = new HBox();

				messbox.setAlignment(Pos.TOP_RIGHT);
				messbox.setPadding(new Insets(5, 5, 5, 10));
				
				Image playImage = new Image(getClass().getResourceAsStream("/application/resources/images/playbtnPng.png"));
				ImageView imageViewPlay = new ImageView();
				imageViewPlay.setFitWidth(45);
				imageViewPlay.setFitHeight(45);
				imageViewPlay.setStyle("-fx-margin: 25;");
				imageViewPlay.setImage(playImage);
				
				
				Media media = new Media(selectedFile.toURI().toString());
				MediaPlayer mediaPlayer = new MediaPlayer(media);
				MediaView mdav = new MediaView();
				mdav.setMediaPlayer(mediaPlayer);

				VBox audioVbox = new VBox();
				audioVbox.setAlignment(Pos.CENTER);
				audioVbox.setMaxWidth(250);
				audioVbox.setMaxHeight(100);
				
				Slider audioSlier = new Slider();
				audioSlier.setPadding(new Insets(35, 5, 5, 5));
				
				audioVbox.setStyle("-fx-background-color: #a9bad9;"
							+ "-fx-background-radius: 10;"
							+ "-fx-border-radius:10;"
							+ "-fx-border-color:#ffffff;");
				
				audioVbox.getChildren().addAll(imageViewPlay, audioSlier);
				
				

				messbox.getChildren().addAll(mdav, audioVbox);

				try {
					addNewMessage(sendToUserID, "audio", "time", false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// remove last child

				if (originVBox.getChildren().size() >= 2) {
					if (originVBox.getChildren().get(originVBox.getChildren().size() - 2) instanceof HBox) {
						// xoa 1 cai
						System.out.println("Xoa 1 cai label time cua ben kia");
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					} else {
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					}
				}


				originVBox.getChildren().add(messbox);


				imageViewPlay.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent evt) {
//						MediaView mdav = (MediaView) evt.getSource();

						if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
							mediaPlayer.pause();
						} else {
							mediaPlayer.play();
						}

						if (evt.getButton() == MouseButton.PRIMARY) {
							long clickTime = System.currentTimeMillis();
							if (clickTime - lastClickTime <= DOUBLE_CLICK_TIME_DELTA) {
								// Xử lý double click
//				                		Thread t = new Thread(new Runnable() {
//											public void run() {
								Platform.runLater(() -> ClientModel.getInstance().getViewFactory().showPlayVideoWindow(mdav, (Stage) mdav.getScene().getWindow()));
//											}
//										});
//				                		t.start();

							}
							lastClickTime = clickTime;
						}

					}
				});
				
				sendMessageStatus = new Label();
				timeSendMessage = new Label();

				sendMessageStatus.setMinWidth(55.5);
				timeSendMessage.setMinWidth(50.0);

				sendMessageStatus.setAlignment(Pos.BASELINE_RIGHT);
				timeSendMessage.setAlignment(Pos.BASELINE_RIGHT);

				sendMessageStatus.setStyle("-fx-background-color:grey;"
						+ "-fx-text-fill:white;"
						+ "-fx-background-radius: 10;"
						+ "-fx-padding: 0 10 0 10;");

				sendMessageStatus.setText("✓ sent");

				timeSendMessage.setText("time");

				originVBox.setAlignment(Pos.BASELINE_RIGHT);
				originVBox.getChildren().add(timeSendMessage);
				originVBox.getChildren().add(sendMessageStatus);
				
				
				System.out.println("File sent successfully.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendAudioFileWithFile(File selectedFile) {
		if (selectedFile != null) {
			try {
				byte[] fileData = readBytesFromFile(selectedFile);
				
				Request sendAudio = new AudioRequest(RequestType.SEND_AUDIO, sendToUserID, selectedFile.getName(), fileData);
				// write fileDataObject

				ClientModel.getInstance().getClient().getOut().writeObject(sendAudio);

				//display
				if (originVBox.getChildren().size() >= 2) {
					if (originVBox.getChildren().get(originVBox.getChildren().size() - 2) instanceof HBox) {
						// xoa 1 cai
						System.out.println("Xoa 1 cai label time cua ben kia");
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					} else {
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					}
				}
				
				HBox messbox = new HBox();

				messbox.setAlignment(Pos.TOP_RIGHT);
				messbox.setPadding(new Insets(5, 5, 5, 10));
				
				Image playImage = new Image(getClass().getResourceAsStream("/application/resources/images/playbtnPng.png"));
				ImageView imageViewPlay = new ImageView();
				imageViewPlay.setFitWidth(45);
				imageViewPlay.setFitHeight(45);
				imageViewPlay.setStyle("-fx-margin: 25;");
				imageViewPlay.setImage(playImage);
				
				
				Media media = new Media(selectedFile.toURI().toString());
				MediaPlayer mediaPlayer = new MediaPlayer(media);
				MediaView mdav = new MediaView();
				mdav.setMediaPlayer(mediaPlayer);

				VBox audioVbox = new VBox();
				audioVbox.setAlignment(Pos.CENTER);
				audioVbox.setMaxWidth(250);
				audioVbox.setMaxHeight(100);
				
				Slider audioSlier = new Slider();
				audioSlier.setPadding(new Insets(35, 5, 5, 5));
				
				audioVbox.setStyle("-fx-background-color: #a9bad9;"
							+ "-fx-background-radius: 10;"
							+ "-fx-border-radius:10;"
							+ "-fx-border-color:#ffffff;");
				
				audioVbox.getChildren().addAll(imageViewPlay, audioSlier);
				
				

				messbox.getChildren().addAll(mdav, audioVbox);

				try {
					addNewMessage(sendToUserID, "audio", "time", false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// remove last child

				if (originVBox.getChildren().size() >= 2) {
					if (originVBox.getChildren().get(originVBox.getChildren().size() - 2) instanceof HBox) {
						// xoa 1 cai
						System.out.println("Xoa 1 cai label time cua ben kia");
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					} else {
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					}
				}


				originVBox.getChildren().add(messbox);


				imageViewPlay.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent evt) {
//						MediaView mdav = (MediaView) evt.getSource();

						if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
							mediaPlayer.pause();
						} else {
							mediaPlayer.play();
						}

						if (evt.getButton() == MouseButton.PRIMARY) {
							long clickTime = System.currentTimeMillis();
							if (clickTime - lastClickTime <= DOUBLE_CLICK_TIME_DELTA) {
								// Xử lý double click
//				                		Thread t = new Thread(new Runnable() {
//											public void run() {
								Platform.runLater(() -> ClientModel.getInstance().getViewFactory().showPlayVideoWindow(mdav, (Stage) mdav.getScene().getWindow()));
//											}
//										});
//				                		t.start();

							}
							lastClickTime = clickTime;
						}

					}
				});
				
				sendMessageStatus = new Label();
				timeSendMessage = new Label();

				sendMessageStatus.setMinWidth(55.5);
				timeSendMessage.setMinWidth(50.0);

				sendMessageStatus.setAlignment(Pos.BASELINE_RIGHT);
				timeSendMessage.setAlignment(Pos.BASELINE_RIGHT);

				sendMessageStatus.setStyle("-fx-background-color:grey;"
						+ "-fx-text-fill:white;"
						+ "-fx-background-radius: 10;"
						+ "-fx-padding: 0 10 0 10;");

				sendMessageStatus.setText("✓ sent");

				timeSendMessage.setText("time");

				originVBox.setAlignment(Pos.BASELINE_RIGHT);
				originVBox.getChildren().add(timeSendMessage);
				originVBox.getChildren().add(sendMessageStatus);
				
				
				System.out.println("File sent successfully.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] readBytesFromFile(File file) throws IOException {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			try (FileInputStream inputStream = new FileInputStream(file)) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
			}
			return outputStream.toByteArray();
		}
	}

	public void chooseVideo(Stage primaryStage) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Video files (*.mp4)", "*.mp4");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Select Video");

		// Chọn video từ máy tính
		Stage fileStage = new Stage();
		fileStage.initOwner(primaryStage);
		fileStage.setTitle("File Selection");

		File selectedFile = fileChooser.showOpenDialog(fileStage);
		if (selectedFile != null) {
			sendVideo(selectedFile, selectedFile.getName(), selectedFile.getPath());
		} else {
		}
	}

	private void sendVideo(File selectedFile, String fileName, String videoPath) {
		try {

//            FileInputStream fis = new FileInputStream(videoPath);
			FileInputStream fis = new FileInputStream(selectedFile);

			// thong bao sap nhan video cho server

			byte[] videoData = fis.readAllBytes();

			//////////

			// add message text to chat area
			
				if (originVBox.getChildren().size() >= 2) {
					if (originVBox.getChildren().get(originVBox.getChildren().size() - 2) instanceof HBox) {
						// xoa 1 cai
						System.out.println("Xoa 1 cai label time cua ben kia");
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					} else {
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
						originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					}
				}

				//

				HBox messbox = new HBox();

				messbox.setAlignment(Pos.TOP_RIGHT);
				messbox.setPadding(new Insets(5, 5, 5, 10));

				// add media view
				Media media = new Media(selectedFile.toURI().toString());
				MediaPlayer mediaPlayer = new MediaPlayer(media);
				MediaView mdav = new MediaView();
				mdav.setMediaPlayer(mediaPlayer);
				mdav.setStyle("-fx-background-radius:10;");

				mdav.setFitHeight(270);
				mdav.setFitWidth(350);

				mdav.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent evt) {
						MediaView mdav = (MediaView) evt.getSource();

						if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
							mediaPlayer.pause();
						} else {
							mediaPlayer.play();
						}

						if (evt.getButton() == MouseButton.PRIMARY) {
							long clickTime = System.currentTimeMillis();
							if (clickTime - lastClickTime <= DOUBLE_CLICK_TIME_DELTA) {
								// Xử lý double click
//    		                		Thread t = new Thread(new Runnable() {
//    									public void run() {
								Platform.runLater(() -> ClientModel.getInstance().getViewFactory().showPlayVideoWindow(mdav, (Stage) mdav.getScene().getWindow()));
//    									}
//    								});
//    		                		t.start();

							}
							lastClickTime = clickTime;
						}

					}
				});

				//

//    			textFlow.setPrefSize(350, 50);
				messbox.getChildren().add(mdav);
				///
				String timeSend;

				Request videorq = new VideoRequest(RequestType.SEND_VIDEO, sendToUserID, videoData, fileName);
				ClientModel.getInstance().getClient().getOut().writeObject(videorq);

				timeSend = ((VideoRequest) videorq).getTimeSend();

				///
				addNewMessage(sendToUserID, "video", timeSend, false);

				originVBox.getChildren().add(messbox);

				sendMessageStatus = new Label();
				timeSendMessage = new Label();

				sendMessageStatus.setMinWidth(55.5);
				timeSendMessage.setMinWidth(50.0);

				sendMessageStatus.setAlignment(Pos.BASELINE_RIGHT);
				timeSendMessage.setAlignment(Pos.BASELINE_RIGHT);

				sendMessageStatus.setStyle("-fx-background-color:grey;"
						+ "-fx-text-fill:white;"
						+ "-fx-background-radius: 10;"
						+ "-fx-padding: 0 10 0 10;");

				sendMessageStatus.setText("✓ sent");

				timeSendMessage.setText(timeSend);

				originVBox.setAlignment(Pos.BASELINE_RIGHT);
				originVBox.getChildren().add(timeSendMessage);
				originVBox.getChildren().add(sendMessageStatus);

			

			/////////

			fis.close();
//            dos.close();
//            socket.close();
			System.out.println("Video sent to server.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(("Error sending video."));
		}
	}

	public void playTypingMessage(int dur) {

		this.typing_lbl.setText("Typinggg ...");
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(dur), typing_lbl);
		translateTransition.setFromY(5);
		translateTransition.setToY(-5);
		translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
		translateTransition.setAutoReverse(true);
		translateTransition.setInterpolator(Interpolator.SPLINE(0.4, 0.1, 0.2, 0.9));
		translateTransition.play();

		PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
		pauseTransition.setOnFinished(event -> {
			translateTransition.stop();
			this.typing_lbl.setText("");

		});

		pauseTransition.play();

	}

	private Label sendMessageStatus, timeSendMessage, timeReceiveMessage;

	public Label getSendMessageStatus() {
		return sendMessageStatus;
	}

	public void sendMessage(String sendToUserID) throws IOException {

		String messToSend = this.message_tf.getText();

		// add message text to chat area
		System.out.println("chat = " + messToSend);
		if (messToSend != null) {

			// remove status label && time label
			// neu tin nhan truoc do la cua minh -> co 2 label time and status
			// neu tin nhan truoc do la cua ban be -> co 1 label time

			if (originVBox.getChildren().size() >= 2) {
				if (originVBox.getChildren().get(originVBox.getChildren().size() - 2) instanceof HBox) {
					// xoa 1 cai
					System.out.println("Xoa 1 cai label time cua ben kia");
					originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
				} else {
					originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
					originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
				}
			}

			//

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

			sendMessageStatus = new Label();
			timeSendMessage = new Label();

			sendMessageStatus.setMinWidth(55.5);
			timeSendMessage.setMinWidth(50.0);

			sendMessageStatus.setAlignment(Pos.BASELINE_RIGHT);
			timeSendMessage.setAlignment(Pos.BASELINE_RIGHT);

			sendMessageStatus.setStyle("-fx-background-color:grey;"
					+ "-fx-text-fill:white;"
					+ "-fx-background-radius: 10;"
					+ "-fx-padding: 0 10 0 10;");

			sendMessageStatus.setText("✓ sent");

			String t = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
			timeSendMessage.setText(t);

			originVBox.setAlignment(Pos.BASELINE_RIGHT);
			originVBox.getChildren().add(timeSendMessage);
			originVBox.getChildren().add(sendMessageStatus);

		}
	}

	// sau khi nhan message tu user -> hien thi vao chat view
	public void displayReceiveMessage(String receiveFromID, String message, String timeReceive) throws IOException {
		HBox receiveMess = new HBox();
		receiveMess.setAlignment(Pos.TOP_LEFT);
		receiveMess.setPadding(new Insets(5, 5, 5, 10));

		ImageView imageView = new ImageView();
		imageView.setFitWidth(25);
		imageView.setFitHeight(25);
		Image image = new Image(getClass().getResourceAsStream("/application/resources/images/appchatIcon.png"));
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

		timeReceiveMessage = new Label();
//		timeReceiveMessage.setStyle("-fx-background-color:red;");

		timeReceiveMessage.setPadding(new Insets(0, 0, 0, 60));

		timeReceiveMessage.setAlignment(Pos.BASELINE_LEFT);

		timeReceiveMessage.setText(timeReceive);

		receiveMess.getChildren().addAll(imageView, textFlow);

		addNewMessage(receiveFromID, message, timeReceive, true);

		// remove last child

//		if (originVBox.getChildren().size() > 1) {
//			originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
//		}

		if (originVBox.getChildren().size() >= 2) {
			if (originVBox.getChildren().get(originVBox.getChildren().size() - 2) instanceof HBox) {
				// xoa 1 cai
				System.out.println("Xoa 1 cai label time cua ben kia");
				originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
			} else {

				System.out.println("Xoa 2 cai label");
				originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
				originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
			}
		}
		timeReceiveMessage.setMaxWidth(message_view_sp.getWidth());

		originVBox.getChildren().add(receiveMess);
		originVBox.getChildren().add(timeReceiveMessage);

		// neu chua bam vao xem (clicked) thi se chop chop do do
	}

	public void displayReceiveVideo(String receiveFromID, String videoURL, String timeReceive) throws IOException {
		HBox receiveVideo = new HBox();
		receiveVideo.setAlignment(Pos.TOP_LEFT);
		receiveVideo.setPadding(new Insets(5, 5, 5, 10));

		ImageView imageView = new ImageView();
		imageView.setFitWidth(25);
		imageView.setFitHeight(25);
		Image image = new Image(getClass().getResourceAsStream("/application/resources/images/appchatIcon.png"));
		imageView.setImage(image);

		File file = new File(videoURL);
		Media media = new Media(file.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		MediaView mdav = new MediaView();
		mdav.setMediaPlayer(mediaPlayer);
		mdav.setStyle("-fx-background-radius:10;");

		timeReceiveMessage = new Label();

		timeReceiveMessage.setMinWidth(110.0);

		timeReceiveMessage.setPadding(new Insets(0, 0, 0, 60));

		timeReceiveMessage.setAlignment(Pos.BASELINE_LEFT);

		timeReceiveMessage.setText(timeReceive);

		receiveVideo.getChildren().addAll(imageView, mdav, timeReceiveMessage);

		addNewMessage(receiveFromID, "video", timeReceive, true);

		// remove last child

		if (originVBox.getChildren().size() >= 2) {
			if (originVBox.getChildren().get(originVBox.getChildren().size() - 2) instanceof HBox) {
				// xoa 1 cai
				System.out.println("Xoa 1 cai label time cua ben kia");
				originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
			} else {
				originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
				originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
			}
		}

		timeReceiveMessage.setMaxWidth(message_view_sp.getWidth());

		originVBox.getChildren().addAll(receiveVideo, timeReceiveMessage);

		mediaPlayer.setOnReady(() -> {
			mdav.setFitHeight(270);
			mdav.setFitWidth(350);
		});

		mdav.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent evt) {
				MediaView mdav = (MediaView) evt.getSource();

				if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
					mediaPlayer.pause();
				} else {
					mediaPlayer.play();
				}

				if (evt.getButton() == MouseButton.PRIMARY) {
					long clickTime = System.currentTimeMillis();
					if (clickTime - lastClickTime <= DOUBLE_CLICK_TIME_DELTA) {
						// Xử lý double click
//		                		Thread t = new Thread(new Runnable() {
//									public void run() {
						Platform.runLater(() -> ClientModel.getInstance().getViewFactory().showPlayVideoWindow(mdav, (Stage) mdav.getScene().getWindow()));
//									}
//								});
//		                		t.start();

					}
					lastClickTime = clickTime;
				}

			}
		});

		// neu chua bam vao xem (clicked) thi se chop chop do do
	}

	public void displayReceiveAudio(String receiveFromID, String audioURL, String timeReceive) {
		HBox receiveAudio = new HBox();
		receiveAudio.setAlignment(Pos.TOP_LEFT);
		receiveAudio.setPadding(new Insets(5, 5, 5, 10));
		
		ImageView imageView = new ImageView();
		imageView.setFitWidth(25);
		imageView.setFitHeight(25);
		Image image = new Image(getClass().getResourceAsStream("/application/resources/images/appchatIcon.png"));
		imageView.setImage(image);

		Image playImage = new Image(getClass().getResourceAsStream("/application/resources/images/playbtnPng.png"));
		ImageView imageViewPlay = new ImageView();
		imageViewPlay.setFitWidth(45);
		imageViewPlay.setFitHeight(45);
		imageViewPlay.setStyle("-fx-margin: 25;");
		imageViewPlay.setImage(playImage);
		
		
		File file = new File(audioURL);
		Media media = new Media(file.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		MediaView mdav = new MediaView();
		mdav.setMediaPlayer(mediaPlayer);
//		mdav.setFitHeight(100);
//		mdav.setFitWidth(100);
		
		VBox audioVbox = new VBox();
		audioVbox.setAlignment(Pos.CENTER);
		audioVbox.setMaxWidth(250);
		audioVbox.setMaxHeight(100);
		
		Slider audioSlier = new Slider();
		audioSlier.setPadding(new Insets(35, 5, 5, 5));
		
		audioVbox.setStyle("-fx-background-color: #a9bad9;"
					+ "-fx-background-radius: 10;"
					+ "-fx-border-radius:10;"
					+ "-fx-border-color:#ffffff;");
		
		audioVbox.getChildren().addAll(imageViewPlay, audioSlier);
		
		timeReceiveMessage = new Label();

		timeReceiveMessage.setMinWidth(110.0);

		timeReceiveMessage.setPadding(new Insets(0, 0, 0, 60));

		timeReceiveMessage.setAlignment(Pos.BASELINE_LEFT);

		timeReceiveMessage.setText(timeReceive);

		receiveAudio.getChildren().addAll(imageView, mdav, audioVbox);

		try {
			addNewMessage(receiveFromID, "audio", timeReceive, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// remove last child

		if (originVBox.getChildren().size() >= 2) {
			if (originVBox.getChildren().get(originVBox.getChildren().size() - 2) instanceof HBox) {
				// xoa 1 cai
				System.out.println("Xoa 1 cai label time cua ben kia");
				originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
			} else {
				originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
				originVBox.getChildren().remove(originVBox.getChildren().size() - 1);
			}
		}

		timeReceiveMessage.setMaxWidth(message_view_sp.getWidth());

		originVBox.getChildren().addAll(receiveAudio, timeReceiveMessage);


		imageViewPlay.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent evt) {
//				MediaView mdav = (MediaView) evt.getSource();

				if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
					mediaPlayer.pause();
				} else {
					mediaPlayer.play();
				}

				if (evt.getButton() == MouseButton.PRIMARY) {
					long clickTime = System.currentTimeMillis();
					if (clickTime - lastClickTime <= DOUBLE_CLICK_TIME_DELTA) {
						// Xử lý double click
//		                		Thread t = new Thread(new Runnable() {
//									public void run() {
						Platform.runLater(() -> ClientModel.getInstance().getViewFactory().showPlayVideoWindow(mdav, (Stage) mdav.getScene().getWindow()));
//									}
//								});
//		                		t.start();

					}
					lastClickTime = clickTime;
				}

			}
		});
	}
	
	public String writeSendMessageRequest(String toUserId, String message) throws IOException {
		String timeSend;
		Request sendMessageRequest = new SendMessage(RequestType.SEND_MESSAGE, message, toUserId);
		timeSend = ((SendMessage) sendMessageRequest).getTimeSend();
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
					System.out.println("userIDVBOX = " + userID);
					had = true;
					setIn4InboxHBox(clientbox.getFriend_box_hbox(), clientbox.getClientname(), message, time);

					resetVBox(clientbox.getChatVBox());
					if (isReceive) {
						stopFlashing(clientbox.getFriend_box_hbox());
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

			// set thong tin cho hbox
			setIn4InboxHBox(h, name, message, time);

			if (isReceive) {
//				stopFlashing(currentHboxNewIb);
				startFlashing(h);
				startStopTimer(h);
			}
			messVBox.getChildren().add(h);
//			this.currentHboxNewIb = h;
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

	public void showMessageView() {
//		clientBorderPane.getCenter().setVisible(false);
//		clientBorderPane.getCenter();

		this.friend_ib.setVisible(true);
		this.chat_view.setVisible(true);
		this.friend_book.setVisible(false);
		this.friend_list_view.setVisible(false);
		this.friendRequestView.setVisible(false);

	}

	// danh ba ban be
	public void showfriendBookView() {
		this.friend_ib.setVisible(false);
		this.chat_view.setVisible(false);
		this.friend_book.setVisible(true);
		this.friend_list_view.setVisible(true);
		this.friendRequestView.setVisible(false);
		addFriendAnchor.setVisible(false);

	}

	public void showFriendRequestView() {
		this.friend_ib.setVisible(false);
		this.chat_view.setVisible(false);
		this.friend_book.setVisible(true);
		this.friend_list_view.setVisible(false);
		this.friendRequestView.setVisible(true);

	}

	public void sendgetfriendlistrequest() {
		// TODO Auto-generated method stub
		if (getListFriend == false) {
			Request rq = new GetFriendList(RequestType.GET_FRIEND_LIST, null, null);
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

	public ArrayList<FriendBoxViewController> friendListController = new ArrayList<>();
	public Map<String, Boolean> friendListStatus = new HashMap<>();

	// add danh sach ban be vao danh ba ban be
	public void addFriendToListView(Map<String, String> friendList, Map<String, Boolean> friendListStatus) throws IOException {

		if (!this.friendList.isEmpty()) {
			System.out.println("Clear vbox");
			Platform.runLater(() -> friend_list_view_vbox.getChildren().clear());
		}

		this.friendList = friendList;
		this.friendListStatus = friendListStatus;

		for (Map.Entry<String, String> entry : friendList.entrySet()) {

			System.out.println(entry.getKey() + ": " + entry.getValue());

			HBox h;

			FriendBoxViewController friendBoxView = new FriendBoxViewController();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/guiobjectjson/friendBoxView.fxml"));
			loader.setController(friendBoxView);
			h = loader.load();
			friendBoxView.setUserID(entry.getKey());

			if ((friendListStatus.get(entry.getKey())) == null) {
				friendBoxView.setIsOnline(false);
			} else {
				friendBoxView.setIsOnline(true);
			}

			friendListController.add(friendBoxView);

			// lay con vbox ra tu hbox

			Label friendNameLabel = (Label) h.getChildren().get(1);
			friendNameLabel.setText(entry.getValue());

			Platform.runLater(() -> friend_list_view_vbox.getChildren().add(h));
			System.out.println("Added to hbox");
		}

	}

	public void updateFriendOnline(String id, boolean status) {
		this.friendListStatus.put(id, true);
		for (FriendBoxViewController fbv : friendListController) {
			if (fbv.getUserID().equals(id)) {
				fbv.setIsOnline(status);
			}
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
		// set vbox cua id nay
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

	public void addFriend() {

		searchFriendTf.requestFocus();
		searchFriendTf.setOnKeyPressed(event -> {

			this.friend_ib.setVisible(false);
			this.friend_book.setVisible(false);

			this.friend_list_view.setVisible(false);
			this.chat_view.setVisible(false);

			this.addFriendAnchor.setVisible(true);

			String searchInput = searchFriendTf.getText();

			if (!(searchInput == null || searchInput == "")) {
				Request getSearchLiRequest = new GetSearchList(RequestType.GET_SEARCH_ADDFRIEND_LIST, searchInput);
				try {
					ClientModel.getInstance()
							.getClient()
							.getOut().writeObject(getSearchLiRequest);
				} catch (IOException e) {
					System.out.println("send search list request error!");
					e.printStackTrace();
				}
			}
		});
	}

	private ArrayList<HBox> addFriendResultSearchHBoxList = new ArrayList<>();
	private ArrayList<AddFriendBoxController> addFriendResultSearchControllerList = new ArrayList<>();
	private ArrayList<String> sentRequestID = new ArrayList<>();

	public ArrayList<String> getSentRequestID() {
		return sentRequestID;
	}

	public void addFriendResultSearch(Map<String, String> clientList) throws IOException {

		if (!addFriendResultSearchHBoxList.isEmpty()) {
			Platform.runLater(() -> addFriendViewVBox.getChildren().clear());
			addFriendResultSearchHBoxList.removeAll(addFriendResultSearchHBoxList);
			addFriendResultSearchControllerList.removeAll(addFriendResultSearchControllerList);
		}

//		addFriendViewVBox.getChildren().removeAll(clientInBoxFriend);

		for (Map.Entry<String, String> entry : clientList.entrySet()) {

//			for(AddFriendBoxController add : sentRequest) {
//				
//			}

			HBox h;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/fxml/AddFriend.fxml"));
			AddFriendBoxController addfriendboxcontroller = new AddFriendBoxController(entry.getKey(), entry.getValue());
			loader.setController(addfriendboxcontroller);
			h = loader.load();

//			addfriendboxcontroller.setName();

			// add vao list label
			System.out.println("NAMEEEE  =  " + entry.getValue());

			if (sentRequestID.contains(entry.getKey())) {
				addfriendboxcontroller.disableAddButton();
			}

			Platform.runLater(() -> addFriendViewVBox.getChildren().add(h));

			addFriendResultSearchHBoxList.add(h);
			addFriendResultSearchControllerList.add(addfriendboxcontroller);

			addfriendboxcontroller.setName();

		}
	}

	public void addFriendRequest(String id, String name) throws IOException {
		HBox h;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/fxml/AddFriendRequest.fxml"));
		FriendRequestController friendRequestController = new FriendRequestController(id, name);
		loader.setController(friendRequestController);
		h = loader.load();

//			addfriendboxcontroller.setName();

		// add vao list label
		System.out.println("NAMEEEE  =  " + name);
		Platform.runLater(() -> friendRequestVB.getChildren().add(h));
		friendRequestController.setName();

	}

	public void sendFriendRequest() {

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

	public void displayAcceptedFR(String name) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(name + " Accepted your request!");
		alert.setTitle("Response Friend Request");
		alert.initModality(Modality.WINDOW_MODAL);
		alert.initOwner((Stage) addFriendBtn.getScene().getWindow());
		alert.showAndWait();



		this.getListFriend = false;
		sendgetfriendlistrequest();
	}

}
