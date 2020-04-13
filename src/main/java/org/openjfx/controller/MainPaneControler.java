package org.openjfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import org.openjfx.domain.Video;
import org.openjfx.service.VideoRecording;


public class MainPaneControler {


    private Video video;

    private VideoRecording videoRecording;

    @FXML
    private ToggleButton recordingButton;



    @FXML
    private void startRecording(ActionEvent event){
//        判断是否点击录制按钮８
        if (recordingButton.isSelected()){
            recordingButton.setText("test");
            video=new Video();
//            System.out.println(video.getFileName());
            videoRecording=new VideoRecording(video, false);
            videoRecording.start();

        }else{
            recordingButton.setText("Hello World, I am JavaFX!");
            videoRecording.stop();
        }
    }








}
