package com.mariechan.le3;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainController {
    @FXML
    VBox parent;
    @FXML
    private Button closeButton;


    
    
    @FXML
    public void handleNew() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("texteditor.fxml"));
        Parent root = fxmlLoader.load();

        Stage newWindow = new Stage();

        newWindow.setScene(new Scene(root, 650, 650));

        newWindow.show();
    }

    @FXML
    public void handleLoad() {
        var stage = (Stage) parent.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load file...");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text", "*.txt"));
        var file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {

                openNewWindow(stage, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void handleClose() {
    // get a handle to the stage
    Stage stage = (Stage) closeButton.getScene().getWindow();
    // do what you have to do
    stage.close();
    }

    public void openNewWindow(Stage stage, File file) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("texteditor.fxml"));
        Parent root = fxmlLoader.load();
        TextEditorController controller = fxmlLoader.getController();
        controller.setFile(file);
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root, 640, 480));

        newStage.show();
    }
}
