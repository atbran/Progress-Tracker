package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Play the audio
            String resourcePath = "tada.wav";
            media.audioPlayer.playAudio(resourcePath);

            // Load your main UI
            Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
            primaryStage.setTitle("Progress Tracker: ALPHA 0.2.5");
            primaryStage.setScene(new Scene(root, 1000, 800));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
