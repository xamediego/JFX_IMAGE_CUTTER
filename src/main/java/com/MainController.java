package com;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    private ImageView selectedImage;

    private final FileChooser fileChooser = new FileChooser();

    @FXML
    private Slider zoomSlider;

    @FXML
    private HBox imageBox;

    @FXML
    private HBox cropBox;

    @FXML
    private HBox filterBox;

    @FXML
    private HBox rec1;

    @FXML
    private HBox rec2;

    @FXML
    private VBox vec1;

    @FXML
    private VBox vec2;

    Vector2D initial = new Vector2D();

    private int width, height;
    private double zoomlvl, hValue, vValue, viewWidth, offSetX, offSetY;

    int dragSpeed = 2;

    int mid = 150;

    int baseLine = 300;

    @FXML
    private void selectImage() {
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            resetValues();

            String filePath = selectedFile.getAbsolutePath();

            Image sourceImage = new Image(filePath);

            selectedImage.setImage(sourceImage);

            height = (int) sourceImage.getHeight();
            width = (int) sourceImage.getWidth();

            if (width > height) {
                double per = (height - selectedImage.getBaselineOffset()) / height;
                viewWidth = (int) (width - (width * per));
            }

            if (height > width) {
                double per = (height - selectedImage.getBaselineOffset()) / height;
                int viewWidth = (int) (width - (width * per));
                double per2 = ((selectedImage.getBaselineOffset() - viewWidth) / viewWidth);

                selectedImage.setFitHeight(selectedImage.getBaselineOffset() * (per2 + 1));
                imageBox.setPrefHeight(selectedImage.getBaselineOffset() * (per2 + 1));

            }
            configZoomsSlide();

            activeFilterPane();

            configYDrag();
        }
    }

    private void resetValues() {
        setZoomValue(1);

        selectedImage.setFitHeight(baseLine);
        imageBox.setPrefHeight(baseLine);
    }

    private void configZoomsSlide() {
        zoomSlider.valueProperty().addListener(e -> setZoomValue(zoomSlider.getValue()));
    }

    private void setZoomValue(double value) {
        zoomlvl = value;

        double newValue = (double) ((int) (zoomlvl * 10)) / 10;

        if (offSetX < (width / newValue) / 2) {
            offSetX = (width / newValue) / 2;
        }

        if (offSetX > width - ((width / newValue) / 2)) {
            offSetX = width - ((width / newValue) / 2);
        }

        if (offSetY < (height / newValue) / 2) {
            offSetY = (height / newValue) / 2;
        }

        if (offSetY > height - ((height / newValue) / 2)) {
            offSetY = height - ((height / newValue) / 2);
        }
        selectedImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
    }

    private void setHorizontalView(double value) {
        int f = 2;

        offSetX = value;
        zoomlvl = zoomSlider.getValue();

        double newValue = (double) ((int) (zoomlvl * 10)) / 10;

        if (offSetX < (width / newValue) / f) {
            offSetX = (width / newValue) / f;
        }
        if (offSetX > width - ((width / newValue) / f)) {
            offSetX = width - ((width / newValue) / f);
        }

        selectedImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / f), offSetY - ((height / newValue) / f), width / newValue, height / newValue));

    }

    private void setVerticalView(double value) {
        int f = 2;

        offSetY = height - value;
        zoomlvl = zoomSlider.getValue();

        double newValue = (double) ((int) (zoomlvl * 10)) / 10;

        if (offSetY < (height / newValue) / f) {
            offSetY = (height / newValue) / f;
        }

        if (offSetY > height - ((height / newValue) / f)) {
            offSetY = height - ((height / newValue) / f);
        }

        selectedImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / f), offSetY - ((height / newValue) / f), width / newValue, height / newValue));
    }

    private void configYDrag() {
        Vector2D location = new Vector2D();
        Vector2D velocity = new Vector2D();

        filterBox.setOnMousePressed(me -> {
            location.y = (float) (me.getY() - viewWidth);

            imageBox.setCursor(Cursor.CLOSED_HAND);
        });

        filterBox.setOnMouseDragged(me -> {
            if (selectedImage.getImage() != null) {

                imageBox.setCursor(Cursor.OPEN_HAND);

                velocity.y = (float) (me.getY() - viewWidth);

                if (vec1.getPrefHeight() < selectedImage.getFitHeight() - baseLine) {
                    if (location.y < velocity.y) {
                        vec1.setPrefHeight(vec1.getPrefHeight() + dragSpeed);
                        vec2.setPrefHeight(vec2.getPrefHeight() - dragSpeed);
                    }
                }

                if (vec2.getPrefHeight() < selectedImage.getFitHeight() - baseLine) {
                    if (location.y > velocity.y) {
                        vec1.setPrefHeight(vec1.getPrefHeight() - dragSpeed);
                        vec2.setPrefHeight(vec2.getPrefHeight() + dragSpeed);
                    }
                }

                velocity.x = (float) (me.getX() - mid);

                if (rec1.getPrefWidth() < viewWidth - baseLine) {
                    if (location.x < velocity.x) {
                        rec1.setPrefWidth(rec1.getPrefWidth() + dragSpeed);
                        rec2.setPrefWidth(rec2.getPrefWidth() - dragSpeed);
                    }
                }

                if (rec2.getPrefWidth() < viewWidth - baseLine) {
                    if (location.x > velocity.x) {
                        rec1.setPrefWidth(rec1.getPrefWidth() - dragSpeed);
                        rec2.setPrefWidth(rec2.getPrefWidth() + dragSpeed);
                    }
                }

            }
        });
    }


    private void activeFilterPane() {
        cropBox.setVisible(true);
        cropBox.setDisable(false);
    }

    private void disableFilterPane() {
        cropBox.setVisible(true);
        cropBox.setDisable(true);
    }


    @FXML
    private void save() {
        File file = new File("croppedImage.jpg");

        SnapshotParameters parameters = new SnapshotParameters();

        parameters.setFill(Color.TRANSPARENT);

        int avatarSize = 300;
        parameters.setViewport(new Rectangle2D(
                imageBox.getLayoutX(), imageBox.getLayoutY(), avatarSize, avatarSize));

        WritableImage wi = new WritableImage(avatarSize, avatarSize);

        selectedImage.snapshot(parameters, wi);

        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);

        try {
            ImageIO.write(bufImageRGB, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics.dispose();
    }

}


