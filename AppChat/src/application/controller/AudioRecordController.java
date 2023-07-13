package application.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.jfoenix.controls.JFXButton;

import application.models.ClientModel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class AudioRecordController implements Initializable {


    @FXML
    private JFXButton closeAudioRecord_btn;

    @FXML
    private FontAwesomeIconView microphone_fasiv;

    @FXML
    private JFXButton sendAudioRecord_btn;

    @FXML
    private Label start_lbl;

    @FXML
    private JFXButton start_stop_record_btn;


	private boolean isRecording = false;

	private File outputFile;
	private TargetDataLine audioLine;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		start_stop_record_btn.setOnAction(event -> {
			if (!this.isRecording) {
				
				audioRecord();
				
			} else {
				this.isRecording=false;
				this.start_lbl.setText("Click to start record");
				this.microphone_fasiv.setFill(Color.BLACK);
				stopRecording();
			}
		});
		this.sendAudioRecord_btn.setOnAction(event -> {
			if(this.outputFile!=null) {
				ClientModel.getInstance().getViewFactory().getClientController().sendAudioFileWithFile(outputFile);

			}
		});
	}

	public void audioRecord() {

		// open audio record
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Audio File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav"));
		outputFile = fileChooser.showSaveDialog(null);

		// Start recording
		if(outputFile!=null) {
			try {
//	            AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
				AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

				DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

				audioLine = (TargetDataLine) AudioSystem.getLine(info);
				audioLine.open(audioFormat);
				audioLine.start();

				// Create a new thread to capture audio data and write it to the output file
				Thread recordingThread = new Thread(this::record);
				recordingThread.start();
				this.isRecording = true;
				this.start_lbl.setText("Recording ...");
				this.microphone_fasiv.setFill(Color.WHITE);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}else {
			
		}
	}

	// record
	public void record() {
		AudioInputStream audioStream = new AudioInputStream(audioLine);
		
		try {
			AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outputFile);

		} catch (IOException e) {
			
			System.out.println("DONOTSAVE");
			
			
			e.printStackTrace();
		}
	}

	private void stopRecording() {
		audioLine.stop();
		audioLine.close();
	}
}
