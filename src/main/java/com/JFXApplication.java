package com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JFXApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SnapShotScene snapShotScene = new SnapShotScene();
        snapShotScene.setBaseLine(300,300);

        Scene scene = new Scene(snapShotScene.getRoot());

        stage.setScene(scene);

        stage.getIcons().add(new Image("/icon.png"));

        stage.setTitle("Sneed-Cutter");

        stage.setResizable(false);

        stage.show();
    }

}
