package application.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import application.models.ClientModel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class OpenWindowController implements Initializable{
	
	
	@FXML
	private ProgressBar loadingOpen;
	
	private Timer timer;
	private TimerTask task;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		startProgress();
		loadingOpen.progressProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.doubleValue() >= 1.0) {
				timer.cancel();
				Stage stage = (Stage)loadingOpen.getScene().getWindow();
				stage.close();
				//				ClientModel.getInstance().getViewFactory().transparentStageFadeout(stage);

				ClientModel.getInstance().getViewFactory().showLoginWindow();
			}
			
		});
	}
	
	private void startProgress() {
		
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				
				// cần đảm bảo rằng các thay đổi giao diện người dùng của JavaFX được thực hiện trên luồng ứng dụng JavaFX chính
				//đảm bảo rằng việc cập nhật tiến trình (progress) được thực hiện trên luồng ứng dụng JavaFX chính
				Platform.runLater(()->loadingOpen.setProgress(loadingOpen.getProgress() + 0.01));
				
			}
		};
		timer.scheduleAtFixedRate(task, 0, 15);
	}
	
	//fade out stage the close the stage in 1 sec
	
	
}
