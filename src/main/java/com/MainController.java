package com;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ImageView image;
    @FXML
    private HBox XfilterBox;
    @FXML
    private HBox rec1;
    @FXML
    private HBox rec2;
    @FXML
    private HBox xFilter;
    @FXML
    private VBox yFilter;
    @FXML
    private VBox YfilterBox;
    @FXML
    private VBox vec1;
    @FXML
    private VBox vec2;
    @FXML
    private HBox midBox;

    private final FileChooser fileChooser = new FileChooser();

    int rightB = 0;

    int leftB = 0;

    int mid = 150;

    int baseLine = 300;

    float dragSpeed = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


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

                midBox.setPrefHeight(300);
                image.setFitHeight(300);

                double per = (image.getImage().getHeight() - image.getBaselineOffset()) / image.getImage().getHeight();

                int width = (int) (image.getImage().getWidth() - (image.getImage().getWidth() * per));

                leftB = mid - ((width / 2) - mid);
                rightB = mid + ((width / 2) - mid);

                enableXFilter();
                configXDrag();
            }

            if (image.getImage().getHeight() > image.getImage().getWidth()) {
                double per = (image.getImage().getHeight() - image.getBaselineOffset()) / image.getImage().getHeight();
                int width = (int) (image.getImage().getWidth() - (image.getImage().getWidth() * per));
                double per2 = ((image.getBaselineOffset() - width) / width);

                midBox.setPrefHeight(baseLine * (per2 + 1));
                image.setFitHeight(baseLine * (per2 + 1));

                System.out.println(midBox.getPrefHeight());


                enableYFilter();
                configYDrag();
            }

        }
    }

    private void configYDrag() {
        Vector2D location = new Vector2D();
        Vector2D velocity = new Vector2D();

        YfilterBox.setOnMousePressed(me -> {
            location.y = ((float) me.getY() - mid);
            System.out.println(location.y);
        });

        YfilterBox.setOnMouseDragged(me -> {

            if (image.getImage() != null) {

                velocity.y = (float) (me.getY() - mid);

                if (location.y < velocity.y) {
                    vec1.setPrefHeight(vec1.getPrefHeight() + dragSpeed);
                    vec2.setPrefHeight(vec2.getPrefHeight() - dragSpeed);
                }


                if (location.y > velocity.y) {
                    vec1.setPrefHeight(vec1.getPrefHeight() - dragSpeed);
                    vec2.setPrefHeight(vec2.getPrefHeight() + dragSpeed);
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

                if (XfilterBox.getLayoutX() < rightB - 2) {
                    if (location.x < velocity.x) {
                        rec1.setPrefWidth(rec1.getPrefWidth() + dragSpeed);
                        rec2.setPrefWidth(rec2.getPrefWidth() - dragSpeed);
                    }
                }

                if (XfilterBox.getLayoutX() > leftB + 2) {
                    if (location.x > velocity.x) {
                        rec1.setPrefWidth(rec1.getPrefWidth() - dragSpeed);
                        rec2.setPrefWidth(rec2.getPrefWidth() + dragSpeed);
                    }
                }
            }
        });
    }

    private void enableXFilter() {
        xFilter.setVisible(true);
        xFilter.setDisable(false);
    }

    private void disableXFilter() {
        xFilter.setVisible(false);
        xFilter.setDisable(true);
    }

    private void enableYFilter() {
        yFilter.setVisible(true);
        yFilter.setDisable(false);
    }

    private void disableYFilter() {
        yFilter.setVisible(false);
        yFilter.setDisable(true);
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


