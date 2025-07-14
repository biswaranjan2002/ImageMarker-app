package com.example.practiceangledraw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ImageLablerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("imageLabeler.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Image Labeler Application");
        stage.setScene(scene);
        stage.setWidth(1980);
        stage.setHeight(1080);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}