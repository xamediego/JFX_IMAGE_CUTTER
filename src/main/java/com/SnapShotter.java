package com;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SnapShotter extends VBox {

    public SnapShotter(SnapShotController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FxmlFiles/main.fxml"));

        loader.setController(controller);
        loader.setRoot(this);
        loader.load();
    }

}
