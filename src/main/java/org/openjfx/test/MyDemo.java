package org.openjfx.test;


import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

public class MyDemo extends JPanel {
    private BufferedImage mImg;

    /**
     * 转换图像
     * @param mat
     * @return
     */
    private BufferedImage mat2BI(Mat mat){
        int dataSize = mat.cols()*mat.rows()*(int)mat.elemSize();
        byte[] data = new byte[dataSize];
        mat.get(0, 0,data);

        int type = mat.channels()==1? BufferedImage.TYPE_BYTE_GRAY:BufferedImage.TYPE_3BYTE_BGR;
        if(type == BufferedImage.TYPE_3BYTE_BGR){
            for(int i=0;i<dataSize;i+=3){
                byte blue=data[i+0];
                data[i+0]=data[i+2];
                data[i+2]=blue;
            }
        }
        BufferedImage image=new BufferedImage(mat.cols(),mat.rows(),type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);

        return image;
    }

    @Override
    public void paint(Graphics g){
        if(mImg!=null){
            g.drawImage(mImg, 0, 0, mImg.getWidth(),mImg.getHeight(),this);
        }
    }


    /**
     * opencv实现人脸识别，同时检测到人脸和人眼时才截图
     * @param img
     */
    public static Mat detectFace(Mat img) {

        System.out.println("Running DetectFace ... ");
        // 从配置文件lbpcascade_frontalface.xml中创建一个人脸识别器，该文件位于opencv安装目录中
        CascadeClassifier faceDetector = new CascadeClassifier("C:\\env\\opencv\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        CascadeClassifier eyeDetector = new CascadeClassifier("C:\\env\\opencv\\opencv\\sources\\data\\haarcascades\\haarcascade_eye.xml");


        // 在图片中检测人脸
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(img, faceDetections);

        //System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        Rect[] rects = faceDetections.toArray();
        Random r = new Random();
        if(rects != null && rects.length >= 1){
            for (Rect rect : rects) {

                //画矩形
                Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(0, 0, 255), 2);
//                Imgproc.circle(img, new Point(rect.x + rect.width, rect.y + rect.height), cvRound((rect.width + rect.height) * 0.25),
//                        new Scalar(0, 0, 255), 2);

                //识别人眼
                Mat faceROI = new Mat(img, rect );
                MatOfRect eyesDetections = new MatOfRect();
                eyeDetector.detectMultiScale( faceROI, eyesDetections);
                System.out.println("Running DetectEye ... "+ eyesDetections);

                if( eyesDetections.toArray().length > 1){
                    save(img, rect, "C:\\Users\\TR\\Desktop\\demo\\test\\"+r.nextInt(2000)+".jpg");
                }

            }
        }
        return img;
    }

    /**
     * opencv将人脸进行截图并保存
     * @param img
     */
    private static void save(Mat img, Rect rect, String outFile){
        Mat sub = img.submat(rect);
        Mat mat = new Mat();
        Size size = new Size(300, 300);
        Imgproc.resize(sub, mat, size);
        Imgcodecs.imwrite(outFile, mat);
    }


    public static void main(String[] args) {
        try{
            //加载opencv库
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            //获取摄像头视频流
            VideoCapture capture = new VideoCapture(0);
            int height = (int)capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
            int width = (int)capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
            if(height == 0||width == 0){
                throw new Exception("camera not found!");
            }

            //使用Swing生成GUI
            JFrame frame = new JFrame("camera");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            MyDemo panel = new MyDemo();
            frame.setContentPane(panel);
            frame.setVisible(true);
            frame.setSize(width+frame.getInsets().left+frame.getInsets().right,
                    height+frame.getInsets().top+frame.getInsets().bottom);

            Mat capImg = new Mat();
            Mat temp=new Mat();
            //Random r = new Random();
            while(frame.isShowing()){
                //获取视频帧
                capture.read(capImg);
                //转换为灰度图
                Imgproc.cvtColor(capImg, temp, Imgproc.COLOR_RGB2GRAY);
                //识别人脸
                Mat image = detectFace(capImg);
                //转为图像显示
                panel.mImg = panel.mat2BI(image);
                panel.repaint();
            }
            capture.release();
            frame.dispose();

        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }
        finally{
            System.out.println("Exit");
        }

    }

}