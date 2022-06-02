package com;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;

public class SnapShotScene {

    private SnapShotController controller;
    private SnapShotter snap;

    public SnapShotScene() {
        try {
            this.controller = new SnapShotController();
            this.snap = new SnapShotter(this.controller);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setSceneSize(int width, int height){
        this.snap.setPrefSize(width ,height);
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

    public SnapShotController getController() {
        return controller;
    }

    public SnapShotter getRoot(){
        return this.snap;
    }
}
