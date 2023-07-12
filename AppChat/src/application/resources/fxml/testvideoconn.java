package application.resources.fxml;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class testvideoconn implements Initializable{

    @FXML
    private MediaView mdav;

    @FXML
    private Button play;

    @FXML
    private Button stop;
    MediaPlayer mediaPlayer;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		File file = new File("/Users/tongcongminh/Pictures/ahri.mp4");
		Media media = new Media(file.toURI().toString());
		
		mediaPlayer = new MediaPlayer(media);
		//den day chi moi nghe dc am thanh
		//them trinh phat phuong tien vao
		mdav.setMediaPlayer(mediaPlayer);
		
	}
	
	public void playMedia() {
		mediaPlayer.play();
	}
	public void pauseMedia() {
		mediaPlayer.pause();;
		
	}
	public void resetMedia() {
		if(mediaPlayer.getStatus() != MediaPlayer.Status.READY)
		mediaPlayer.seek(Duration.seconds(.0));
	}
}
