package com;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;

public class SnapShot {

    public static SnapshotParameters getSnapShot(Vector2D xy, float baseLine){
        SnapshotParameters parameters = new SnapshotParameters();

        parameters.setFill(Color.TRANSPARENT);

        parameters.setViewport(new Rectangle2D(xy.x, xy.y, baseLine, baseLine));

        return parameters;
    }

}
