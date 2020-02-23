package aritUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AritSoftware extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
        primaryStage.setTitle("Arit Software");
        primaryStage.setScene(new Scene(root, 732, 502));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
