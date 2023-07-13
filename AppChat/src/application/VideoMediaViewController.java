package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import com.jfoenix.controls.JFXSlider;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VideoMediaViewController implements Initializable {
	@FXML
	private Button buttonPPR;

	@FXML
	private HBox hboxControl;

	@FXML
	private HBox hboxVolume;

	@FXML
	private Label labelFullScreen;

	@FXML
	private Label labelCurrentTime;

	@FXML
	private Label labelSpeed;

	@FXML
	private Label labelTotalTime;

	@FXML
	private Label labelVolume;

	@FXML
	private MediaView mvVideo;
	private MediaPlayer mpVideo;
	private Media mediaVideo;

	@FXML
	private Slider sliderTime;

	@FXML
	private Slider sliderVolume;

	@FXML
	private VBox vboxParent;

	private boolean atTheEndOfVideo = false;
	private boolean isPlaying = true;
	private boolean isMuted = true;

	private ImageView ivPlay;
	private ImageView ivPause;
	private ImageView ivRestart;
	private ImageView ivVolume;
	private ImageView ivFullScreen;
	private ImageView ivMute;
	private ImageView ivExit;

	private MediaPlayer mediaPlayer;
	
	public VideoMediaViewController(MediaPlayer mediaPlayer) {
		this.mpVideo = mediaPlayer;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		final int IV_SIZE = 25;

//		mediaVideo = new Media(new File("src/application/ahri.mp4").toURI().toString());
//		mpVideo = new MediaPlayer(mediaVideo);
		mvVideo.setMediaPlayer(mpVideo);

		Image imagePlay = new Image(new File("src/application/play_icon_134504.png").toURI().toString());
		ivPlay = new ImageView(imagePlay);
		ivPlay.setFitHeight(IV_SIZE);
		ivPlay.setFitWidth(IV_SIZE);

		Image imageStop = new Image(new File("src/application/pause-btn.png").toURI().toString());
		ivPause = new ImageView(imageStop);
		ivPause.setFitHeight(IV_SIZE);
		ivPause.setFitWidth(IV_SIZE);

		Image imageRestart = new Image(new File("src/application/restart-btn.png").toURI().toString());
		ivRestart = new ImageView(imageRestart);
		ivRestart.setFitHeight(IV_SIZE);
		ivRestart.setFitWidth(IV_SIZE);

		Image imageVolume = new Image(new File("src/application/volume.png").toURI().toString());
		ivVolume = new ImageView(imageVolume);
		ivVolume.setFitHeight(IV_SIZE);
		ivVolume.setFitWidth(IV_SIZE);

		Image imageFullScreen = new Image(new File("src/application/fullscreen.png").toURI().toString());
		ivFullScreen = new ImageView(imageFullScreen);
		ivFullScreen.setFitHeight(IV_SIZE);
		ivFullScreen.setFitWidth(IV_SIZE);

		Image imageMute = new Image(new File("src/application/mute.png").toURI().toString());
		ivMute = new ImageView(imageMute);
		ivMute.setFitHeight(IV_SIZE);
		ivMute.setFitWidth(IV_SIZE);

		Image imageExit = new Image(new File("src/application/exitscreen.png").toURI().toString());
		ivExit = new ImageView(imageExit);
		ivExit.setFitHeight(IV_SIZE);
		ivExit.setFitWidth(IV_SIZE);

		buttonPPR.setGraphic(ivPause);
		labelVolume.setGraphic(ivVolume);
		labelSpeed.setText("1X");
		labelFullScreen.setGraphic(ivFullScreen);

		buttonPPR.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent ae) {
				Button buttonPlay = (Button) ae.getSource();
				if (atTheEndOfVideo) {
					sliderTime.setValue(0);
					atTheEndOfVideo = false;
					isPlaying = false;
				}

				if (isPlaying) {
					buttonPlay.setGraphic(ivPlay);
					mpVideo.pause();
					isPlaying = false;
				} else {
					buttonPlay.setGraphic(ivPause);
					mpVideo.play();
					isPlaying = true;
				}
			}
		});

		hboxVolume.getChildren().remove(sliderVolume);

		// sync the volume property
		mpVideo.volumeProperty().bindBidirectional(sliderVolume.valueProperty());

		bindCurrentTimeLabel();

		sliderVolume.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				mpVideo.setVolume(sliderVolume.getValue());
				if (mpVideo.getVolume() != 0.0) {
					labelVolume.setGraphic(ivVolume);
					isMuted = false;
				} else {
					labelVolume.setGraphic(ivMute);
					isMuted = true;
				}
			}
		});
		
		labelSpeed.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if(labelSpeed.getText().equals("1X")) {
					labelSpeed.setText("2X");
					mpVideo.setRate(2.0);
				}else {
					labelSpeed.setText("1X");
					mpVideo.setRate(1.0);
				}
			}

		});
		
		labelVolume.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if(isMuted) {
					labelVolume.setGraphic(ivVolume);
					sliderVolume.setValue(0.2);
					isMuted = false;
				}else {
					labelVolume.setGraphic(ivMute);
					sliderVolume.setValue(0);
					isMuted = true;
				}
			}
		});
	
		labelVolume.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if(hboxVolume.lookup("#sliderVolume")==null) {
					hboxVolume.getChildren().add(sliderVolume);
					sliderVolume.setValue(mpVideo.getVolume());
				}
			}
		});
		
		hboxVolume.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				hboxVolume.getChildren().remove(sliderVolume);
			}
		});
		
		vboxParent.sceneProperty().addListener(new ChangeListener<Scene>() {

			@Override
			public void changed(ObservableValue<? extends Scene> arg0, Scene oldS, Scene newS) {
				if(oldS==null && newS!=null) {
					mvVideo.fitHeightProperty().bind(newS.heightProperty().subtract(hboxControl.heightProperty().add(20)));
				}
			}	
		});
		
		labelFullScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				Label label = (Label)me.getSource();
				Stage stage =  (Stage)label.getScene().getWindow();
				if(stage.isFullScreen()) {
					stage.setFullScreen(false);
					labelFullScreen.setGraphic(ivFullScreen);
				}else {
					stage.setFullScreen(true);
					labelFullScreen.setGraphic(ivExit);
				}
				stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

					@Override
					public void handle(KeyEvent keyE) {
						if(keyE.getCode()==KeyCode.ESCAPE) {
							labelFullScreen.setGraphic(ivFullScreen);
						}
					}
				});
			}
		});
		
		mpVideo.totalDurationProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> obs, Duration oldv, Duration newv) {
				sliderTime.setMax(newv.toSeconds());
				labelTotalTime.setText(getTime(newv));
				
			}
			
		});
		
		sliderTime.valueChangingProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isChanging) {
				if(!isChanging) {
					mpVideo.seek(Duration.seconds(sliderTime.getValue()));  
				}
			}
		});
		
		sliderTime.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> obs, Number oldv, Number newv) {
				double currenttime = mpVideo.getCurrentTime().toSeconds();
				if(Math.abs(currenttime - newv.doubleValue())>0.5) {
					mpVideo.seek(Duration.seconds(newv.doubleValue()));
				}
				labelMatchEndVideo(labelCurrentTime.getText(), labelTotalTime.getText());
			}
			
		});
		
		mpVideo.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> obs, Duration oldt, Duration newt) {
				if(!sliderTime.isValueChanging()) {
					sliderTime.setValue(newt.toSeconds());
				}
				labelMatchEndVideo(labelCurrentTime.getText(), labelTotalTime.getText());
			}
		});
		
		mpVideo.setOnEndOfMedia(new Runnable() {
			
			@Override
			public void run() {
				buttonPPR.setGraphic(ivRestart);
				atTheEndOfVideo = true;
				if(!labelCurrentTime.textProperty().equals(labelTotalTime.textProperty())) {
					labelCurrentTime.textProperty().unbind();
					labelCurrentTime.setText(getTime(mpVideo.getTotalDuration())+" / ");
				}
			}
		});
		
		mpVideo.setOnReady(() -> mpVideo.play());
		
	}

	public void bindCurrentTimeLabel() {
		labelCurrentTime.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return getTime(mpVideo.getCurrentTime()) + " / ";
			}
		}, mpVideo.currentTimeProperty()));
	}

	public String getTime(Duration time) {
		int hours = (int) time.toHours();
		int minutes = (int) time.toMinutes();
		int seconds = (int) time.toSeconds();

		if (seconds > 59)
			seconds = seconds % 60;
		if (minutes > 59)
			minutes = minutes % 60;
		if (hours > 59)
			hours = hours % 60;

		if (hours > 0) {
			return String.format("%d:%02d:%02d", hours, minutes, seconds);
		} else {
			return String.format("%02d:%02d", minutes, seconds);
		}
	}
	public void labelMatchEndVideo(String labeltime, String labeltotaltime) {
		for(int i = 0; i<labeltotaltime.length(); i++) {
			if(labeltime.charAt(i)!=labeltotaltime.charAt(i)) {
				atTheEndOfVideo = false;
				if(isPlaying) buttonPPR.setGraphic(ivPause);
				else buttonPPR.setGraphic(ivPlay);
				break;
			}else {
				atTheEndOfVideo = true;
				buttonPPR.setGraphic(ivRestart);
			}
		}
	}
}
