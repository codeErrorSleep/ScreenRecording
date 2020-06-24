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
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;
import org.openjfx.service.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalTime;



/**
 * @Description:    主界面控制类
 * @Author:         qiuShao
 * @CreateDate:     20-4-19 下午9:39
 */
public class MainPaneController {

    //  视频设置类
    private Video video;
    //  音频设置类
    private Audio audio;
    //  设置类
    private SettingUtils settingUtils;

    //　录制屏幕实例
    private VideoRecording videoRecording=new VideoRecording();
    //    录制摄像头实例
    private CameraRecording cameraRecording;
//    录制屏幕时显示摄像头的内容
    private DisplayCamera displayCamera;

    //  时间线控件(计算时间差)
    private Timeline timeline;

    //    判断是否打开麦克风标志
    private boolean isMicroPhone=false;
//    判断是否打开摄像头界面
    private boolean isDisplayCamera=false;

    //跳转设置界面
    @FXML
    private Button closeButton;
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

    @FXML
    private Button SettingButton;

    //    录制摄像头按钮
    @FXML
    private ToggleButton cameraButton;

//    是否打开摄像头按钮
    @FXML
    private ToggleButton showCamerasButton;
//  暂停按钮
    @FXML
    private ToggleButton pauseButton;


    //截图
    @FXML
    private void screenShots(){
        Stage stage = (Stage) cameraButton.getScene().getWindow();
        stage.hide();
        CaptureScreen captureScreen=new CaptureScreen();
        captureScreen.test();
        stage.show();
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
                java.time.Duration duration = java.time.Duration.between(startTime,endTime);
//            更换时间格式
                String hms = String.format("%d:%02d:%02d",
                        duration.toHoursPart(),
                        duration.toMinutesPart(),
                        duration.toSecondsPart());

                timeLabel.setText(hms);
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
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

//            新建视频音频属性类
            settingUtils=new SettingUtils();
            settingUtils.checkJsonFile();
            video=settingUtils.readVidioJSON();
            audio=settingUtils.readAudioJSON();
//            开始录制屏幕
//            videoRecording=new VideoRecording(video,audio, isMicroPhone);
            videoRecording.initialize(video,audio,isMicroPhone);
            videoRecording.start();

//          打开摄像头
            if (isDisplayCamera){
                displayCamera=new DisplayCamera();
                displayCamera.start();
            }

//            在开始后才开始计算时间
//            计算时间差
            computationTime(true);

        }else{
//            停止计算
            computationTime(false);
            videoRecording.stop();
//            关闭摄像头
            if (isDisplayCamera){
                displayCamera.stop();
            }

        }
    }



    /**
     * 暂停录制
     * @author      qiushao
     * @date        20-5-5 上午11:29
     */
    @FXML
    public void pauseRecording(){
        if(pauseButton.isSelected()){
            System.out.println("暂停录制");
            videoRecording.pause();
        }else{
            System.out.println("取消暂停");
            videoRecording.start();
        }

    }

    /**
     * 摄像头录制
     * @author      qiushao
     * @date        20-5-5 上午11:29
     */
    @FXML
    public void startCameraRecording(){
        if (cameraButton.isSelected()) {
//            计算时间差
            computationTime(true);
//            新建视频音频属性类
            settingUtils = new SettingUtils();
            settingUtils.checkJsonFile();
            video = settingUtils.readVidioJSON();
            audio = settingUtils.readAudioJSON();

            cameraRecording=new CameraRecording(video,audio);
            cameraRecording.start();
        }else{
            //            停止计算
            computationTime(false);
            cameraRecording.stop();
        }
    }





    //跳转设置界面
    @FXML
    private void getSettingPane(ActionEvent event) throws IOException {
        SettingPane settingPane=new SettingPane();
        try{
            settingPane.start(new Stage());
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.hide();

        } catch (Exception e){e.printStackTrace();}

    }


    //是否录制麦克风
    @FXML
    private void isOpenMicroPhone(ActionEvent event){
//       判断是否点击麦克风录制按钮
        if(microPhoneButton.isSelected()){
            isMicroPhone=true;
            System.out.println("使用麦克风");
        }
        else{
            isMicroPhone=false;
            System.out.println("麦克风");
        }

    }


//    是否打开摄像头界面
    @FXML
    private void isShowCamera(){
        if (showCamerasButton.isSelected()){
            isDisplayCamera=true;
            System.out.println("打开摄像头");
        }
        else{
            isDisplayCamera=false;
            System.out.println("关闭摄像头");

        }


    }


    @FXML
    private void closePane(ActionEvent event) throws IOException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();


    }

    @FXML
    private Button playerButton;
    @FXML
    private void openPlayer(ActionEvent event) throws IOException {


        PlayerPane playerPane=new PlayerPane();
        try{
            playerPane.start(new Stage());
        } catch (Exception e){e.printStackTrace();}

    }



}