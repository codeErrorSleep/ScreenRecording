package org.openjfx.service;

import org.bytedeco.javacpp.avcodec;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
* @Description:    录制的服务类
* @Author:         qiuShao
* @CreateDate:     20-4-12 下午2:40
*/
public class VideoRecording {
    //录屏线程池 screenTimer
    private ScheduledThreadPoolExecutor screenTimer;
    //获取屏幕尺寸
//    private Rectangle rectangle; // 截屏的大小
    //获取屏幕尺寸
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Rectangle rectangle = new Rectangle(screenSize.width, screenSize.height); // 截屏的大小

    //视频类 FFmpegFrameRecorder
    private FFmpegFrameRecorder recorder;
//  音频类
    private AudioFormat audioFormat;
    private Robot robot;
    //音频线程池 exec
    private ScheduledThreadPoolExecutor exec;
    private TargetDataLine line;
    private DataLine.Info dataLineInfo;
//    判断是否打开麦克风
    private boolean isHaveDevice = true;
    private long startTime = 0;
    private long videoTS = 0;
    private long pauseTime = 0;
    private double frameRate = 25;


    public VideoRecording() {
    }


    public VideoRecording(Video video, Audio audio, boolean isHaveDevice){
        /**
         * 视频设置
         */
        System.out.println(video.getSavePath());
//        视频属性设置
        recorder = new FFmpegFrameRecorder(video.getSavePath(), screenSize.width, screenSize.height);
//        视频编码格式e
//        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4); // 13
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

//        视频文件格式
        recorder.setFormat(video.getSaveFormat());
        // 视频帧率(保证视频质量的情况下最低25，低于25会出现闪屏)
        recorder.setFrameRate(video.getFrameRate());
        // 关键帧间隔，一般与帧率相同或者是视频帧率的两倍
        recorder.setGopSize((int) frameRate * 2);
//        分辨率设置
        recorder.setImageWidth(video.getVideoWidth());
        recorder.setImageHeight(video.getVideoHeigth());

        recorder.setVideoBitrate(2000000);
        recorder.setVideoOption("tune", "zerolatency");
        recorder.setVideoOption("preset", "slow");
        recorder.setVideoQuality(0);
        recorder.setVideoOption("crf", "25");


        /**
        * 音频设置
        */
        // 不可变(固定)音频比特率
        recorder.setAudioOption("crf", "0");
        // 最高质量
        recorder.setAudioQuality(audio.getAudioQuality());
        // 音频比特率
        recorder.setAudioBitrate(audio.getAudioBitrate());
        // 音频采样率
        recorder.setSampleRate(audio.getSampleRate());
        // 双通道(立体声)
        recorder.setAudioChannels(audio.getAudioChannels());
        // 音频编/解码器
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);



