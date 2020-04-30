package org.openjfx.service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;

import java.io.File;
import java.io.IOException;


/**
 * @Description: 保存和读取用户的保存设置(json)
 * @Author: qiuShao
 * @CreateDate: 20-4-22 下午9:04
 */
public class SettingUtils {
//    public static void main(String args[]){
//        SettingUtils tester = new SettingUtils();
//        tester.checkJsonFile();
//    }

    /**
     * 检查是否已有设置文件,有则读取,无就新建
     *
     * @return
     * @author qiushao
     * @date 20-4-30 上午11:25
     */
    public void checkJsonFile() {
        File file = new File("videoSetting.json");
//找不到文件直接新建音频视频文件
        if (!file.exists()) {
            Video video = new Video();
            Audio audio = new Audio();
            // 文件不存在
            writeJsonFile(video,audio);
        }
    }


    /**
     * 将video,audio写入到json文件
     * @author qiushao
     * @date 20-4-30 上午11:24
     */
    public void writeJsonFile(Video video,Audio audio) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("videoSetting.json"), video);
            mapper.writeValue(new File("audioSetting.json"), audio);
            System.out.println("保存文件成功");

        } catch (Exception e) {
//            JsonGenerationException, JsonMappingException, IOException
            e.printStackTrace();
        }

    }


    public Video readVidioJSON() {
        Video video = new Video();
        try {
            ObjectMapper mapper = new ObjectMapper();
            video = mapper.readValue(new File("videoSetting.json"), Video.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return video;
    }

    public Audio readAudioJSON() {
        Audio audio = new Audio();
        try {
            ObjectMapper mapper = new ObjectMapper();
            audio = mapper.readValue(new File("audioSetting.json"), Audio.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audio;
    }

}
