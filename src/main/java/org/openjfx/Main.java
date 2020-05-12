package org.openjfx;

import javafx.application.Application;
import org.openjfx.controller.MainPane;
import org.openjfx.test.jietutest;


public class Main {
    public static void main(String[] args){
//获取屏幕分辨率
//        int screenWidth=((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
//        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
//        System.out.println(screenWidth+""+screenHeight);


        Application.launch(MainPane.class);

//        Application.launch(jietutest.class);


    }

}
