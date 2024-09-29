package main;

import ClassDetails.ClassInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileReader;
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

    public void initialize() {
        // Set up the columns in the table
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("classDesc"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("classDifficulty"));
        percentDoneColumn.setCellValueFactory(new PropertyValueFactory<>("percentDone"));

        // FUCKING INTELLIJ SHIT!
//        ObservableList<ClassInfo> classList = loadClassDataFromJson("/ClassDetails/classList.json");
        //IntelliJ pisses me off with this wrap around horse shite.
        //WHY
        //CANT
        //YOU
        //FIND
        //THE FUCKING FILE!
        ObservableList<ClassInfo> classList = loadClassDataFromJson("C:\\Users\\ayden\\IdeaProjects\\Progress_tracker\\src\\ClassDetails\\classList.json");
        // Add the list to the TableView
        tableView.setItems(classList);
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
}
