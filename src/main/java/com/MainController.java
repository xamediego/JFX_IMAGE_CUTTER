package com;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ImageView selectedImage;
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

    @FXML
    private VBox sliderBox;
    @FXML
    private Slider zoomSlider;
    @FXML
    private Slider hSlider;
    @FXML
    private Slider vSlider;

    @FXML
    private ImageView avatarView;
    @FXML
    private HBox avatarBox;
    @FXML
    private Circle avatarCircle;

    @FXML
    private Rectangle filterRectangle;
    @FXML
    private Circle filterCircle;

    private final FileChooser fileChooser = new FileChooser();
    private Vector2D viewPortVector = new Vector2D();

    private float width, height;
    private double zoomlvl, viewWidth, offSetX, offSetY, dragX, dragY;

    int dragSpeed = 2;
    int mid = 150;

    int maxWidth = 550;
    int maxHeight = 500;

    float baseLine = 300;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideVHSlider();
    }

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

            configImageView();

            offSetX = width / 2;
            offSetY = height / 2;

            hSlider.setMax(width);
            vSlider.setMax(height);

            zoomSlider.setValue(1);

            setXDragSpeed();
            setYDragSpeed();

            setHorizontalView();
            setVerticalView();
            setZoomValue();
            configYDrag();
            activateCropBox();

        }
    }

    private void activateCropBox() {
        cropBox.setVisible(true);
        cropBox.setDisable(false);
    }

    private void resetValues() {
        viewPortVector = new Vector2D(0, 0);
        baseLine = 300;

        filterBox.setPrefWidth(baseLine);
        filterBox.setPrefHeight(baseLine);

        filterCircle.setRadius(baseLine / 2);

        filterRectangle.setHeight(baseLine);
        filterRectangle.setWidth(baseLine);

        selectedImage.setFitHeight(300);
        selectedImage.setFitWidth(0);

        rec1.setPrefWidth(Region.USE_COMPUTED_SIZE);
        rec2.setPrefWidth(Region.USE_COMPUTED_SIZE);

        vec1.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vec2.setPrefHeight(Region.USE_COMPUTED_SIZE);

    }

    private void configImageView() {
        if (width > height) {

            double per = (height - baseLine) / height;
            viewWidth = (int) (width - (width * per));

            if (viewWidth > maxWidth) {
                double ratio = -((viewWidth - maxWidth) / viewWidth);

                viewWidth = maxWidth;

                selectedImage.setFitHeight(baseLine * ratio);
                selectedImage.setFitWidth(viewWidth);

                filterRectangle.setHeight(selectedImage.getFitHeight());
                filterRectangle.setWidth(selectedImage.getFitHeight());

                filterCircle.setRadius(selectedImage.getFitHeight() / 2);

                filterBox.setPrefHeight(selectedImage.getFitHeight());
                filterBox.setPrefWidth(selectedImage.getFitHeight());

                baseLine = (float) selectedImage.getFitHeight();
            }
        }

        if (height > width) {
            double per = (height - baseLine) / height;
            viewWidth = (int) (width - (width * per));
            double per2 = ((baseLine - viewWidth) / viewWidth);

            viewWidth = baseLine;

            selectedImage.setFitHeight(baseLine * (per2 + 1));

            if (selectedImage.getFitHeight() > maxHeight) {
                double ratio = 1 - ((selectedImage.getFitHeight() - maxHeight) / selectedImage.getFitHeight());

                selectedImage.setFitHeight(maxHeight);
                selectedImage.setFitWidth(baseLine * ratio);

                filterRectangle.setHeight(selectedImage.getFitWidth());
                filterRectangle.setWidth(selectedImage.getFitWidth());

                filterCircle.setRadius(selectedImage.getFitWidth() / 2);

                filterBox.setPrefHeight(selectedImage.getFitWidth());
                filterBox.setPrefWidth(selectedImage.getFitWidth());

                baseLine = (float) selectedImage.getFitWidth();
                viewWidth = selectedImage.getFitWidth();
            }

        }
    }

    private void setXDragSpeed() {
        if (selectedImage.getImage().getWidth() > 600) {
            dragX = 0.75 + ((selectedImage.getImage().getWidth() - 600) * 0.005);
        } else {
            dragX = 0.75 - ((selectedImage.getImage().getWidth() - 600) * 0.005);
        }

        if (dragX < 0.3) {
            dragX = 0.3;
        }
        if (dragX > 2.2) {
            dragX = 2.2;
        }
    }

    private void setYDragSpeed() {
        if (selectedImage.getImage().getHeight() > 600) {
            dragY = 0.75 + ((selectedImage.getImage().getHeight() - 600) * 0.005);
        } else {
            dragY = 0.75 - ((selectedImage.getImage().getHeight() - 600) * 0.005);
        }

        if (dragY < 0.3) {
            dragY = 0.3;
        }
        if (dragY > 2.2) {
            dragY = 2.2;
        }
    }

    private void setZoomValue() {
        zoomSlider.valueProperty().addListener(e -> setZoomValue(zoomSlider.getValue()));
    }

    private void setHorizontalView() {
        hSlider.valueProperty().addListener(e -> setHorizontalValue(hSlider.getValue()));
    }

    private void setVerticalView() {
        vSlider.valueProperty().addListener(e -> setVerticalValue(vSlider.getValue()));
    }

    private void showVHSlider() {
        sliderBox.getChildren().add(hSlider);
        sliderBox.getChildren().add(vSlider);
    }

    private void hideVHSlider() {
        sliderBox.getChildren().remove(hSlider);
        sliderBox.getChildren().remove(vSlider);
    }

    private void setZoomValue(double value) {
        zoomlvl = value;

        double newValue = (double) ((int) (zoomlvl * 10)) / 10;

        setOffSetX(newValue);
        setOffSetY(newValue);

        hSlider.setValue(offSetX);
        vSlider.setValue(height - offSetY);
        selectedImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
    }

    private void setHorizontalValue(double value) {
        offSetX = value;
        zoomlvl = zoomSlider.getValue();

        double newValue = (double) ((int) (zoomlvl * 10)) / 10;

        setOffSetX(newValue);

        selectedImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
    }

    private void setVerticalValue(double value) {
        offSetY = height - value;
        zoomlvl = zoomSlider.getValue();

        double newValue = (double) ((int) (zoomlvl * 10)) / 10;

        setOffSetY(newValue);

        selectedImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
    }


    private void setOffSetX(double newValue) {
        if (offSetX < (width / newValue) / 2) {
            offSetX = (width / newValue) / 2;
        }
        if (offSetX > width - ((width / newValue) / 2)) {
            offSetX = width - ((width / newValue) / 2);
        }
    }

    private void setOffSetY(double newValue) {
        if (offSetY < (height / newValue) / 2) {
            offSetY = (height / newValue) / 2;
        }
        if (offSetY > height - ((height / newValue) / 2)) {
            offSetY = height - ((height / newValue) / 2);
        }
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
                } else {
                    vSlider.setValue(vSlider.getValue() - dragY);
                }

                if (vec2.getPrefHeight() < selectedImage.getFitHeight() - baseLine) {
                    if (location.y > velocity.y) {
                        vec1.setPrefHeight(vec1.getPrefHeight() - dragSpeed);
                        vec2.setPrefHeight(vec2.getPrefHeight() + dragSpeed);
                    }
                } else {
                    vSlider.setValue(vSlider.getValue() + dragY);
                }

                velocity.x = (float) (me.getX() - mid);

                if (rec1.getPrefWidth() < viewWidth - baseLine) {
                    if (location.x < velocity.x) {
                        rec1.setPrefWidth(rec1.getPrefWidth() + dragSpeed);
                        rec2.setPrefWidth(rec2.getPrefWidth() - dragSpeed);
                    }
                } else {
                    hSlider.setValue(hSlider.getValue() + dragX);

                }

                if (rec2.getPrefWidth() < viewWidth - baseLine) {
                    if (location.x > velocity.x) {
                        rec1.setPrefWidth(rec1.getPrefWidth() - dragSpeed);
                        rec2.setPrefWidth(rec2.getPrefWidth() + dragSpeed);
                    }
                } else {
                    hSlider.setValue(hSlider.getValue() - dragX);

                }

            }
        });
    }

    private void setXCrop() {
        if (rec1.getPrefWidth() >= rec2.getPrefWidth()) {
            viewPortVector.x = (float) ((viewWidth - baseLine) / 2 + (rec1.getPrefWidth() / 2));
        }

        if (rec1.getPrefWidth() <= rec2.getPrefWidth()) {
            viewPortVector.x = (float) ((viewWidth - baseLine) / 2 - (rec2.getPrefWidth() / 2));
        }
    }

    private void setYCrop() {
        if (vec1.getPrefHeight() >= vec2.getPrefHeight()) {
            viewPortVector.y = (float) ((selectedImage.getFitHeight() - baseLine) / 2 + (vec1.getPrefHeight() / 2));
        }

        if (vec1.getPrefHeight() <= vec2.getPrefHeight()) {
            viewPortVector.y = (float) ((selectedImage.getFitHeight() - baseLine) / 2 - (vec2.getPrefHeight() / 2));
        }
    }

    // TODO: 22/05/2022 add ability to crop gifs?
    @FXML
    private void save() {
        if (width > height) {
            setXCrop();
        }

        if (height > width) {
            setYCrop();
        }

        File file = new File("croppedImage.jpg");
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        parameters.setViewport(new Rectangle2D(
                viewPortVector.x, viewPortVector.y, baseLine, baseLine));

        WritableImage writableImage = new WritableImage((int) baseLine, (int) baseLine);

        selectedImage.snapshot(parameters, writableImage);

        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(writableImage, null);
        BufferedImage bufImageRGB = new BufferedImage(
                bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);

        try {
            ImageIO.write(bufImageRGB, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics.dispose();
        setNewAvatar(writableImage);
    }

    private void setNewAvatar(Image image) {
        avatarView.setImage(image);

        Circle circle = new Circle(avatarView.getBaselineOffset() / 2);

        circle.setLayoutX(avatarView.getFitWidth() /2);
        circle.setLayoutY(avatarView.getFitHeight() /2);

        avatarView.setClip(circle);
        avatarCircle.setVisible(true);
    }

}


