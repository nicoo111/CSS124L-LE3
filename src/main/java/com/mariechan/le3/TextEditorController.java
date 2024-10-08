package com.mariechan.le3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TextEditorController {
    
    @FXML
    TextArea textArea;

    private boolean isTextChanged = false;
    private String documentTitle = "Untitled Document.txt";
    private File currentFile = null;
//    private List<String> recentFiles = new ArrayList<>();
//    private ListView<String> recentFilesList = new ListView<>();

    @FXML
    public void initialize() {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            isTextChanged = true;
            Stage stage = (Stage) textArea.getScene().getWindow();
            stage.setTitle(documentTitle + " (In Progress)");
        });

        Platform.runLater(() -> {
            Stage stage = (Stage) textArea.getScene().getWindow();
            stage.setTitle(documentTitle);
            stage.setOnCloseRequest(event -> {
                if (isTextChanged) {
                    event.consume();
                    dialogClose(stage);
                }
            });
            if (currentFile != null) {

                try {

                    BufferedReader br = new BufferedReader(new FileReader(currentFile));
                    String line;
                    StringBuilder text = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        text.append(line + "\n");
                    }
                    textArea.setText(text.toString());
                    isTextChanged = false;
                    documentTitle = currentFile.getName();
                    stage.setTitle(documentTitle);
                    br.close();
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void dialogClose(Stage stage) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Unsaved changes in " + documentTitle);
        dialog.setHeaderText("You have unsaved changes.");
        dialog.setContentText("Do you want to save your changes before closing?");
        ButtonType saveButton = new ButtonType("Save", ButtonData.YES);
        ButtonType dontSaveButton = new ButtonType("Don't Save", ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(saveButton, dontSaveButton, cancelButton);

        VBox vbox = new VBox();
        vbox.getChildren().add(new Label("Do you want to save your changes?"));
        dialog.getDialogPane().setContent(vbox);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get() == saveButton) {

                handleSave();

                if (!isTextChanged) {
                    stage.close();
                }
            } else if (result.get() == dontSaveButton) {

                stage.close();
            }

        }
    }

    @FXML
    public void handleSave() {
        save(false);
    }

    public void save(boolean saveAsNewFile) {
        var text = textArea.getText();
        Stage stage = (Stage) textArea.getScene().getWindow();
    
        if (saveAsNewFile || currentFile == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("Untitled");
            fileChooser.setTitle("Save file");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
            
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                currentFile = file;
                documentTitle = file.getName();
            } else {
                return; 
            }
        }
    
        try (FileWriter fileWriter = new FileWriter(currentFile)) {
            fileWriter.write(text);
            isTextChanged = false;
            stage.setTitle(documentTitle);
        } catch (IOException e) {
            System.out.println("Error saving file.");
            e.printStackTrace();
        }
    }

//    public void updateRecentFilesList(){
//        recentFilesList.getItems().clear();
//        recentFilesList.getItems().addAll(recentFiles);
//    }

    public void setFile(File file) {
        currentFile = file;
    }
}
