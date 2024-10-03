module Progress.tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.media;

    opens main;
    opens ClassDetails to javafx.base, com.google.gson;
    opens progress to com.google.gson;
    opens Controller;
    opens media;
}
