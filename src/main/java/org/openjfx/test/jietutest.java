package org.openjfx.test;

//import javafx.embed.swing.SwingFXUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.openjfx.domain.Audio;
import org.openjfx.domain.Video;
import org.openjfx.service.CameraRecording;
import org.openjfx.service.CaptureScreen;
import org.openjfx.service.SettingUtils;
import org.openjfx.service.VideoRecording;
import javafx.scene.paint.Paint;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import javafx.application.Application;

import javax.imageio.ImageIO;

public class jietutest extends Application {

    ImageView iv; //切成的图片展示区域
    Stage primaryStage; //主舞台
    Stage stage;        //切图时候的辅助舞台
    double start_x;     //切图区域的起始位置x
    double start_y;     //切图区域的起始位置y
    double w;           //切图区域宽
    double h;           //切图区域高
    HBox hBox;          //切图区域

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;  //主舞台全局赋值
        AnchorPane root = new AnchorPane(); //锚点布局,也称为 绝对布局,定位布局
        //创建截屏按钮与显示区域并且进行布局
        Button bu = new Button("点击截屏");
        iv= new ImageView();
        iv.setFitHeight(400);
        iv.setPreserveRatio(true); //开启等比例缩放
        root.getChildren().addAll(bu,iv);
        AnchorPane.setTopAnchor(bu,50.0);
        AnchorPane.setLeftAnchor(bu,50.0);
        AnchorPane.setTopAnchor(iv,100.0);
        AnchorPane.setLeftAnchor(iv,50.0);

        //设置场景和标题，以及舞台大小
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("截屏工具");
        primaryStage.setHeight(800);
        primaryStage.setWidth(800);
        primaryStage.show();
        //为点击截图按钮绑定事件
        bu.setOnAction(event -> {
            show();
        });
        //绑定截图快捷键，使用快捷键达到点击按钮的目的
        KeyCombination keyCombination = KeyCombination.valueOf("ctrl+alt+p");
        Mnemonic mc = new Mnemonic(bu,keyCombination);
        scene.addMnemonic(mc);
    }

    public void show(){
        //将主舞台缩放到任务栏
        primaryStage.setIconified(true);
        //创建辅助舞台，并设置场景与布局
        stage = new Stage();
        //锚点布局采用半透明
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #85858522");
        //场景设置白色全透明
        Scene scene = new Scene(anchorPane);
        scene.setFill(Paint.valueOf("#ffffff00"));
        stage.setScene(scene);
        //清楚全屏中间提示文字
        stage.setFullScreenExitHint("");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setFullScreen(true);
        stage.show();

        //切图窗口绑定鼠标按下事件
        anchorPane.setOnMousePressed(event -> {
            //清除锚点布局中所有子元素
            anchorPane.getChildren().clear();
            //创建切图区域
            hBox = new HBox();
            //设置背景保证能看到切图区域桌面
            hBox.setBackground(null);
            //设置边框
            hBox.setBorder(new Border(new BorderStroke(Paint.valueOf("#c03700"), BorderStrokeStyle.SOLID,
                    null,new BorderWidths(3))));
            anchorPane.getChildren().add(hBox);
            //记录并设置起始位置
            start_x = event.getSceneX();
            start_y = event.getSceneY();
            AnchorPane.setLeftAnchor(hBox,start_x);
            AnchorPane.setTopAnchor(hBox,start_y);
        });
        //绑定鼠标按下拖拽的事件
        anchorPane.setOnMouseDragged(event -> {
            //用label记录切图区域的长宽
            Label label = new Label();
            label.setAlignment(Pos.CENTER);
            label.setPrefHeight(30);
            label.setPrefWidth(170);
            anchorPane.getChildren().add(label);
            AnchorPane.setLeftAnchor(label,start_x+30);
            AnchorPane.setTopAnchor(label,start_y);
            label.setTextFill(Paint.valueOf("#ffffff"));//白色填充
            label.setStyle("-fx-background-color: #000000");//黑背景
            //计算宽高并且完成切图区域的动态效果
            w = Math.abs(event.getSceneX()-start_x);
            h = Math.abs(event.getSceneY()-start_y);
            hBox.setPrefWidth(w);
            hBox.setPrefHeight(h);
            label.setText("宽："+w+" 高："+h);
        });

        //绑定鼠标松开事件
        anchorPane.setOnMouseReleased(event -> {
            //记录最终长宽
            w = Math.abs(event.getSceneX()-start_x);
            h = Math.abs(event.getSceneY()-start_y);
            anchorPane.setStyle("-fx-background-color: #00000000");
            //添加剪切按钮，并显示在切图区域的底部
            Button b = new Button("剪切");
            hBox.setBorder(new Border(new BorderStroke(Paint.valueOf("#85858544"), BorderStrokeStyle.SOLID,
                    null,new BorderWidths(3))));
            hBox.getChildren().add(b);
            hBox.setAlignment(Pos.BOTTOM_RIGHT);
            //为切图按钮绑定切图事件
            b.setOnAction(event1 -> {
                //切图辅助舞台小时
                stage.close();
                try {
                    //切图具体方法
                    capterImg();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //主舞台还原
                primaryStage.setIconified(false);
            });
        });

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                stage.close();
                primaryStage.setIconified(false);
            }
        });
    }

    public void capterImg() throws Exception {
        //利用awt中的方法，通过记录的起始点和长宽完成屏幕截图
        Robot robot = new Robot();
        Rectangle re = new Rectangle((int)start_x,(int)start_y,(int)w,(int)h);
        BufferedImage screenCapture = robot.createScreenCapture(re);
        //截图图片背景透明处理
//        BufferedImage bufferedImage = Picture4.transferAlpha(screenCapture);
        //不进行背景透明处理
        BufferedImage bufferedImage = screenCapture;
        //转换图片格式展示在主舞台的场景中
//  WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
//        iv.setImage(writableImage);

        //将截图内容，放入系统剪切板
        Clipboard cb = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
//        content.putImage(writableImage);
        cb.setContent(content);

        //将截取图片放入到系统固定位置
        ImageIO.write(bufferedImage,"png",new File("capter.png"));

    }

    public static BufferedImage transferAlpha(BufferedImage img) {
        BufferedImage bufferedImage = null;
        try {
            //取左上角点作为背景点
            int RGB = img.getRGB(img.getMinX(),img.getMinY());
            int R = (RGB & 0xff0000) >>16;
            int G= (RGB & 0xff00) >> 8;
            int B = (RGB & 0xff);
            bufferedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int j1 = img.getMinY(); j1 < img.getHeight(); j1++) {
                //遍历一行中的rgb三通道像素点，
                for (int j2 = img.getMinX(); j2 < img.getWidth(); j2++) {
                    //获取固定点的rgb值
                    int rgb = img.getRGB(j2, j1);
                    int r =(rgb & 0xff0000 ) >> 16 ;
                    int g= (rgb & 0xff00 ) >> 8 ;
                    int b= (rgb & 0xff );
                    if((Math.abs(r-R)<15) && (Math.abs(g-G)<15) && (Math.abs(b-B)<15)){
                        rgb = (1 << 24) | (rgb & 0x00ffffff);
                    }
                    //设置固定点的rgb值
                    bufferedImage.setRGB(j2, j1, rgb);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{

        }
        return bufferedImage;
    }

}
