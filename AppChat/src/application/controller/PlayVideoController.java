package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.skins.JFXButtonSkin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class PlayVideoController implements Initializable{

    @FXML
    private JFXToggleButton cinema_btn;

    @FXML
    private MediaView mediaview;

    @FXML
    private JFXButton play_pause_btn;

    @FXML
    private JFXButton reset_btn;

    @FXML
    private JFXSlider slider_progress;

    @FXML
    private JFXSlider slider_volume;

    @FXML
    private JFXButton volume_btn;

    private MediaPlayer mdp;
    
    public PlayVideoController(MediaView mdp) {
		System.out.println("THIET LAP doi tuong");

    		this.mdp = mdp.getMediaPlayer();

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("THIET LAP MEDIAPLAYER");

		this.mediaview.setMediaPlayer(mdp);
						
		this.reset_btn.setSkin(new JFXButtonSkin(reset_btn));
		
		this.play_pause_btn.setOnAction(event->{
			System.out.println("P/P");
			if(mdp.getStatus().equals(MediaPlayer.Status.PLAYING)) {
				mdp.pause();
			}else if(mdp.getStatus().equals(MediaPlayer.Status.PAUSED)) {
				mdp.play();
			}else if(mdp.getStatus().equals(MediaPlayer.Status.STOPPED)) {
				mdp.play(); 
				
			}else {
				mdp.play(); 

			}
		});
		
		this.reset_btn.setOnAction(event -> {
			System.out.println("RS");
			mdp.seek(mdp.getStartTime()); 
		});
		
		slider_progress.setMin(0);
		slider_progress.setMax(mdp.getTotalDuration().toSeconds()*10); 
		slider_progress.setValue(0);
		
//		slider_progress.setOnMousePressed(event -> {
//		    if (mdp.getStatus() == MediaPlayer.Status.PLAYING || mdp.getStatus() == MediaPlayer.Status.PAUSED) {
//		    		mdp.pause();
//		    }
//		});
		
		slider_progress.valueChangingProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean isChanging) {
				if(!isChanging) {
					mdp.seek(Duration.seconds(slider_progress.getValue()));
				}
			}
		
		});
		
		slider_progress.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newv) {
				double currenttime = mdp.getCurrentTime().toSeconds();
				if(Math.abs(currenttime-newv.doubleValue())>0.5) {
					mdp.seek(Duration.seconds(newv.doubleValue()));
				}
			}
			
		});	
		
		
		
		mdp.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
		    if (!slider_progress.isValueChanging()) {
		    	slider_progress.setValue(newValue.toSeconds());
		    }
		});
//		
		slider_volume.setMin(0); // Giá trị tối thiểu của Slider
		slider_volume.setMax(1); // Giá trị tối đa của Slider
		slider_volume.setValue(1); // Giá trị ban đầu của Slider
		
		slider_volume.valueProperty().addListener((observable, oldValue, newValue) -> {
		    if (mdp.getStatus() == MediaPlayer.Status.PLAYING) {
		    	mdp.setVolume(newValue.doubleValue());
		    }
		});
		
		cinema_btn.selectedProperty().addListener((obv, oldv, newv)->{
			if (newv) {
                System.out.println("ToggleButton is selected");
            } else {
                System.out.println("ToggleButton is deselected");
            }
		});		
	}
       
}
