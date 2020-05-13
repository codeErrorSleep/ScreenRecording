package org.openjfx.service;


import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;

import javax.sound.sampled.*;
//import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;







public class test
{

    CanvasFrame canvas;
    private int frameRate=5;

    OpenCVFrameGrabber grabber;
    //录屏线程池 screenTimer
    private ScheduledThreadPoolExecutor shwoTable;


    public  void show() throws Exception{
//        canvas = new CanvasFrame("摄像头");//新建一个窗口
//        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new CanvasFrame("Capture Preview", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        canvas.setCanvasSize(640,480);



//        while(true){
//            if(!canvas.isDisplayable()){//窗口是否关闭
//                grabber.stop();//停止抓取
//                System.exit(2);//退出
//                break;
//            }
//            canvas.showImage(grabber.grab());//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
////            Thread.sleep(10);//50毫秒刷新一次图像
//        }
    }


    public void start() throws Exception{
        grabber = new OpenCVFrameGrabber(0);
        grabber.setImageWidth(640);
        grabber.setImageHeight(480);

        grabber.start();   //开始获取摄像头数据


        shwoTable = new ScheduledThreadPoolExecutor(1);
        shwoTable.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try{
                    show();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }, (int) (1000 / frameRate), (int) (1000 / frameRate), TimeUnit.MILLISECONDS);




    }

}