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
* @Description:    保存和读取用户的保存设置(json)
* @Author:         qiuShao
* @CreateDate:     20-4-22 下午9:04
*/
public class SettingUtils {
    public static void main(String args[]){
        SettingUtils tester = new SettingUtils();
        try {

            Video video=new Video();
            tester.writeVideoJSON(video);

            Audio audio=new Audio();
            tester.writeAudioJSON(audio);


//            Video video1= tester.reada();
//            System.out.println(video1);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeVideoJSON(Video video) throws JsonGenerationException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("videoSetting.json"), video);
    }


    private void writeAudioJSON(Audio audio) throws JsonGenerationException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("audioSetting.json"), audio);
    }

    private Video readVidioJSON() throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        Video video= mapper.readValue(new File("videoSetting.json"), Video.class);
        return video;
    }

    private Audio readAudioJSON() throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        Audio audio= mapper.readValue(new File("audioSetting.json"), Audio.class);
        return audio;
    }

}
