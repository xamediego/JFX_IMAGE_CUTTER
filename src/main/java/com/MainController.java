package com;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainController extends Application {

    @FXML
    private ImageView image;
    @FXML
    private HBox filterBox;
    @FXML
    private HBox rec1;
    @FXML
    private HBox rec2;
    @FXML
    private StackPane stackPane;

    private FileChooser fileChooser = new FileChooser();
    private String filePath;

    private int imageWidth;
    private int imageHeight;


    @FXML
    private void selectImage() {
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

            filePath = selectedFile.getAbsolutePath();
            Image img = new Image(filePath);

            image.setImage(img);
            image.autosize();

            if (image.getFitWidth() < 300) {
                filterBox.setPrefWidth(image.getFitWidth());
            }

        }

        if (image.getImage().getHeight() > image.getBaselineOffset()) {
            double f = image.getBaselineOffset() / image.getImage().getHeight();
            imageWidth = (int) (f * image.getImage().getWidth());
        }

        System.out.println(imageWidth);

        System.out.println(imageWidth / 2);

        System.out.println((imageWidth / 2) - 150);


    }


    @Override
    public void start(Stage stage) {

    }


    //

    //I don't know why this works
    @FXML
    private void dragged(MouseEvent event) {
        if(image.getImage() != null) {
            final DragContext dragContext = new DragContext();

            if (filterBox.getLayoutX() > imageWidth / 2) {
                System.out.println("Over");
            }

            if (filterBox.getLayoutX() > (imageWidth / 2) - 150) {
                System.out.println("Under");
            }

            dragContext.mouseAnchorX = event.getX();

            rec1.setPrefWidth(rec1.getPrefWidth() + dragContext.mouseAnchorX);
            rec2.setPrefWidth(rec1.getPrefWidth() - dragContext.mouseAnchorX);
        }
}


private static final class DragContext {
    public double mouseAnchorX;
}


    @FXML
    private void save() {


        System.out.println(filterBox.getLayoutX());
//        File file = new File("croppedImage.jpg");
//
//        SnapshotParameters parameters = new SnapshotParameters();
//
//        parameters.setFill(Color.TRANSPARENT);
//
//        parameters.setViewport(new Rectangle2D(
//                filterBox.getLayoutX(), filterBox.getLayoutY(), 300, 300));
//
//        WritableImage wi = new WritableImage(300, 300);
//        image.snapshot(parameters, wi);
//
//        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
//        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);
//
//        Graphics2D graphics = bufImageRGB.createGraphics();
//        graphics.drawImage(bufImageARGB, 0, 0, null);
//
//        try {
//            ImageIO.write(bufImageRGB, "jpg", file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        graphics.dispose();
    }
}


