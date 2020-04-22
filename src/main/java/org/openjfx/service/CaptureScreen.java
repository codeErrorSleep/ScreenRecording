package org.openjfx.service;

import org.bytedeco.javacv.FrameRecorder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
* @Description:    截图功能的实现
* @Author:         qiuShao
* @CreateDate:     20-4-22 上午9:02
*/
public class CaptureScreen {

    private String folder;
    private String fileName;
//    保存文件的文件夹
    private File screenFile;
//    图片
    private BufferedImage image;

//    截取图片处理
    public void processingImages() throws Exception{
        //调用截图api
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        Rectangle screenRectangle = new Rectangle(screenSize);
//        Robot robot1 = new Robot();
        try {
//            image = robot1.createScreenCapture(screenRectangle);
//            image = robot1.createScreenCapture(screenRectangle);
             image = (new Robot()).createScreenCapture(new
                    Rectangle(0, 0, (int) 1920, (int) 1080));

        }catch (Exception e){
            System.out.println(e);
        }
    }



//将截图保存到文件夹
    public void saveFile() throws Exception{
        //        初始化文件夹
        folder= DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        screenFile = new File(folder);
        if (!screenFile.exists()) {
            screenFile.mkdir();
        }
        //设置文件夹与文件名
        fileName= DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss").format(LocalDateTime.now())+".png";
        //保存路径
        File f = new File(screenFile, fileName);
//        File f = new File(folder);
        ImageIO.write(image, "png", f);
    }


    public void test() {
        try {
            processingImages();
            saveFile();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}