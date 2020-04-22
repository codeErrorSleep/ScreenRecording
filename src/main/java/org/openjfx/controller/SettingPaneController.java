package org.openjfx.controller;


import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class SettingPaneController {


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




    @FXML
    private void getLocalFile(ActionEvent event){
        showFileButton.setText("test");

    }





}
