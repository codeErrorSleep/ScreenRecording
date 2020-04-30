package org.openjfx.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingPane extends Application {

    private static Scene scene;


    /**
     * 初始化设置文件
     * @author      qiushao
     * @date        20-4-15 下午10:47
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("settingPane"));

        stage.setTitle("设置");
        stage.setScene(scene);
        stage.show();


    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPane.class.getResource("../"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }



}
