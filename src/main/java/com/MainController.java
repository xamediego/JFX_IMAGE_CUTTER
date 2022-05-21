package com;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController{
    @FXML
    private ImageView image;

    @FXML
    private HBox xFilter;
    @FXML
    private HBox XfilterBox;
    @FXML
    private HBox rec1;
    @FXML
    private HBox rec2;

    @FXML
    private HBox Xbox;

    @FXML
    private VBox YBox;

    @FXML
    private VBox yFilter;
    @FXML
    private VBox YfilterBox;
    @FXML
    private VBox vec1;
    @FXML
    private VBox vec2;

    @FXML
    private Slider zoomLvl;

    @FXML
    private HBox midBox;

    private final FileChooser fileChooser = new FileChooser();

    int mid = 150;

    int baseLine = 300;

    float dragSpeed = 1;

    int width = 0;

    @FXML
    private void selectImage() {
        File selectedFile = fileChooser.showOpenDialog(null);

        disableXFilter();
        disableYFilter();

        if (selectedFile != null) {

            String filePath = selectedFile.getAbsolutePath();
            Image img = new Image(filePath);

            image.setImage(img);

            if (image.getImage().getWidth() > image.getImage().getHeight()) {

                midBox.setPrefHeight(baseLine);
                image.setFitHeight(baseLine);

                double per = (image.getImage().getHeight() - image.getBaselineOffset()) / image.getImage().getHeight();

                width = (int) (image.getImage().getWidth() - (image.getImage().getWidth() * per));

                xFilter.setPrefWidth(width);

                enableXFilter();
                configXDrag();
            }

            if (image.getImage().getHeight() > image.getImage().getWidth()) {

                double per = (image.getImage().getHeight() - image.getBaselineOffset()) / image.getImage().getHeight();
                int width = (int) (image.getImage().getWidth() - (image.getImage().getWidth() * per));
                double per2 = ((image.getBaselineOffset() - width) / width);

                midBox.setPrefHeight(baseLine * (per2 + 1));
                image.setFitHeight(baseLine * (per2 + 1));

                yFilter.setPrefHeight(image.getFitHeight());

                enableYFilter();
                configYDrag();
            }

        }
    }

    private void configYDrag() {
        Vector2D location = new Vector2D();
        Vector2D velocity = new Vector2D();

        YfilterBox.setOnMousePressed(me -> location.y = ((float) me.getY() - mid));

        YfilterBox.setOnMouseDragged(me -> {
            if (image.getImage() != null) {
                velocity.y = (float) (me.getY() - mid);
                if (vec1.getPrefHeight() < image.getFitHeight() - baseLine) {
                    if (location.y < velocity.y) {
                        vec1.setPrefHeight(vec1.getPrefHeight() + dragSpeed);
                        vec2.setPrefHeight(vec2.getPrefHeight() - dragSpeed);
                    }
                }
                if (vec2.getPrefHeight() < image.getFitHeight() - baseLine) {
                    if (location.y > velocity.y) {
                        vec1.setPrefHeight(vec1.getPrefHeight() - dragSpeed);
                        vec2.setPrefHeight(vec2.getPrefHeight() + dragSpeed);
                    }
                }
            }
        });
    }

    private void configXDrag() {
        Vector2D location = new Vector2D();
        Vector2D velocity = new Vector2D();

        XfilterBox.setOnMousePressed(me -> location.x = ((float) me.getX() - mid));

        XfilterBox.setOnMouseDragged(me -> {
            if (image.getImage() != null) {


                velocity.x = (float) (me.getX() - mid);
                if (rec1.getPrefWidth() < width - baseLine) {
                    if (location.x < velocity.x) {
                        rec1.setPrefWidth(rec1.getPrefWidth() + dragSpeed);
                        rec2.setPrefWidth(rec2.getPrefWidth() - dragSpeed);
                    }
                }
                if (rec2.getPrefWidth() < width - baseLine) {
                    if (location.x > velocity.x) {
                        rec1.setPrefWidth(rec1.getPrefWidth() - dragSpeed);
                        rec2.setPrefWidth(rec2.getPrefWidth() + dragSpeed);
                    }
                }

            }
        });
    }

    private void enableXFilter() {
        Xbox.setVisible(true);
        Xbox.setDisable(false);

        rec1.setPrefWidth(0);
        rec2.setPrefWidth(0);
    }

    private void disableXFilter() {
        Xbox.setVisible(false);
        Xbox.setDisable(true);
    }

    private void enableYFilter() {
        YBox.setVisible(true);
        YBox.setDisable(false);

        vec1.setPrefWidth(0);
        vec2.setPrefWidth(0);
    }

    private void disableYFilter() {
        YBox.setVisible(false);
        YBox.setDisable(true);
    }


    @FXML
    private void save() {
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


