package by.baggins;

import by.baggins.controller.DuplicatesController;
import by.baggins.dto.FileInfo;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class App extends Application {
    private ObservableList<FileInfo> fileInfoList = FXCollections.observableArrayList();

    public App() {
        fileInfoList.add(new FileInfo(2.4d, "file1", "txt"));
        fileInfoList.add(new FileInfo(2.5d, "file2", "xml"));
        fileInfoList.add(new FileInfo(2.6d, "file3", "xsl"));
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }



    @Override
    public void start(Stage stage) throws Exception {
        try {
            System.out.println(stage);

            String fxmlFile = "/fxml/mainStage.fxml";
            FXMLLoader loader = new FXMLLoader();
            InputStream inputStream = App.class.getResourceAsStream(fxmlFile);
            Parent root = (Parent) loader.load(inputStream);

            DuplicatesController dupController = loader.getController();
            dupController.setApp(this);

            stage.setTitle("mrPropper");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public ObservableList<FileInfo> getFileInfoList() {
        return fileInfoList;
    }
}
