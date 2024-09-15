package com.scenes.abstractscene;

import com.enums.FXMLPart;

import java.io.IOException;

public abstract class AbstractScene<T extends AbstractController> {

    private T controller;

    private AbstractRoot<T> root;

    public AbstractScene(T controller, FXMLPart fxmlPart) {
        try {
            this.controller = controller;
            this.root = new AbstractRoot<T>(this.controller, fxmlPart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T getController() {
        return controller;
    }

    public AbstractRoot<T> getRoot() {
        return this.root;
    }

    public void setController(T controller) {
        this.controller = controller;
    }
}