        try {
            robot = new Robot();
        } catch (AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            recorder.start();
        } catch (FrameRecorder.Exception e) {
            // TODO Auto-generated catch block
            System.out.print("*******************************");
        }
        this.isHaveDevice = isHaveDevice;
    }


    /**
    * 初始化
    * @author      qiushao
    * @date        20-6-24 下午4:15
    */
    public void initialize(Video video, Audio audio, boolean isHaveDevice){
        /**
         * 视频设置
         */
        System.out.println(video.getSavePath());
//        视频属性设置
        recorder = new FFmpegFrameRecorder(video.getSavePath(), screenSize.width, screenSize.height);
//        视频编码格式e
//        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4); // 13
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

//        视频文件格式
        recorder.setFormat(video.getSaveFormat());
        // 视频帧率(保证视频质量的情况下最低25，低于25会出现闪屏)
        recorder.setFrameRate(video.getFrameRate());
        // 关键帧间隔，一般与帧率相同或者是视频帧率的两倍
        recorder.setGopSize((int) frameRate * 2);
//        分辨率设置
        recorder.setImageWidth(video.getVideoWidth());
        recorder.setImageHeight(video.getVideoHeigth());

        recorder.setVideoBitrate(2000000);
        recorder.setVideoOption("tune", "zerolatency");
        recorder.setVideoOption("preset", "slow");
        recorder.setVideoQuality(0);
        recorder.setVideoOption("crf", "25");


        /**
         * 音频设置
         */
        // 不可变(固定)音频比特率
        recorder.setAudioOption("crf", "0");
        // 最高质量
        recorder.setAudioQuality(audio.getAudioQuality());
        // 音频比特率
        recorder.setAudioBitrate(audio.getAudioBitrate());
        // 音频采样率
        recorder.setSampleRate(audio.getSampleRate());
        // 双通道(立体声)
        recorder.setAudioChannels(audio.getAudioChannels());
        // 音频编/解码器
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            recorder.start();
        } catch (FrameRecorder.Exception e) {
            // TODO Auto-generated catch block
            System.out.print("*******************************");
        }
        this.isHaveDevice = isHaveDevice;
    }


    /**
     * 开始录制
     */
    public void start() {

        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        if (pauseTime == 0) {
            pauseTime = System.currentTimeMillis();
        }
        // 如果有录音设备则启动录音线程
        if (isHaveDevice) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    caputre();
                }
            }).start();
        }
            // 录屏
        screenTimer = new ScheduledThreadPoolExecutor(1);
        screenTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                处理每张截图
                dealImage();
            }
        }, (int) (1000 / frameRate), (int) (1000 / frameRate), TimeUnit.MILLISECONDS);

    }



    /**
     * @author      qiushao
    处理截图
     * @date        20-4-19 下午2:52
     */
    public void dealImage(){
        // 截屏
        BufferedImage screenCapture = robot.createScreenCapture(rectangle);
        // 声明一个BufferedImage用重绘截图
        BufferedImage videoImg = new BufferedImage(screenSize.width, screenSize.height,
                BufferedImage.TYPE_3BYTE_BGR);
        // 创建videoImg的Graphics2D
        Graphics2D videoGraphics = videoImg.createGraphics();

        videoGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        videoGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_SPEED);
        videoGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        // 重绘截图
        videoGraphics.drawImage(screenCapture, 0, 0, null);

        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();

        Frame frame = java2dConverter.convert(videoImg);
        try {
            videoTS = 1000
                    * (System.currentTimeMillis() - startTime - (System.currentTimeMillis() - pauseTime));
            // 检查偏移量
            if (videoTS > recorder.getTimestamp()) {
                recorder.setTimestamp(videoTS);
            }
            recorder.record(frame); // 录制视频
        } catch (FrameRecorder.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 释放资源
        videoGraphics.dispose();
        videoGraphics = null;
        videoImg.flush();
        videoImg = null;
        java2dConverter = null;
        screenCapture.flush();
        screenCapture = null;

    }

    /**
    * 录制音频
    * @author      qiushao
    * @date        20-5-5 上午9:19
    */
    public void caputre() {
        audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);
        // 通过AudioSystem获取本地音频混合器信息
        Mixer.Info[] minfoSet = AudioSystem.getMixerInfo();
        // 通过AudioSystem获取本地音频混合器
        Mixer mixer = AudioSystem.getMixer(minfoSet[4]);


        dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {
            line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        } catch (LineUnavailableException e1) {
            // TODO Auto-generated catch block
            System.out.println("#################");
        }
        try {
            line.open(audioFormat);
        } catch (LineUnavailableException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        line.start();

        final int sampleRate = (int) audioFormat.getSampleRate();
        final int numChannels = audioFormat.getChannels();

        int audioBufferSize = sampleRate * numChannels;
        final byte[] audioBytes = new byte[audioBufferSize];

        exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    int nBytesRead = line.read(audioBytes, 0, line.available());
                    int nSamplesRead = nBytesRead / 2;
                    short[] samples = new short[nSamplesRead];

                    // Let's wrap our short[] into a ShortBuffer and
                    // pass it to recordSamples
                    ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);
                    ShortBuffer sBuff = ShortBuffer.wrap(samples, 0, nSamplesRead);

                    // recorder is instance of
                    // org.bytedeco.javacv.FFmpegFrameRecorder
                    recorder.recordSamples(sampleRate, numChannels, sBuff);
                    // System.gc();
                } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, (int) (1000 / frameRate), TimeUnit.MILLISECONDS);
    }



    /**
     * 停止
     */
    public void stop() {
        if (null != screenTimer) {
            screenTimer.shutdownNow();
        }
        try {
            recorder.stop();
            recorder.release();
            recorder.close();
            screenTimer = null;
            // screenCapture = null;
            if (isHaveDevice) {
                if (null != exec) {
                    exec.shutdownNow();
                }
                if (null != line) {
                    line.stop();
                    line.close();
                }
                dataLineInfo = null;
                audioFormat = null;
            }
        } catch (FrameRecorder.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
    * 暂停
    * @author      qiushao
    * @date        20-6-24 下午4:17
    */
    public void pause(){
        screenTimer.shutdownNow();
        screenTimer = null;
        if (isHaveDevice) {
            exec.shutdownNow();
            exec = null;
            line.stop();
            line.close();
            dataLineInfo = null;
            audioFormat = null;
            line=null;
        }
        pauseTime = System.currentTimeMillis();
    }
}
