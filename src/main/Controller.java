package main;

import ClassDetails.ClassInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import progress.ProgressInfo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class Controller {


    @FXML
    private TableView<ClassInfo> tableViewClassList;

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

    @FXML
    private TextField textFieldClassDescription;

    @FXML
    private TextField textFieldProgressUpdateTitle;

    @FXML
    private TextArea textAreaProgressDescription;

    @FXML
    private Slider sliderMotivation;

    private ObservableList<ClassInfo> classList; // Moved to be an instance variable

    private ObservableList<ProgressInfo> progressList; //thanks gpt

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
        progressList = loadProgressDataFromJson("C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\progress\\progressUpdate.json");
        tableViewClassList.setItems(classList);
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
            // Populate the class description text box with the current description
            textFieldClassDescription.setText(selectedClass.getClassDesc());

            // Populate the class notes text area with the current notes
            textAreaClassNotes.setText(selectedClass.getClassNotes());
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
        // Handle saving progress for the "Progress" tab
        ClassInfo selectedClassProgress = progressComboBox.getSelectionModel().getSelectedItem();
        if (selectedClassProgress != null) {
            try {
                // Retrieve and set the new progress value
                double newProgress = Double.parseDouble(textFieldProgressUpdateProgress.getText());
                selectedClassProgress.setPercentDone(newProgress);
                classProgressBarProgress.setProgress(newProgress);
                classProgressBarDetail.setProgress(newProgress); // Sync the detail progress bar

                // Update the overall progress bar
                updateOverallProgress(classList);

                // Save the updated list back to the JSON file
                saveClassDataToJson(classList, "C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\ClassDetails\\classList.json");

                System.out.println("Progress updated and saved!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid progress value. Please enter a valid number.");
            }
        }

        // Handle saving notes for the "Detail" tab
        ClassInfo selectedClassDetail = detailComboBox.getSelectionModel().getSelectedItem();
        if (selectedClassDetail != null) {
            String newNotes = textAreaClassNotes.getText();
            String newDescription = textFieldClassDescription.getText();
            selectedClassDetail.setClassNotes(newNotes);
            selectedClassDetail.setClassDesc(newDescription);

            // Save the updated list back to the JSON file
            saveClassDataToJson(classList, "C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\ClassDetails\\classList.json");
            tableViewClassList.refresh();
            System.out.println("Class notes updated and saved!");
        }

        // If neither ComboBox has a selected class, show a message
        if (selectedClassProgress == null && selectedClassDetail == null) {
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
    private void saveProgressDataToJson(ObservableList<ProgressInfo> progressList, String filePath) {
        // Register the custom LocalDateTime adapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new localDateTimeAdapter())
                .setPrettyPrinting() // Optional: formats the JSON output nicely
                .create();

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(progressList, writer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save data to JSON.");
        }
    }
@FXML
    private void SaveProgressUpdate() {
        String title = textFieldProgressUpdateTitle.getText();
        String description = textAreaProgressDescription.getText();
        double motivation = sliderMotivation.getValue();
        LocalDateTime dateTime = LocalDateTime.now();

        // Get the current local date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Create a new ProgressInfo object with the current date and time
        ProgressInfo newProgress = new ProgressInfo(title, description, motivation, currentDateTime.toString());

        // Add the new progress to the list
        progressList.add(newProgress);

        // Save the updated list back to the JSON file
        saveProgressDataToJson(progressList, "C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\progress\\progressUpdate.json");

        // Clear the input fields after saving
        textFieldProgressUpdateTitle.clear();
    textAreaProgressDescription.clear();
        sliderMotivation.setValue(0.5);
    }
    private ObservableList<ProgressInfo> loadProgressDataFromJson(String filePath) {
        ObservableList<ProgressInfo> progressList = FXCollections.observableArrayList();

        // Register the custom LocalDateTime adapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new localDateTimeAdapter())
                .create();

        try (FileReader reader = new FileReader(filePath)) {
            Type progressListType = new TypeToken<List<ProgressInfo>>() {}.getType();
            List<ProgressInfo> progressUpdates = gson.fromJson(reader, progressListType);
            progressList.addAll(progressUpdates);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return progressList;
    }

}
