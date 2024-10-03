package Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import progress.ProgressInfo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

//thanks gpt
//Refactored the code to use generics

public class jsonInterface {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new localDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    /**
     * Generic method to load data from a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @param typeToken The TypeToken representing the list type.
     * @param <T> The type of objects in the list.
     * @return An ObservableList of the specified type.
     */
    public static <T> ObservableList<T> loadDataFromJson(String filePath, Type typeToken) {
        try (FileReader reader = new FileReader(filePath)) {
            List<T> dataList = gson.fromJson(reader, typeToken);
            return FXCollections.observableArrayList(dataList);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading data from " + filePath);
            return FXCollections.observableArrayList();
        }
    }

    /**
     * Generic method to save data to a JSON file.
     *
     * @param dataList The list of data to save.
     * @param filePath The path to the JSON file.
     * @param <T> The type of objects in the list.
     */
    public static <T> void saveDataToJson(List<T> dataList, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(dataList, writer);
            System.out.println("Data successfully saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save data to " + filePath);
        }
    }
}
