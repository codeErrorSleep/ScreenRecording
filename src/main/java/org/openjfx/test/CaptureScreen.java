package org.openjfx.test;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

/**
* @Description:    截图功能的实现
* @Author:         qiuShao
* @CreateDate:     20-4-22 上午9:02
*/
public class CaptureScreen {

    public static void captureScreen() throws Exception {

//调用截图api
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);

//设置文件夹与文件名
        String folder= DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        String fileName= DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss").format(LocalDateTime.now())+".png";
        //保存路径
        File screenFile = new File(folder);
        if (!screenFile.exists()) {
            screenFile.mkdir();
        }
        File f = new File(screenFile, fileName);
//        File f = new File(folder);
        ImageIO.write(image, "png", f);
    }


    public static void main(String[] args) {
        try {
            captureScreen();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}