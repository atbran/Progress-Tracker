package Controller;

import ClassDetails.ClassInfo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import progress.ProgressInfo;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class Controller {

    @FXML private TableView<ClassInfo> tableViewClassList;
    @FXML private TableColumn<ClassInfo, String> classNameColumn, courseDescriptionColumn, difficultyColumn;
    @FXML private TableColumn<ClassInfo, Double> percentDoneColumn;
    @FXML private ComboBox<ClassInfo> progressComboBox, detailComboBox;
    @FXML private ProgressBar classProgressBarProgress, classProgressBarDetail, progressBarMain;
    @FXML private Text progressText;
    @FXML private TextField textFieldProgressUpdateProgress, textFieldClassDescription, textFieldProgressUpdateTitle;
    @FXML private TextArea textAreaClassNotes, textAreaProgressDescription;
    @FXML private Slider sliderMotivation;

    private ObservableList<ClassInfo> classList;
    private ObservableList<ProgressInfo> progressList;

    private final String classDataFilePath = "C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\ClassDetails\\classList.json";
    private final String progressDataFilePath = "C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\progress\\progressUpdate.json";

    public void initialize() {
        setupTable();
        loadData();
        setupComboBoxes();
        updateOverallProgress();
    }

    private void setupTable() {
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("classDesc"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("classDifficulty"));
        percentDoneColumn.setCellValueFactory(new PropertyValueFactory<>("percentDone"));
    }

    private void loadData() {
        // Define Types for ClassInfo and ProgressInfo
        Type classListType = new com.google.gson.reflect.TypeToken<List<ClassInfo>>() {}.getType();
        Type progressListType = new com.google.gson.reflect.TypeToken<List<ProgressInfo>>() {}.getType();

        // Load Class Data
        classList = jsonInterface.loadDataFromJson(classDataFilePath, classListType);
        tableViewClassList.setItems(classList);

        // Load Progress Data
        progressList = jsonInterface.loadDataFromJson(progressDataFilePath, progressListType);
    }

    private void setupComboBoxes() {
        progressComboBox.setItems(classList);
        detailComboBox.setItems(classList);
        setupComboBox(progressComboBox);
        setupComboBox(detailComboBox);
        setupComboBoxListeners();
    }

    private void setupComboBox(ComboBox<ClassInfo> comboBox) {
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ClassInfo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getClassName());
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ClassInfo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getClassName());
            }
        });
    }

    private void setupComboBoxListeners() {
        progressComboBox.setOnAction(event -> updateProgressView());
        detailComboBox.setOnAction(event -> updateDetailView());
    }

    private void updateProgressView() {
        ClassInfo selectedClass = progressComboBox.getSelectionModel().getSelectedItem();
        if (selectedClass != null) {
            double progress = selectedClass.getPercentDone();
            classProgressBarProgress.setProgress(progress);
            textFieldProgressUpdateProgress.setText(String.valueOf(progress));
            textAreaClassNotes.setText(selectedClass.getClassNotes());
        }
    }

    private void updateDetailView() {
        ClassInfo selectedClass = detailComboBox.getSelectionModel().getSelectedItem();
        if (selectedClass != null) {
            classProgressBarDetail.setProgress(selectedClass.getPercentDone());
            textFieldClassDescription.setText(selectedClass.getClassDesc());
            textAreaClassNotes.setText(selectedClass.getClassNotes());
        }
    }

    private void updateOverallProgress() {
        if (classList.isEmpty()) {
            progressBarMain.setProgress(0.0);
            progressText.setText("No classes added yet.");
            return;
        }

        double averageProgress = classList.stream().mapToDouble(ClassInfo::getPercentDone).average().orElse(0.0);
        progressBarMain.setProgress(averageProgress);
        progressText.setText(String.format("You are %.2f%% done!", averageProgress * 100));
    }

    @FXML
    private void handleSaveButton() {
        saveProgressData();
        saveDetailData();
        updateOverallProgress();
        tableViewClassList.refresh();
    }

    private void saveProgressData() {
        ClassInfo selectedClass = progressComboBox.getSelectionModel().getSelectedItem();
        if (selectedClass != null) {
            try {
                double newProgress = Double.parseDouble(textFieldProgressUpdateProgress.getText());
                selectedClass.setPercentDone(newProgress);
                classProgressBarProgress.setProgress(newProgress);
                classProgressBarDetail.setProgress(newProgress);
                jsonInterface.saveDataToJson(classList, classDataFilePath);
                System.out.println("Progress updated and saved!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid progress value. Please enter a valid number.");
            }
        }
    }

    private void saveDetailData() {
        ClassInfo selectedClass = detailComboBox.getSelectionModel().getSelectedItem();
        if (selectedClass != null) {
            selectedClass.setClassNotes(textAreaClassNotes.getText());
            selectedClass.setClassDesc(textFieldClassDescription.getText());
            jsonInterface.saveDataToJson(classList, classDataFilePath);
            System.out.println("Class details updated and saved!");
        }
    }

    @FXML
    private void SaveProgressUpdate() {
        ProgressInfo newProgress = new ProgressInfo(
                textFieldProgressUpdateTitle.getText(),
                textAreaProgressDescription.getText(),
                sliderMotivation.getValue(),
                LocalDateTime.now().toString()
        );
        String resourcePath = "/tada.wav";
        media.audioPlayer.playAudio(resourcePath);
        progressList.add(newProgress);
        saveProgressInfo();
    }

    private void saveProgressInfo() {
        Type progressListType = new com.google.gson.reflect.TypeToken<List<ProgressInfo>>() {}.getType();
        jsonInterface.saveDataToJson(progressList, progressDataFilePath);
        clearProgressInputFields();
    }

    private void clearProgressInputFields() {
        textFieldProgressUpdateTitle.clear();
        textAreaProgressDescription.clear();
        sliderMotivation.setValue(0.5);
    }
}
