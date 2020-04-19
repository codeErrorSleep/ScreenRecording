package org.openjfx.controller;


import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.openjfx.App;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class SettingPaneController extends Application {

    private static Scene scene;

    /**
    * 选择文件夹按钮
    */
    @FXML
    private Button showFileButton;

    /**
    * 视频帧率选择
    */
    @FXML
    private ComboBox<String> frameRateComboBox;

    /**
     * 初始化设置文件
    * @author      qiushao
    * @date        20-4-15 下午10:47
    */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("settingPane"));

//        //        初始化分辨率下拉选择框
//        frameRateComboBox.getItems().addAll(
//                "5","15","30","60");


        stage.setTitle("设置");
        stage.setScene(scene);
        stage.show();


    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }


    @FXML
    private void getLocalFile(ActionEvent event){
        showFileButton.setText("test");

    }





}
