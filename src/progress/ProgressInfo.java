package progress;

import java.time.LocalDateTime;


//apparently case matters when declaring a constructor?
//thanks gpt
public class ProgressInfo {
    private String progressTitle;
    private String progressDescription;
    private double progressMotivation;
    private LocalDateTime dateAndTime;

    public ProgressInfo(String progressTitle, String progressDescription, double progressMotivation, String dateAndTime) {
        this.progressTitle = progressTitle;
        this.progressDescription = progressDescription;
        this.progressMotivation = progressMotivation;
        // Parse the string to LocalDateTime
        this.dateAndTime = LocalDateTime.parse(dateAndTime);
    }

    // Getters and Setters
    public String getProgressTitle() {
        return progressTitle;
    }

    public void setProgressTitle(String progressTitle) {
        this.progressTitle = progressTitle;
    }

    public String getProgressDescription() {
        return progressDescription;
    }

    public void setProgressDescription(String progressDescription) {
        this.progressDescription = progressDescription;
    }

    public double getProgressMotivation() {
        return progressMotivation;
    }

    public void setProgressMotivation(double progressMotivation) {
        this.progressMotivation = progressMotivation;
    }

    public LocalDateTime getDateAndTime() { // Use camelCase for method name
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) { // Use camelCase for method name
        this.dateAndTime = LocalDateTime.parse(dateAndTime);
    }
}
