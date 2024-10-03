package Controller;

import ClassDetails.ClassInfo;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import progress.ProgressInfo;

import java.lang.reflect.Type;
import java.math.BigDecimal;
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
    @FXML private TableView<ProgressInfo> progressTable;

    private ObservableList<ClassInfo> classList;
    private ObservableList<ProgressInfo> progressList;

    private final String classDataFilePath = "C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\ClassDetails\\classList.json";
    private final String progressDataFilePath = "C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\progress\\progressUpdate.json";

    public void initialize() {
        setupTable();
        loadData();
        setupComboBoxes();
        updateOverallProgress();
        updateProgressTable();
    }

    private void setupTable() {
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("classDesc"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("classDifficulty"));
        percentDoneColumn.setCellValueFactory(new PropertyValueFactory<>("percentDone"));
    }

    private void loadData() {
        // Define Types for ClassInfo and ProgressInfo
        Type classListType = new TypeToken<List<ClassInfo>>() {}.getType();
        Type progressListType = new TypeToken<List<ProgressInfo>>() {}.getType();

        // Load Class Data
        classList = FXCollections.observableArrayList(jsonInterface.loadDataFromJson(classDataFilePath, classListType));
        tableViewClassList.setItems(classList);

        //Color table rows depending on progress
        tableViewClassList.setRowFactory(tv -> new TableRow<ClassInfo>() {
            @Override
            protected void updateItem(ClassInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getPercentDone() == 0.0) {
                        setStyle("-fx-background-color: #fd6868;");
                    } else if (item.getPercentDone() == 1.0) {
                        setStyle("-fx-background-color: #63fd63;");
                    } else {
                        setStyle("-fx-background-color: #fafa67;");
                    }
                }
            }
        });
        // Load Progress Data
        progressList = FXCollections.observableArrayList(jsonInterface.loadDataFromJson(progressDataFilePath, progressListType));
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
            double newProgress = getValidProgressValue(textFieldProgressUpdateProgress);
            if (newProgress >= 0) {
                selectedClass.setPercentDone(newProgress);
                classProgressBarProgress.setProgress(newProgress);
                classProgressBarDetail.setProgress(newProgress);
                progressList.add(new ProgressInfo(selectedClass.getClassName(), "", newProgress, LocalDateTime.now().toString()));
                jsonInterface.saveDataToJson(progressList, progressDataFilePath);
                System.out.println("Progress updated and saved!");
            }
        }
    }

    private double getValidProgressValue(TextField textField) {
        try {
            double newProgress = Double.parseDouble(textField.getText());
            return BigDecimal.valueOf(newProgress).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (NumberFormatException e) {
            System.out.println("Invalid progress value. Please enter a valid number.");
            return -1;
        }
    }

    private void updateProgressTable() {
        progressList = FXCollections.observableArrayList(jsonInterface.loadDataFromJson(progressDataFilePath, new TypeToken<List<ProgressInfo>>(){}.getType()));
        progressTable.setItems(progressList);

        TableColumn<ProgressInfo, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("progressTitle"));

        TableColumn<ProgressInfo, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("progressDescription"));

        TableColumn<ProgressInfo, Double> motivationColumn = new TableColumn<>("Motivation");
        motivationColumn.setCellValueFactory(new PropertyValueFactory<>("progressMotivation"));

        TableColumn<ProgressInfo, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAndTime"));

        progressTable.getColumns().setAll(titleColumn, descriptionColumn, motivationColumn, dateColumn);
    }

    @FXML
    private void SaveProgressUpdate() {
        // thanks gpt
        double motivationValue = BigDecimal.valueOf(sliderMotivation.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        ProgressInfo newProgress = new ProgressInfo(
                textFieldProgressUpdateTitle.getText(),
                textAreaProgressDescription.getText(),
                motivationValue,
                LocalDateTime.now().toString()
        );
        String resourcePath = "/tada.wav";
        media.audioPlayer.playAudio(resourcePath);
        progressList.add(newProgress);
        saveProgressInfo();
        updateProgressTable();
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

    private void saveProgressInfo() {
        jsonInterface.saveDataToJson(progressList, progressDataFilePath);
        clearProgressInputFields();
    }

    private void clearProgressInputFields() {
        textFieldProgressUpdateTitle.clear();
        textAreaProgressDescription.clear();
        sliderMotivation.setValue(0.5);
    }
}