package main;

import ClassDetails.ClassInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Controller {


    @FXML
    private TableView<ClassInfo> tableView;

    @FXML
    private TableColumn<ClassInfo, String> classNameColumn;

    @FXML
    private TableColumn<ClassInfo, String> courseDescriptionColumn;

    @FXML
    private TableColumn<ClassInfo, String> difficultyColumn;

    @FXML
    private TableColumn<ClassInfo, Double> percentDoneColumn;

    @FXML
    private ComboBox<ClassInfo> progressComboBox;

    @FXML
    private ComboBox<ClassInfo> detailComboBox;

    @FXML
    private ProgressBar classProgressBarProgress;

    @FXML
    private ProgressBar classProgressBarDetail;

    @FXML
    private ProgressBar progressBarMain;

    @FXML
    private Text progressText;

    @FXML
    private TextField textFieldProgressUpdateProgress;

    @FXML
    private TextArea textAreaClassNotes;

    @FXML
    private Button buttonSaveProgressDetail;
    @FXML
    private Button buttonSaveProgressProgress;


    private ObservableList<ClassInfo> classList; // Moved to be an instance variable

    public void initialize() {
        // Set up the columns in the table
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("classDesc"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("classDifficulty"));
        percentDoneColumn.setCellValueFactory(new PropertyValueFactory<>("percentDone"));

        // FUCKING INTELLIJ SHIT!
        // ObservableList<ClassInfo> classList = loadClassDataFromJson("/ClassDetails/classList.json");
        // WHY
        // CANT
        // YOU
        // FIND
        // THE FUCKING FILE!
        classList = loadClassDataFromJson("C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\ClassDetails\\classList.json");

        tableView.setItems(classList);
        progressComboBox.setItems(classList);
        detailComboBox.setItems(classList);

        setupComboBox(progressComboBox);
        setupComboBox(detailComboBox);
        updateOverallProgress(classList);

        progressComboBox.setOnAction(event -> {
            ClassInfo selectedClass = progressComboBox.getSelectionModel().getSelectedItem();
            if (selectedClass != null) {
                double progress = selectedClass.getPercentDone();
                classProgressBarProgress.setProgress(progress);
                textFieldProgressUpdateProgress.setText(String.valueOf(progress));
                textAreaClassNotes.setText(selectedClass.getClassNotes());
            }
        });

        detailComboBox.setOnAction(event -> {
            ClassInfo selectedClass = detailComboBox.getSelectionModel().getSelectedItem();
            if (selectedClass != null) {
                double progress = selectedClass.getPercentDone();
                classProgressBarDetail.setProgress(progress);
            }
        });
    }

    private void setupComboBox(ComboBox<ClassInfo> comboBox) {
        comboBox.setCellFactory(lv -> new ListCell<ClassInfo>() {
            @Override
            protected void updateItem(ClassInfo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getClassName());
            }
        });

        comboBox.setButtonCell(new ListCell<ClassInfo>() {
            @Override
            protected void updateItem(ClassInfo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getClassName());
            }
        });
    }

    private ObservableList<ClassInfo> loadClassDataFromJson(String filePath) {
        ObservableList<ClassInfo> classList = FXCollections.observableArrayList();
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)) {
            // Define the type of the collection
            Type classListType = new TypeToken<List<ClassInfo>>() {}.getType();
            // Parse the JSON file into a list of ClassInfo objects
            List<ClassInfo> classes = gson.fromJson(reader, classListType);
            classList.addAll(classes);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Intellij Strikes again.");
        }

        return classList;
    }

    private void updateOverallProgress(ObservableList<ClassInfo> classList) {
        if (classList.isEmpty()) {
            progressBarMain.setProgress(0.0);
            return;
        }

        double totalProgress = 0.0;
        for (ClassInfo classInfo : classList) {
            totalProgress += classInfo.getPercentDone();
        }

        double averageProgress = totalProgress / classList.size();
        progressBarMain.setProgress(averageProgress);
        double percentDone = 100 * averageProgress;
        String formattedPercent = String.format("%.2f", percentDone);
        progressText.setText("You are " + formattedPercent + "% done!");
    }

    @FXML
    private void handleSaveButton() {
        ClassInfo selectedClass = progressComboBox.getSelectionModel().getSelectedItem();
        if (selectedClass != null) {
            try {
                double newProgress = Double.parseDouble(textFieldProgressUpdateProgress.getText());
                String newNotes = textAreaClassNotes.getText();

                selectedClass.setPercentDone(newProgress);
                selectedClass.setClassNotes(newNotes);
                classProgressBarDetail.setProgress(newProgress);
                classProgressBarProgress.setProgress(newProgress);

                // Update the overall progress bar
                updateOverallProgress(classList);

                // Save the updated list back to the JSON file
                saveClassDataToJson(classList, "C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\ClassDetails\\classList.json");
            } catch (NumberFormatException e) {
                System.out.println("Invalid progress value. Please enter a number.");
            }
        }
        else{
            System.out.println("You have to select a class.");
        }
    }

    private void saveClassDataToJson(ObservableList<ClassInfo> classList, String filePath) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(classList, writer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save data to JSON.");
        }
    }
}
