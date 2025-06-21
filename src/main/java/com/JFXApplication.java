package com;

import com.enums.FXMLPart;
import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.goxr3plus.fxborderlessscene.borderless.CustomStage;

import com.scenes.snapshotter.SnapShotController;
import com.scenes.snapshotter.SnapShotScene;
import com.scenes.titlebar.TitlebarScene;
import com.window.WindowedApplication;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JFXApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        borderlessStage();
    }

    private void vanillaStage(Stage stage) {
        SnapShotScene snapShotScene = new SnapShotScene(new SnapShotController(), FXMLPart.SNAPSHOTTER);

        snapShotScene.setBaseLine(300, 300);

        Scene scene = new Scene(snapShotScene.getRoot());

        stage.setScene(scene);

        stage.getIcons().add(new Image("Images/app/icon.png"));

        stage.setTitle("Sneed-Cutter");

        stage.setResizable(false);

        stage.show();
    }

    private void borderlessStage() {
        int width = 800;
        int height = 600;

        // Init root application box
        WindowedApplication windowedApplication = new WindowedApplication();

        VBox rootBox = windowedApplication.createNewRootBox(width, height, "Styling/app.css", "primaryBackground");

        CustomStage customStage = windowedApplication.createNewCustomStage("Sneed-Cutter", "Images/app/icon.png");

        BorderlessScene borderlessScene = windowedApplication.createNewBorderlessScene(customStage, rootBox);

        TitlebarScene titlebarScene = windowedApplication.createNewTitleBar(customStage, borderlessScene);

        rootBox.getChildren().add(titlebarScene.getRoot());

        VBox itemBox = windowedApplication.createNewItemBox();

        windowedApplication.getRootBox().getChildren().add(itemBox);

        VBox.setVgrow(itemBox, Priority.ALWAYS);

        itemBox.setFillWidth(true);
        itemBox.setAlignment(Pos.CENTER);

        // Init item box for views
        SnapShotScene snapShotScene = new SnapShotScene(new SnapShotController(), FXMLPart.SNAPSHOTTER);

        snapShotScene.setBaseLine(300, 300);
        snapShotScene.setSnapperSize(width, height);

        itemBox.getChildren().add(snapShotScene.getRoot());

        customStage.show();
    }
}
