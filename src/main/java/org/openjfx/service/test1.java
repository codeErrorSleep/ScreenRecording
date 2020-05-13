package org.openjfx.service;


import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;

import javax.sound.sampled.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;




/**
 * @Description:    录制摄像头
 * @Author:         qiuShao
 * @CreateDate:     20-5-4 下午8:44
 */
public class test1 {

    //录屏线程池 screenTimer
    private ScheduledThreadPoolExecutor screenTimer;
    //    音频设置线程池
    private ScheduledThreadPoolExecutor exec;
    private long startTime = 0;
    private long videoTS = 0;

    //    摄像头设备指标
    private int WEBCAM_DEVICE_INDEX=0;
    //    音频设备指标
    private int AUDIO_DEVICE_INDEX=4;

    //    录制分辨率
    private int captureWidth=640;
    private int captureHeight=480;

    //  帧率
    private int frameRate=5;
    //  摄像头显示
    CanvasFrame cFrame;
    //    摄像头的录制类
    OpenCVFrameGrabber grabber;
    //    录像的实体类
    FFmpegFrameRecorder recorder;
    //  音频设置类
    AudioFormat audioFormat;


    /**
     * 初始化视频参数
     * @author      qiushao
     * @date        20-5-5 上午10:51
     */
    public test1(Video video, Audio audio) {

//        设置分辨率
        captureWidth=video.getVideoWidth();
        captureHeight=video.getVideoHeigth();
//      设置帧率
        frameRate=(int) video.getFrameRate();


        grabber = new OpenCVFrameGrabber(WEBCAM_DEVICE_INDEX);
        grabber.setImageWidth(captureWidth);
        grabber.setImageHeight(captureHeight);

    }



    /**
     * 处理每一针的画面
     * @author      qiushao
     * @date        20-5-5 上午9:55
     */
    public void dealImage() throws org.bytedeco.javacv.FrameGrabber.Exception {
//        摄像头显示
        // javaCV提供了优化非常好的硬件加速组件来帮助显示我们抓取的摄像头视频
        cFrame = new CanvasFrame("Capture Preview", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        cFrame.setCanvasSize(640,480);
        Frame capturedFrame = null;
//        合并图片成视频
        // 执行抓取（capture）过程
        while ((capturedFrame = grabber.grab()) != null) {
            if (cFrame.isVisible()) {
                //本机预览要发送的帧
                cFrame.showImage(capturedFrame);
            }
        }


    }



    /**
     * 开始录制
     */
    public void start(){
//        开始录制
        System.out.println("开始录制...");
        try {
            grabber.start();
            recorder.start();
        } catch (Exception e) {
            System.out.println(e);
        }
        // 如果有录音设备则启动录音线程

        // 录屏
        screenTimer = new ScheduledThreadPoolExecutor(1);
        screenTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try{
                    dealImage();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }, (int) (1000 / frameRate), (int) (1000 / frameRate), TimeUnit.MILLISECONDS);

    }


    /**
     * 停止录制,关闭线程池,释放资源
     * @author      qiushao
     * @date        20-5-5 上午9:58
     */
    public void stop(){
        //        检测关闭
        cFrame.dispose();
//        关闭两个线程池
        if (null != screenTimer){
            screenTimer.shutdownNow();

        }
//        关闭录制资源
        try {
            if(null!=grabber){
                grabber.stop();
                grabber.release();
                grabber.close();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }



//
//    public static void main(String[] args) throws Exception {
////        Loader.load(opencv_objdetect.class);
//        CameraRecording(
//                "test3",640,480,25);
//    }
}
