package org.openjfx.service;

import java.util.Scanner;

public class test {

    public static void main(String[] args)  {
        CameraRecording cameraRecording=new CameraRecording("test3",1920,1080,25);
//        Loader.load(opencv_objdetect.class);
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
