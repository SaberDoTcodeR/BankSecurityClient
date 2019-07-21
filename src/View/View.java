package View;

import Controller.LoginController;
import Model.Connection;
import Model.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class View {
    static Scene scene = new Scene(new GridPane());

    public static void makeLoginScene() {
        Platform.runLater(() -> {
            GridPane root = null;
            try {
                root = FXMLLoader.load(LoginController.class.getResource("loginMenu.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            scene.setRoot(root);
            scene.setCursor(new ImageCursor(new Image("Controller/css/OzFOdVG.png")));
            scene.getStylesheets().clear();
            scene.getStylesheets().add(LoginController.class.getResource("css/css.css").toExternalForm());
            Stage primaryStage1 = Main.getPrimaryStage();
            primaryStage1.setScene(scene);
            primaryStage1.setResizable(true);
            primaryStage1.setWidth(400);
            primaryStage1.setHeight(600);
        });
    }
    public static void makeMainMenuScene() {
        Platform.runLater(() -> {
            GridPane root = null;
            try {
                root = FXMLLoader.load(LoginController.class.getResource("mainMenu.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            scene.setRoot(root);
            scene.setCursor(new ImageCursor(new Image("Controller/css/OzFOdVG.png")));
            scene.getStylesheets().clear();
            scene.getStylesheets().add(LoginController.class.getResource("css/css.css").toExternalForm());
            Stage primaryStage1 = Main.getPrimaryStage();
            primaryStage1.setScene(scene);
            primaryStage1.setResizable(true);
            primaryStage1.setWidth(400);
            primaryStage1.setHeight(600);
        });
    }
}
