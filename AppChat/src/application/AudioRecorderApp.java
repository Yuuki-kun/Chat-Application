package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorderApp {

    private File outputFile;
    private TargetDataLine audioLine;

    private Button recordButton;
    private Button stopButton;

    private void startRecording() {
        // Select the output file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Audio File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav"));
        outputFile = fileChooser.showSaveDialog(null);

        // Start recording
        try {
//            AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
        	AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

            audioLine = (TargetDataLine) AudioSystem.getLine(info);
            audioLine.open(audioFormat);
            audioLine.start();

            recordButton.setDisable(true);
            stopButton.setDisable(false);

            // Create a new thread to capture audio data and write it to the output file
            Thread recordingThread = new Thread(this::record);
            recordingThread.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void record() {
        AudioInputStream audioStream = new AudioInputStream(audioLine);

        try {
            AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        audioLine.stop();
        audioLine.close();

        recordButton.setDisable(false);
        stopButton.setDisable(true);
    }
}
