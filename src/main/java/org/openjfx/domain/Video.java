package org.openjfx.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description: 录制视频选项
 * @Author: qiuShao
 * @CreateDate: 20-4-12 上午10:14
 */
public class Video {
    /**
     * 屏幕分辨率
     */
    private int videoWidth;
    private int videoHeigth;
    /**
     * 视频保存位置
     */
    private String savePath;
    /**
     * 视频保存名
     */
    private String fileName;
    /**
     * 视频保存格式（ＭＰ４...)
     */
    private String saveFormat;
    /**
     * 视频帧率
     */
    private double frameRate;

    /**
    * 对视频实体类进行初始化
    * @author      qiushao
    * @date        20-4-30 下午10:31
    */
    public Video() {
        videoWidth = 1920;
        videoHeigth = 1080;
//        处理保存的时间
        fileName = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").format(LocalDateTime.now());
        savePath="";
        saveFormat = "flv";
        frameRate = 25;
    }


    /**
     * 设置分辨率
     * * "1920*1080","1650*1050","1024*768"
     *
     * @author qiushao
     * @date 20-4-30 下午10:27
     */
    public void setWidthAndHeiht(String resolutions) {
        if ("1920*1080".equals(resolutions)) {
            videoWidth = 1920;
            videoHeigth = 1080;
        } else if ("1650*1050".equals(resolutions)) {
            videoWidth = 1650;
            videoHeigth = 1050;
        } else if ("1024*768".equals(resolutions)) {
            videoWidth = 1024;
            videoHeigth = 768;
        }
    }


    public double getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSaveFormat() {
        return saveFormat;
    }

    public void setSaveFormat(String saveFormat) {
        this.saveFormat = saveFormat;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeigth() {
        return videoHeigth;
    }

    public void setVideoHeigth(int videoHeigth) {
        this.videoHeigth = videoHeigth;
    }

    public String getSavePath() {

//        savePath=savePath+"/"+fileName + "."+saveFormat;
        return savePath;
    }

    public void setSavePath(String savePath) {
        if ("".equals(savePath)){
            savePath=fileName + "."+saveFormat;
        }
//        else{
//            savePath=savePath+"/"+fileName + "."+saveFormat;
//        }
        this.savePath = savePath;
    }
}
