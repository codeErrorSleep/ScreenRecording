package org.openjfx.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class PlayerPaneController {
    @FXML
    MediaView playView;
    @FXML
    Button playBT;
    @FXML
    Button stopBT;
    @FXML
    Button maxBT;
    @FXML
    Button volumeBT;
    @FXML
    Slider volumeSD;
    @FXML
    BorderPane controlBorderPane;
    @FXML
    AnchorPane mainPanel;
    @FXML
    MenuBar menu;
    @FXML
    ImageView imageView;
    @FXML
    Menu menubar1;
    @FXML
    Menu menubar2;
    @FXML
    Slider processSD;
    @FXML
    Label timeLB;
    private MediaPlayer mediaPlayer;
    private Media media;
    private String playIcon;
    private String pauseIcon;
    private String stopIcon;
    private String volOffIcon;
    private String volOnIcon;
    private String maxIcon;
    private String backImage;
    private Duration duration;
    Boolean isMute = false;
    double currentWidth, currentHeight;
    Scene scene;
    String url = null;
    Stage stage;
    Boolean isFullWindow = false;
    Boolean isPlaying = false;
    Integer iconHeight = 23;
    Integer iconWidth = 20;


    /**
    * 初始化
    * @author      qiushao
    * @date        20-5-6 下午9:41
    */
    public void init() {
        ClassLoader classLoader = PlayerPaneController.class.getClassLoader();
        playIcon = classLoader.getResource("org/openjfx/icon/play.png").toString();
        pauseIcon = classLoader.getResource("org/openjfx/icon/pause.png").toString();
        stopIcon = classLoader.getResource("org/openjfx/icon/stop.png").toString();
        volOffIcon = classLoader.getResource("org/openjfx/icon/volume_off.png").toString();
        volOnIcon = classLoader.getResource("org/openjfx/icon/volume_on.png").toString();
        maxIcon = classLoader.getResource("org/openjfx/icon/max.png").toString();
        backImage = classLoader.getResource("org/openjfx/icon/background.png").toString();
        //设置各控件图标
        setIcon(playBT, playIcon);
        setIcon(stopBT, stopIcon);
        setIcon(volumeBT, volOnIcon);
        setIcon(maxBT, maxIcon);
        scene = mainPanel.getScene();
        stage = (Stage) mainPanel.getScene().getWindow();
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            currentWidth = newValue.intValue();
            adjustSize();
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            currentHeight = newValue.intValue();
            adjustSize();
        });

        currentWidth = mainPanel.getWidth();
        currentHeight = mainPanel.getHeight();
        Image backGround = new Image(backImage);
        imageView.setImage(backGround);
        imageView.setFitHeight(backGround.getHeight());
        imageView.setFitWidth(backGround.getWidth());
        adjustSize();
    }

    /**
    * 设置图标
    * @author      qiushao
    * @date        20-5-6 下午9:41
    */
    private void setIcon(Button button, String path) {
        Image icon = new Image(path);
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(iconWidth);
        imageView.setFitHeight(iconHeight);
        button.setGraphic(imageView);
        //设置图标点击时发亮
        ColorAdjust colorAdjust = new ColorAdjust();
        button.setOnMousePressed(event -> {
            colorAdjust.setBrightness(0.5);
            button.setEffect(colorAdjust);
        });
        button.setOnMouseReleased(event -> {
            colorAdjust.setBrightness(0);
            button.setEffect(colorAdjust);
        });
    }


    /**
    * 播放前修改好进度条等参数
    * @author      qiushao
    * @date        20-5-6 下午10:28
    */
    public void initBeforePlay() {
        mediaPlayer.pause();
        mediaPlayer.setVolume(volumeSD.getValue() / 100);
        volumeSD.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100);
            }
        });
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                //System.out.println("it is called");
                updateTime();
            }
        });
    }

    /**
    * 打开系统目录找MP4文件
    * @author      qiushao
    * @date        20-5-6 下午9:44
    */
    public void openNewLocalMp4(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a new Mp4 file");
        File file = fileChooser.showOpenDialog(stage);
        url = file.toURI().toString();
        if (media != null) {
            mediaPlayer.stop();
            isPlaying = false;
            setIcon(playBT, playIcon);
        }
        media = new Media(url);
        mediaPlayer = new MediaPlayer(media);
        playView.setMediaPlayer(mediaPlayer);
        initBeforePlay();
    }





    /**
    * 调整界面大小
    * @author      qiushao
    * @date        20-5-6 下午9:45
    */
    public void adjustSize() {
        mainPanel.setPrefSize(currentWidth, currentHeight);
        AnchorPane.setBottomAnchor(controlBorderPane, 0.0);
        AnchorPane.setTopAnchor(menu, 0.0);
        AnchorPane.setTopAnchor(playView, menu.getHeight());
        playView.setFitHeight(currentHeight - menu.getHeight() - controlBorderPane.getHeight());
        playView.setFitWidth(currentWidth);
        menu.setPrefWidth(currentWidth);
        controlBorderPane.setPrefWidth(currentWidth);
        AnchorPane.setTopAnchor(imageView, currentHeight / 2 - imageView.getFitHeight() / 2);
        AnchorPane.setLeftAnchor(imageView, currentWidth / 2 - imageView.getFitWidth() / 2);
    }

    /**
    * 关闭程序
    * @author      qiushao
    * @date        20-5-6 下午9:45
    */
    public void exit(ActionEvent actionEvent) {
        stage.close();
    }


    /**
    * 播放时间设置
    * @author      qiushao
    * @param       elapsed duration
    * @return      String
    * @date        20-5-6 下午9:46
    */
    private String formatTime(Duration elapsed, Duration duration) {
        //将两个Duartion参数转化为 hh：mm：ss的形式后输出
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        int elapsedMinutes = (intElapsed - elapsedHours * 60 * 60) / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;
        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            int durationMinutes = (intDuration - durationHours * 60 * 60) / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

            if (durationHours > 0) {
                return String.format("%02d:%02d:%02d / %02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds, durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d / %02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes, durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%02d:%02d:%02d / %02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d / %02d:%02d", elapsedMinutes, elapsedSeconds);
            }
        }
    }


    /**
    * 设置时间样式
    * @author      qiushao
    * @date        20-5-6 下午9:46
    */
    public void updateTime() {
        if (processSD != null && timeLB != null && volumeSD != null && volumeBT != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Duration currentTime = mediaPlayer.getCurrentTime();
                    timeLB.setText(formatTime(currentTime, duration));    //设置时间标签
                    processSD.setDisable(duration.isUnknown());   //无法读取时间是隐藏进度条
                    if (!processSD.isDisabled() && duration.greaterThan(Duration.ZERO) && !processSD.isValueChanging()) {
                        processSD.setValue(currentTime.toMillis() / duration.toMillis() * 100);   //设置进度条
                    }
                }
            });
        }
    }

    /**
    * 设置是否全屏
    * @author      qiushao
    * @date        20-5-6 下午9:47
    */
    public void setMaximizeButton() {
        if (isFullWindow == false) {
            stage.setFullScreen(true);
            isFullWindow = true;
        } else {
            stage.setFullScreen(false);
            isFullWindow = false;
        }

    }


    public void stop(MouseEvent mouseEvent) {
        if (media != null) {
            mediaPlayer.stop();
            playView.setMediaPlayer(null);
            isPlaying = false;
            setIcon(playBT, playIcon);
        }
    }


    /**
    * 开始播放
    * @author      qiushao
    * @date        20-5-6 下午9:48
    */
    public void play(MouseEvent mouseEvent) {
        if (media != null && playView.getMediaPlayer()!=null) {
            if (isPlaying == false) {
                mediaPlayer.play();
                /*
                只有play了之后才知道duration
                 */
                duration = mediaPlayer.getMedia().getDuration();
                isPlaying = true;
                setIcon(playBT, pauseIcon);
            } else {
                mediaPlayer.pause();
                isPlaying = false;
                setIcon(playBT, playIcon);
            }
        }
    }
    
    /**
    * 音量进度条
    * @author      qiushao
    * @date        20-5-6 下午9:50
    */
    public void changeMute(MouseEvent mouseEvent) {
        if (media != null) {
            if (isMute == false) {
                isMute = true;
                setIcon(volumeBT, volOffIcon);
                mediaPlayer.setMute(true);
            } else {
                isMute = false;
                setIcon(volumeBT, volOnIcon);
                mediaPlayer.setMute(false);
            }
        }
    }




}
