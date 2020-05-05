//package org.openjfx.service;
//
//import org.bytedeco.ffmpeg.global.avcodec;
//import org.bytedeco.javacv.CanvasFrame;
//import org.bytedeco.javacv.FFmpegFrameRecorder;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.OpenCVFrameGrabber;
//
//import javax.sound.sampled.*;
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.ShortBuffer;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//
//
//
///**
//* @Description:    备份摄像头
//* @Author:         qiuShao
//* @CreateDate:     20-5-4 下午8:57
//*/
//
//
//public class CameraRecording {
//
//
//
//
//
//    public static void CameraRecording(String outputFile, int captureWidth, int captureHeight, int FRAME_RATE)
//            throws org.bytedeco.javacv.FrameGrabber.Exception {
//
//        long startTime = 0;
//        long videoTS = 0;
//
//        //    摄像头设备指标
//        int WEBCAM_DEVICE_INDEX=0;
//        //    音频设备指标
//        int AUDIO_DEVICE_INDEX=4;
//
//
//        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(WEBCAM_DEVICE_INDEX);
//        grabber.setImageWidth(captureWidth);
//        grabber.setImageHeight(captureHeight);
//        System.out.println("开始抓取摄像头...");
//
//        // 摄像头开启状态
//        int isTrue = 0;
//        try {
//            grabber.start();
//            isTrue += 1;
//        } catch (org.bytedeco.javacv.FrameGrabber.Exception e2) {
//            if (grabber != null) {
//                try {
//                    grabber.restart();
//                    isTrue += 1;
//                } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
//                    isTrue -= 1;
//                    try {
//                        grabber.stop();
//                    } catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
//                        isTrue -= 1;
//                    }
//                }
//            }
//        }
//        if (isTrue < 0) {
//            System.err.println("摄像头首次开启失败，尝试重启也失败！");
//            return;
//        } else if (isTrue < 1) {
//            System.err.println("摄像头开启失败！");
//            return;
//        } else if (isTrue == 1) {
//            System.err.println("摄像头开启成功！");
//        } else if (isTrue == 1) {
//            System.err.println("摄像头首次开启失败，重新启动成功！");
//        }
//
//
//        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, captureWidth, captureHeight, 2);
//        recorder.setInterleaved(true);
//        recorder.setVideoOption("tune", "zerolatency");
//        recorder.setVideoOption("preset", "ultrafast");
//        recorder.setVideoOption("crf", "25");
//        // 2000 kb/s, 720P视频的合理比特率范围
//        recorder.setVideoBitrate(2000000);
//        // h264编/解码器
//        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//        // 封装格式flv
//        recorder.setFormat("flv");
//        // 视频帧率(保证视频质量的情况下最低25，低于25会出现闪屏)
//        recorder.setFrameRate(FRAME_RATE);
//        // 关键帧间隔，一般与帧率相同或者是视频帧率的两倍
//        recorder.setGopSize(FRAME_RATE * 2);
//        // 不可变(固定)音频比特率
//        recorder.setAudioOption("crf", "0");
//        // 最高质量
//        recorder.setAudioQuality(0);
//        // 音频比特率
//        recorder.setAudioBitrate(192000);
//        // 音频采样率
//        recorder.setSampleRate(44100);
//        // 双通道(立体声)
//        recorder.setAudioChannels(2);
//        // 音频编/解码器
//        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
//        System.out.println("开始录制...");
//
//        try {
//            recorder.start();
//        } catch (org.bytedeco.javacv.FrameRecorder.Exception e2) {
//            if (recorder != null) {
//                System.out.println("关闭失败，尝试重启");
//                try {
//                    recorder.stop();
//                    recorder.start();
//                } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
//                    try {
//                        System.out.println("开启失败，关闭录制");
//                        recorder.stop();
//                        return;
//                    } catch (org.bytedeco.javacv.FrameRecorder.Exception e1) {
//                        return;
//                    }
//                }
//            }
//
//        }
//
//
//        // 音频捕获
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                /**
//                 * 设置音频编码器 最好是系统支持的格式，否则getLine() 会发生错误
//                 * 采样率:44.1k;采样率位数:16位;立体声(stereo);是否签名;true:
//                 * big-endian字节顺序,false:little-endian字节顺序(详见:ByteOrder类)
//                 */
//                AudioFormat audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);
//
//                // 通过AudioSystem获取本地音频混合器信息
//                Mixer.Info[] minfoSet = AudioSystem.getMixerInfo();
//                // 通过AudioSystem获取本地音频混合器
//                Mixer mixer = AudioSystem.getMixer(minfoSet[AUDIO_DEVICE_INDEX]);
//                // 通过设置好的音频编解码器获取数据线信息
//                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
//                try {
//                    // 打开并开始捕获音频
//                    // 通过line可以获得更多控制权
//                    // 获取设备：TargetDataLine line
//                    // =(TargetDataLine)mixer.getLine(dataLineInfo);
//                    TargetDataLine line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
//                    line.open(audioFormat);
//                    line.start();
//                    // 获得当前音频采样率
//                    int sampleRate = (int) audioFormat.getSampleRate();
//                    // 获取当前音频通道数量
//                    int numChannels = audioFormat.getChannels();
//                    // 初始化音频缓冲区(size是音频采样率*通道数)
//                    int audioBufferSize = sampleRate * numChannels;
//                    byte[] audioBytes = new byte[audioBufferSize];
//
//                    ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
//                    exec.scheduleAtFixedRate(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                // 非阻塞方式读取
//                                int nBytesRead = line.read(audioBytes, 0, line.available());
//                                // 因为我们设置的是16位音频格式,所以需要将byte[]转成short[]
//                                int nSamplesRead = nBytesRead / 2;
//                                short[] samples = new short[nSamplesRead];
//                                /**
//                                 * ByteBuffer.wrap(audioBytes)-将byte[]数组包装到缓冲区
//                                 * ByteBuffer.order(ByteOrder)-按little-endian修改字节顺序，解码器定义的
//                                 * ByteBuffer.asShortBuffer()-创建一个新的short[]缓冲区
//                                 * ShortBuffer.get(samples)-将缓冲区里short数据传输到short[]
//                                 */
//                                ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);
//                                // 将short[]包装到ShortBuffer
//                                ShortBuffer sBuff = ShortBuffer.wrap(samples, 0, nSamplesRead);
//                                // 按通道录制shortBuffer
//                                recorder.recordSamples(sampleRate, numChannels, sBuff);
//                            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, 0, (long) 1000 / FRAME_RATE, TimeUnit.MILLISECONDS);
//                } catch (LineUnavailableException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }).start();
//
//        // javaCV提供了优化非常好的硬件加速组件来帮助显示我们抓取的摄像头视频
//        CanvasFrame cFrame = new CanvasFrame("Capture Preview", CanvasFrame.getDefaultGamma() / grabber.getGamma());
//        Frame capturedFrame = null;
//        // 执行抓取（capture）过程
//        while ((capturedFrame = grabber.grab()) != null) {
//            if (cFrame.isVisible()) {
//                //本机预览要发送的帧
//                cFrame.showImage(capturedFrame);
//            }
//            //定义我们的开始时间，当开始时需要先初始化时间戳
//            if (startTime == 0){
//                startTime = System.currentTimeMillis();
//            }
//
//            // 创建一个 timestamp用来写入帧中
//            videoTS = 1000 * (System.currentTimeMillis() - startTime);
//            //检查偏移量
//            if (videoTS > recorder.getTimestamp()) {
//                System.out.println("Lip-flap correction: " + videoTS + " : " + recorder.getTimestamp() + " -> "
//                        + (videoTS - recorder.getTimestamp()));
//                //告诉录制器写入这个timestamp
//                recorder.setTimestamp(videoTS);
//            }
//            // 发送帧
//            try {
//                recorder.record(capturedFrame);
//            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
//                System.out.println("录制帧发生异常，什么都不做");
//            }
//        }
//
//        cFrame.dispose();
//        try {
//            if (recorder != null) {
//                recorder.stop();
//            }
//        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
//            System.out.println("关闭录制器失败");
//            try {
//                if (recorder != null) {
//                    grabber.stop();
//                }
//            } catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
//                System.out.println("关闭摄像头失败");
//                return;
//            }
//        }
//        try {
//            if (recorder != null) {
//                grabber.stop();
//            }
//        } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
//            System.out.println("关闭摄像头失败");
//        }
//    }
//
//
//
//
//
//
//
//    public static void main(String[] args) throws Exception {
////        Loader.load(opencv_objdetect.class);
//        CameraRecording(
//                "test3",640,480,25);
//    }
//}
