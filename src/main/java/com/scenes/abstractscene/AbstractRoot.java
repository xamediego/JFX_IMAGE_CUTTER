package com.scenes.abstractscene;

import com.enums.FXMLPart;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AbstractRoot<T extends AbstractController> extends VBox {
    public AbstractRoot(T controller, FXMLPart FXMLPart) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(FXMLPart.getPath()));

        loader.setController(controller);
        loader.setRoot(this);
        loader.load();
    }

}
