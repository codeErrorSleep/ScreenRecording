package org.openjfx.service;


import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;

import javax.sound.sampled.AudioFormat;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
* @Description:    显示摄像头内容,不保存.减少资源使用(用于录制屏幕的时候)
* @Author:         qiuShao
* @CreateDate:     20-5-13 下午4:45
*/
public class DisplayCamera {

    //录屏线程池 cameraThread
    private ScheduledThreadPoolExecutor cameraThread;
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



    /**
     * 初始化视频参数
     * @author      qiushao
     * @date        20-5-5 上午10:51
     */
    public DisplayCamera() {
        grabber = new OpenCVFrameGrabber(WEBCAM_DEVICE_INDEX);
        grabber.setImageWidth(captureWidth);
        grabber.setImageHeight(captureHeight);
    }



    /**
     * 处理每一针的画面
     * @author      qiushao
     * @date        20-5-5 上午9:55
     */
    public void showCamera() throws org.bytedeco.javacv.FrameGrabber.Exception {
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
     * 开始显示摄像头
     */
    public void start(){
        System.out.println("窗口显示摄像头...");
        try {
            grabber.start();
        } catch (Exception e) {
            System.out.println(e);
        }
        // 如果有录音设备则启动录音线程

        // 录屏
        cameraThread = new ScheduledThreadPoolExecutor(1);
        cameraThread.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try{
                    showCamera();
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
        if (null != cameraThread){
            cameraThread.shutdownNow();

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


}
