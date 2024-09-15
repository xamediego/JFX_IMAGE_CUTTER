package com.scenes.titlebar;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.scenes.abstractscene.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class TitlebarController extends AbstractController {

    @FXML
    private VBox titleBox;

    @FXML
    private FontIcon minMaxIcon;

    private final BorderlessScene borderlessScene;

    private final Stage mainStage;

    public TitlebarController(Stage mainStage, BorderlessScene borderlessScene) {
        this.mainStage = mainStage;
        this.borderlessScene = borderlessScene;
    }

    public void init(){
        borderlessScene.setMoveControl(titleBox);
    }

    @FXML
    private void minimize(){
        mainStage.setIconified(true);
    }

    @FXML
    private void exit(){
        mainStage.close();
    }

    @FXML
    private void minMax(){
        borderlessScene.maximizeStage();
        iconCheck();
    }

    private void iconCheck(){
        if(borderlessScene.isMaximized()){
            minMaxIcon.setIconLiteral("far-window-maximize");
        } else {
            minMaxIcon.setIconLiteral("far-window-restore");
        }
    }

}
