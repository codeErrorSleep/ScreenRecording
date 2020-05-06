package org.openjfx.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayerPane extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../mediaPlayer.fxml"));

        Parent root = fxmlLoader.load();
        PlayerPaneController controller = fxmlLoader.getController();
        primaryStage.setTitle("Media Player");
        primaryStage.setScene(new Scene(root, 640, 430));
        primaryStage.show();
        controller.init();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
