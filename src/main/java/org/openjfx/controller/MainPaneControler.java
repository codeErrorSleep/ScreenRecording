package org.openjfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;
import org.openjfx.service.VideoRecording;

import java.io.IOException;

/**
* @Description:    主界面控制类
* @Author:         qiuShao
* @CreateDate:     20-4-19 下午9:39
*/
public class MainPaneControler {

//  视频设置类
    private Video video;
//  音频设置类
    private Audio audio;
//　录制实例
    private VideoRecording videoRecording;


//    判断是否打开麦克风标志
    private boolean isMicroPhone=false;


//    录制按钮
    @FXML
    private ToggleButton recordingButton;

//  选择是否使用麦克风
    @FXML
    private ToggleButton microPhoneButton;


//开始录制函数
    @FXML
    private void startRecording(ActionEvent event){
//        判断是否点击录制按钮８
        if (recordingButton.isSelected()){
            recordingButton.setText("正在录制");
//            新建视频音频属性类
            video=new Video();
            audio=new Audio();

//            System.out.println(video.getFileName());
            videoRecording=new VideoRecording(video,audio, isMicroPhone);
            videoRecording.start();

        }else{
            recordingButton.setText("录制");
            videoRecording.stop();
        }
    }


//跳转设置界面
    @FXML
    private void getSettingPane(ActionEvent event) throws IOException {
        SettingPaneController settingPane=new SettingPaneController();
        try{
            settingPane.start(new Stage());
        } catch (Exception e){e.printStackTrace();}

    }


//是否录制麦克风
    @FXML
    private void isOpenMicroPhone(ActionEvent event){
//       判断是否点击麦克风录制按钮
        if(microPhoneButton.isSelected()){
            isMicroPhone=true;
            microPhoneButton.setText("使用麦克风");
        }
        else{
            isMicroPhone=false;
            microPhoneButton.setText("麦克风");
        }

    }




}
