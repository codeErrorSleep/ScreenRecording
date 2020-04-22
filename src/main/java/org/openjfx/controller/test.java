package org.openjfx.controller;

import org.openjfx.service.CaptureScreen;

public class test {
    public static void test1(){
        try {
            CaptureScreen captureScreen=new CaptureScreen();
            captureScreen.processingImages();
            captureScreen.saveFile();
        }catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void main(String[] args){
        test1();
    }
}
