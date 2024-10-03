package media;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class audioPlayer {
    private static MediaPlayer mediaPlayer;

    public static void playAudio(String resourcePath) {
        try {
            // Correctly load the audio file from resources
            URL resourceURL = audioPlayer.class.getResource(resourcePath);
            if (resourceURL == null) {
                System.out.println("Audio resource not found: " + resourcePath);
                return;
            }
            String mediaURI = resourceURL.toExternalForm();
            System.out.println("Playing audio from URI: " + mediaURI);

            Media sound = new Media(mediaURI);
            mediaPlayer = new MediaPlayer(sound);

            // Set volume before playing
            mediaPlayer.setVolume(1.0);

            // Error handling for Media
            sound.setOnError(() -> {
                System.out.println("Media Error: " + sound.getError());
            });

            // Error handling for MediaPlayer
            mediaPlayer.setOnError(() -> {
                System.out.println("MediaPlayer Error: " + mediaPlayer.getError());
            });

            mediaPlayer.setOnReady(() -> {
                System.out.println("Audio is ready to play");
                mediaPlayer.play();
            });

            mediaPlayer.setOnPlaying(() -> {
                System.out.println("Audio playback started");
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("Audio playback completed");
                mediaPlayer.dispose();
                mediaPlayer = null;
            });

        } catch (Exception e) {
            System.out.println("Error playing audio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
