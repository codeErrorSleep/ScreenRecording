package org.openjfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;

public class SettingPaneController {

    @FXML
    private TextField fileText;

    /**
     * 选择文件夹按钮
     */
    @FXML
    private Button showFileButton;
    private Window stage;

    @FXML
    private void screenShots() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        String path = file.getPath();//选择的文件夹路径
        fileText.setText(path);

        /*FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(stage);*/

    }


}



