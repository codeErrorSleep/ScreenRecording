package org.openjfx.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;
import org.openjfx.service.CaptureScreen;
import org.openjfx.service.VideoRecording;

import java.io.IOException;
import java.time.LocalTime;

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
//  时间线控件(计算时间差)
    private Timeline timeline;

    //    判断是否打开麦克风标志
    private boolean isMicroPhone=false;

    private CaptureScreen captureScreen;



//    录制按钮
    @FXML
    private ToggleButton recordingButton;

//  选择是否使用麦克风
    @FXML
    private ToggleButton microPhoneButton;

//    时间
    @FXML
    private Label timeLabel;

//    截图
    @FXML
    private Button screenShotsButton;


//截图
    @FXML
    private void screenShots(){
//            captureScreen=new CaptureScreen();
//            captureScreen.processingImages();
//            captureScreen.saveFile();
//            BufferedImage image = robot.createScreenCapture(screenRectangle);

    }



    /**
    * 计算录制时间
    * @author      qiushao
    * @date        20-4-21 下午9:45
    */
    private void computationTime(boolean isRecording){
        if(isRecording){
            LocalTime startTime=LocalTime.now();
            timeline = new Timeline(new KeyFrame(Duration.millis(1000), arg1->{
                LocalTime endTime=LocalTime.now();
//            计算时间差
                java.time.Duration duration = java.time.Duration.between(startTime,endTime );
//            更换时间格式
                String hms = String.format("%d:%02d:%02d",
                        duration.toHoursPart(),
                        duration.toMinutesPart(),
                        duration.toSecondsPart());

                timeLabel.setText(hms);
            }));
            timeline.setCycleCount(Animation.INDEFINITE );
            timeline.play();
        }
        else{
            timeline.stop();
            timeLabel.setText("0:00:00");
        }
    }


    /**
    * 开始录制函数
    * @author      qiushao
    * @date        20-4-21 下午9:48
    */
    @FXML
    private void startRecording(ActionEvent event){
//        判断是否点击录制按钮８
        if (recordingButton.isSelected()){
//            计算时间差
            computationTime(true);
            recordingButton.setText("正在录制");
//            新建视频音频属性类
            video=new Video();
            audio=new Audio();

//            System.out.println(video.getFileName());
            videoRecording=new VideoRecording(video,audio, isMicroPhone);
            videoRecording.start();

        }else{
//            停止计算
            computationTime(false);
            recordingButton.setText("录制");
            videoRecording.stop();
        }
    }


//跳转设置界面
    @FXML
    private void getSettingPane(ActionEvent event) throws IOException {
        SettingPane settingPane=new SettingPane();
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
