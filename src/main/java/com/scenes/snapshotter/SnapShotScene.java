package com.scenes.snapshotter;

import com.enums.FXMLPart;
import com.scenes.abstractscene.AbstractScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SnapShotScene extends AbstractScene<SnapShotController> {

    private final SnapShotController controller;

    public SnapShotScene(SnapShotController controller, FXMLPart fxmlPart) {
        super(controller, fxmlPart);

        this.controller = controller;
    }

    public void setSnapperSize(int width, int height){
        this.getRoot().setPrefSize(width, height);
    }

    public void setMidBoxSize(int width, int height){
        this.controller.setMidBoxSize(width,height);
    }

    public void setBaseLine(int x, int y){
        this.controller.setFilterSize(x, y);
    }

    public void setImageMaxSize(int width, int height){
        this.controller.setMaxHeightWidth(width,height);
    }

    public void setCircle(boolean set){
        this.controller.setCircleVisible(set);
    }

    public void setSaveEvent(EventHandler<ActionEvent> event){
        this.controller.setSaveEvent(event);
    }

    public void setCancelEvent(EventHandler<ActionEvent> event){
        this.controller.setCancelEvent(event);
    }
}
