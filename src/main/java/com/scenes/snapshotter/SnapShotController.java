package com.scenes.snapshotter;

import com.scenes.abstractscene.AbstractController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SnapShotController extends AbstractController implements Initializable {

    @FXML
    private ImageView selectedImage;
    @FXML
    private HBox imageBox;
    @FXML
    private HBox cropBox;
    @FXML
    private HBox midBox;

    @FXML
    private ImageView avatarView;

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
    private Rectangle filterRectangle;
    @FXML
    private Circle filterCircle;

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private final FileChooser fileChooser = new FileChooser();
    private Vector2D viewPortVector = new Vector2D();

    private float width, height;
    private double zoomLvl, viewWidth, offSetX, offSetY, dragX, dragY;

    int dragSpeed = 2;

    private int maxWidth = 600;
    private int maxHeight = 425;

    Vector2D currentBase = new Vector2D(), startBase = new Vector2D();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideVHSlider();
    }

    // ----- Methods that change the base values for the filters etc -----

    public void setMidBoxSize(int width, int height) {
        midBox.setPrefSize(width, height);
    }

    public void setFilterSize(int x, int y) {
        startBase.x = x;
        startBase.y = y;
    }

    public void setMaxHeightWidth(int width, int height) {
        maxWidth = width;
        maxHeight = height;
    }

    // ----- Methods for image selection -----

    @FXML
    private void selectImage() {
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            System.out.println(filePath);

            configureSnapper(new Image(filePath));
        }

    }

    public void setImage(Image image) {
        configureSnapper(image);
    }

    private void configureSnapper(Image sourceImage) {
        saveButton.setDisable(false);
        sliderBox.setDisable(false);
        sliderBox.setVisible(true);

        resetValues();

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
        configDrag();
        activateCropBox();
    }

    private void activateCropBox() {
        cropBox.setVisible(true);
        cropBox.setDisable(false);
    }

    private void resetValues() {
        viewPortVector = new Vector2D(0, 0);

        currentBase = new Vector2D(startBase.x, startBase.y);

        filterBox.setPrefSize(currentBase.x, currentBase.y);

        filterBox.setPrefSize(currentBase.x, currentBase.y);

        filterCircle.setRadius(currentBase.x / 2);

        filterRectangle.setHeight(currentBase.y);
        filterRectangle.setWidth(currentBase.x);


        selectedImage.setFitHeight(currentBase.y);
        selectedImage.setFitWidth(0);

        selectedImage.setViewport(new Rectangle2D(0, 0, 0, 0));

        rec1.setPrefWidth(Region.USE_COMPUTED_SIZE);
        rec2.setPrefWidth(Region.USE_COMPUTED_SIZE);

        vec1.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vec2.setPrefHeight(Region.USE_COMPUTED_SIZE);
    }


    private void configImageView() {
        if (width > height) {
            setHorizontalImage();
        }

        if (height > width) {
            setVerticalImage();
        }

        if (height == width) {
            viewWidth = startBase.x;
        }

    }

    private void setHorizontalImage() {
        double per = (height - currentBase.y) / height;

        viewWidth = (int) (width - (width * per));

        if (viewWidth > maxWidth) {
            maxWidthAdjustment();
        }

    }

    private void maxWidthAdjustment() {
        double ratio = 1 - ((viewWidth - maxWidth) / viewWidth);

        viewWidth = maxWidth;

        selectedImage.setFitHeight(currentBase.y * ratio);
        selectedImage.setFitWidth(0);

        filterRectangle.setHeight(selectedImage.getFitHeight());
        filterRectangle.setWidth(currentBase.x * ratio);

        filterCircle.setRadius(selectedImage.getFitHeight() / 2);

        filterBox.setPrefSize(currentBase.x * ratio, selectedImage.getFitHeight());

        currentBase.y = (float) selectedImage.getFitHeight();
        currentBase.x = (float) filterBox.getPrefWidth();
    }


    private void setVerticalImage() {
        double per = (height - currentBase.y) / height;
        viewWidth = (int) (width - (width * per));

        double per2 = ((currentBase.x - viewWidth) / viewWidth);

        viewWidth = currentBase.x;
        selectedImage.setFitHeight(currentBase.y * (per2 + 1));

        if (selectedImage.getFitHeight() < currentBase.y) {
            adjustToYBase();
        }

        if (selectedImage.getFitHeight() > maxHeight) {
            maxHeightAdjustment();
        }
    }

    private void adjustToYBase() {
        double per3 = 1 + ((currentBase.y - selectedImage.getFitHeight()) / selectedImage.getFitHeight());

        selectedImage.setFitHeight(currentBase.y);
        selectedImage.setFitWidth(currentBase.x * per3);

        viewWidth = selectedImage.getFitWidth();
    }

    private void maxHeightAdjustment() {
        double ratio = 1 - ((selectedImage.getFitHeight() - maxHeight) / selectedImage.getFitHeight());

        selectedImage.setFitHeight(maxHeight);
        selectedImage.setFitWidth(currentBase.x * ratio);

        filterRectangle.setHeight(currentBase.y * ratio);
        filterRectangle.setWidth(selectedImage.getFitWidth());

        filterCircle.setRadius(selectedImage.getFitWidth() / 2);

        filterBox.setPrefSize(selectedImage.getFitWidth(), currentBase.y * ratio);

        currentBase.x = (float) selectedImage.getFitWidth();
        currentBase.y *= ratio;
        viewWidth = selectedImage.getFitWidth();
    }

    // ----- Methods used to make the box move around the image -----


    // ----- dragSpeed -----
    private void setXDragSpeed() {
        if (selectedImage.getImage().getWidth() > maxWidth) {
            dragX = 0.50 + ((selectedImage.getImage().getWidth() - maxWidth) * 0.00075);
        } else {
            dragX = 0.50 - ((selectedImage.getImage().getWidth() - maxWidth) * 0.00075);
        }

        if (dragX < 0.3) {
            dragX = 0.7;
        }
        if (dragX > 1.00) {
            dragX = 1;
        }

    }


    private void setYDragSpeed() {
        if (selectedImage.getImage().getHeight() > maxWidth) {
            dragY = 0.50 + ((selectedImage.getImage().getHeight() - maxWidth) * 0.00075);
        } else {
            dragY = 0.50 - ((selectedImage.getImage().getHeight() - maxWidth) * 0.00075);
        }

        if (dragY < 0.3) {
            dragY = 0.7;
        }
        if (dragY > 1.00) {
            dragY = 1;
        }
    }

    private void setZoomValue() {
        zoomSlider.valueProperty().addListener(e -> setZoomValue(zoomSlider.getValue()));
    }

    // ----- Slider hack -----
    private void setHorizontalView() {
        hSlider.valueProperty().addListener(e -> setHorizontalValue(hSlider.getValue()));
    }

    private void setVerticalView() {
        vSlider.valueProperty().addListener(e -> setVerticalValue(vSlider.getValue()));
    }

    private void hideVHSlider() {
        sliderBox.getChildren().remove(hSlider);
        sliderBox.getChildren().remove(vSlider);
    }

    // ----- When zooming -----

    private void setZoomValue(double value) {
        zoomLvl = value;

        double newValue = (double) ((int) (zoomLvl * 10)) / 10;

        setOffSetX(newValue);
        setOffSetY(newValue);

        hSlider.setValue(offSetX);
        vSlider.setValue(height - offSetY);
        selectedImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
    }

    private void setHorizontalValue(double value) {
        offSetX = value;
        zoomLvl = zoomSlider.getValue();

        double newValue = (double) ((int) (zoomLvl * 10)) / 10;

        setOffSetX(newValue);

        selectedImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
    }

    private void setVerticalValue(double value) {
        offSetY = height - value;
        zoomLvl = zoomSlider.getValue();

        double newValue = (double) ((int) (zoomLvl * 10)) / 10;

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

    // ----- configuring draging over the image -----

    private void configDrag() {
        Vector2D location = new Vector2D();
        Vector2D velocity = new Vector2D();

        filterBox.setOnMousePressed(me -> {
            location.y = (float) (me.getY() - viewWidth);
            location.x = 0;

            imageBox.setCursor(Cursor.CLOSED_HAND);
        });

        filterBox.setOnMouseDragged(me -> {
            if (selectedImage.getImage() != null) {

                imageBox.setCursor(Cursor.OPEN_HAND);

                velocity.y = (float) (me.getY() - viewWidth);

                if (vec1.getPrefHeight() < selectedImage.getFitHeight() - currentBase.y) {
                    if (location.y < velocity.y) {
                        vec1.setPrefHeight(vec1.getPrefHeight() + dragSpeed);
                        vec2.setPrefHeight(vec2.getPrefHeight() - dragSpeed);
                    }
                } else {
                    vSlider.setValue(vSlider.getValue() - dragY);
                }

                if (vec2.getPrefHeight() < selectedImage.getFitHeight() - currentBase.y) {
                    if (location.y > velocity.y) {
                        vec1.setPrefHeight(vec1.getPrefHeight() - dragSpeed);
                        vec2.setPrefHeight(vec2.getPrefHeight() + dragSpeed);
                    }
                } else {
                    vSlider.setValue(vSlider.getValue() + dragY);
                }

                velocity.x = (float) me.getX();

                if (rec1.getPrefWidth() < viewWidth - currentBase.x) {
                    if (location.x < velocity.x) {
                        rec1.setPrefWidth(rec1.getPrefWidth() + dragSpeed);
                        rec2.setPrefWidth(rec2.getPrefWidth() - dragSpeed);
                    }
                } else {
                    hSlider.setValue(hSlider.getValue() + dragX);
                }

                if (rec2.getPrefWidth() < viewWidth - currentBase.x) {

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

    // ----- configuring crop coordinates -----

    private void setXCrop() {
        if (rec1.getPrefWidth() >= rec2.getPrefWidth()) {
            viewPortVector.x = (float) ((viewWidth - currentBase.x) / 2 + (rec1.getPrefWidth() / 2));
        }

        if (rec1.getPrefWidth() <= rec2.getPrefWidth()) {
            viewPortVector.x = (float) ((viewWidth - currentBase.x) / 2 - (rec2.getPrefWidth() / 2));
        }
    }

    private void setYCrop() {
        if (vec1.getPrefHeight() >= vec2.getPrefHeight()) {
            viewPortVector.y = (float) ((selectedImage.getFitHeight() - currentBase.y) / 2 + (vec1.getPrefHeight() / 2));
        }

        if (vec1.getPrefHeight() <= vec2.getPrefHeight()) {
            viewPortVector.y = (float) ((selectedImage.getFitHeight() - currentBase.y) / 2 - (vec2.getPrefHeight() / 2));
        }
    }

    // ----- Methods concerning the buttons on the scene -----

    public BufferedImage getCroppedImage() {
        if (width > height) {
            setXCrop();
        }

        if (height > width) {
            setYCrop();
        }

        WritableImage writableImage = new WritableImage((int) currentBase.x, (int) currentBase.y);

        SnapshotParameters parameters = new SnapshotParameters();

        parameters.setFill(javafx.scene.paint.Color.TRANSPARENT);

        parameters.setViewport(new Rectangle2D(viewPortVector.x, viewPortVector.y, currentBase.x, currentBase.y));

        selectedImage.snapshot(parameters, writableImage);

        setAvatar(writableImage);

        return SwingFXUtils.fromFXImage(writableImage, null);
    }

    // ----- setting result avatar -----

    @FXML
    public void setAvatar(Image image) {
        avatarView.setImage(image);

        Circle circle = new Circle(avatarView.getBaselineOffset() / 2);

        circle.setLayoutX(avatarView.getFitWidth() / 2);
        circle.setLayoutY(avatarView.getFitHeight() / 2);

        avatarView.setClip(circle);
    }

    @FXML
    private void save() {

        try {
            ImageIO.write(getCroppedImage(), "png", new File("defaultItemBoxImage.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void cancel() {
        System.exit(0);
    }

    public void setSaveEvent(EventHandler<ActionEvent> event) {
        saveButton.setOnAction(event);
    }

    public void setCancelEvent(EventHandler<ActionEvent> event) {
        cancelButton.setOnAction(event);
    }

    public void setCircleVisible(boolean set) {
        this.filterCircle.setVisible(set);
    }
}