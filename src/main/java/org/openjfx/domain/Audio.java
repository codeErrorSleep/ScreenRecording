package org.openjfx.domain;



/**
* @Description:    音频类的属性设置
* @Author:         qiuShao
* @CreateDate:     20-4-19 下午9:33
*/
public class Audio {
    // 最高质量
    private int audioQuality=0;
    // 音频比特率
    private int audioBitrate=192000;
    // 音频采样率
    private int sampleRate=44100;
    // 双通道(立体声)
    private int audioChannels=2;


    public int getAudioQuality() {
        return audioQuality;
    }

    public void setAudioQuality(int audioQuality) {
        this.audioQuality = audioQuality;
    }

    public int getAudioBitrate() {
        return audioBitrate;
    }

    public void setAudioBitrate(int audioBitrate) {
        this.audioBitrate = audioBitrate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getAudioChannels() {
        return audioChannels;
    }

    public void setAudioChannels(int audioChannels) {
        this.audioChannels = audioChannels;
    }
}
