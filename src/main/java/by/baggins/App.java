package by.baggins;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class App extends Application {

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
            stage.setTitle("mrPropper");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
