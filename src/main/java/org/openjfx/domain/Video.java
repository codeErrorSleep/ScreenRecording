package org.openjfx.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
* @Description:    录制视频选项
* @Author:         qiuShao
* @CreateDate:     20-4-12 上午10:14
*/
public class Video {
    /**
    * 屏幕分辨率
    */
    private int screenWidth;
    private int screenHeigth;
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

//    设置默认的视频选项
    public Video(){
        screenWidth=1920;
        screenHeigth=1080;
        savePath="outputVideo/";
//        处理保存的时间
        fileName= DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").format(LocalDateTime.now());


        savePath=fileName+".flv";

        saveFormat="flv";
        frameRate=25;

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

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeigth() {
        return screenHeigth;
    }

    public void setScreenHeigth(int screenHeigth) {
        this.screenHeigth = screenHeigth;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}
