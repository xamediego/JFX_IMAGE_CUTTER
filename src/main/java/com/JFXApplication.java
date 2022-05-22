package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JFXApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/FxmlFiles/main.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.getIcons().add(new Image("/icon.png"));

        stage.setTitle("Sneed-Cutter");

        stage.setResizable(false);

        stage.show();
    }


}
