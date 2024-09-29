module Progress.tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens main;
    opens ClassDetails to javafx.base, com.google.gson;
    opens progress to com.google.gson;


}
