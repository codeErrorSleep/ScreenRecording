package org.openjfx.service;

import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;

import javax.swing.undo.AbstractUndoableEdit;
import java.util.Scanner;

public class test {


    public static void main(String[] args)  {
//        CameraRecording cameraRecording=new CameraRecording("test3",1920,1080,25);
//        Loader.load(opencv_objdetect.class);

        SettingUtils settingUtils=new SettingUtils();
        settingUtils.checkJsonFile();
        Video video=settingUtils.readVidioJSON();
        Audio audio=settingUtils.readAudioJSON();

//        Video video=new Video();
//        Audio audio=new Audio();
//

        CameraRecording cameraRecording=new CameraRecording(video,audio);
//        CameraRecording cameraRecording=new CameraRecording("out.flv",640,480,5);


        try{
            cameraRecording.start();
        }catch (Exception e)
        {

        }


        while (true) {
            System.out.println("你要停止吗？请输入(stop)，程序会停止。");
            Scanner sc = new Scanner(System.in);
            if (sc.next().equalsIgnoreCase("stop")) {
                cameraRecording.stop();
                System.out.println("停止");
            }
            if (sc.next().equalsIgnoreCase("start")) {
                cameraRecording.start();
                System.out.println("开始");
            }
        }

    }
}
