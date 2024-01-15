//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SpaceApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainUI root = new MainUI(700);
        Scene scene = new Scene(root);
        stage.setTitle("Space App!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}