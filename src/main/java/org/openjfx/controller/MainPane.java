package org.openjfx.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class MainPane extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {


        scene = new Scene(loadFXML("MainPane"));
        //隐藏标题
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        AtomicReference<Double> xOffSet= new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffSet= new AtomicReference<>((double) 0);
        scene.setOnMousePressed(event -> {
            xOffSet.set(event.getSceneX());
            yOffSet.set(event.getSceneY());
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffSet.get());
            stage.setY(event.getScreenY() - yOffSet.get());
        });
        stage.show();

    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPane.class.getResource("../" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
}

