package com.window;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.goxr3plus.fxborderlessscene.borderless.CustomStage;
import com.enums.FXMLPart;
import com.scenes.titlebar.TitlebarController;
import com.scenes.titlebar.TitlebarScene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowedApplication {

    private CustomStage customStage;
    private BorderlessScene borderlessScene;

    private VBox rootBox;
    private TitlebarScene titlebarScene;

    private VBox itemBox;

    public WindowedApplication() {}

    public VBox createNewItemBox(){
        itemBox = new VBox();

        itemBox.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        itemBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        return itemBox;
    }

    public VBox createNewRootBox(int initialXSize , int initialYSize, String appCssUrl, String classname){
        rootBox = new VBox();

        rootBox.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        rootBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        rootBox.setMinSize(initialXSize, initialYSize);
        rootBox.getStylesheets().add(appCssUrl);
        rootBox.getStyleClass().add(classname);

        return rootBox;
    }

    public TitlebarScene createNewTitleBar(Stage mainStage, BorderlessScene borderlessScene){
        titlebarScene = new TitlebarScene(new TitlebarController(mainStage, borderlessScene), FXMLPart.TITLEBAR);
        titlebarScene.getController().init();

        return titlebarScene;
    }

    public BorderlessScene createNewBorderlessScene(CustomStage customStage , VBox rootBox){
        borderlessScene = customStage.craftBorderlessScene(rootBox);
        borderlessScene.removeDefaultCSS();
        customStage.setScene(borderlessScene);

        return borderlessScene;
    }

    public CustomStage createNewCustomStage(String title, String iconUrl){
        customStage = new CustomStage(StageStyle.UNDECORATED);

        customStage.getIcons().add(new Image(iconUrl));
        customStage.setTitle(title);
        customStage.setResizable(false);

        return customStage;
    }

    public VBox getRootBox() {
        return rootBox;
    }

    public VBox getItemBox() {
        return itemBox;
    }
}
