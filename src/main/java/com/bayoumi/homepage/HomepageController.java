package com.bayoumi.homepage;

import com.bayoumi.util.FileUtils;
import com.bayoumi.util.PropertiesUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
    private final FontAwesomeIconView playIcon = new FontAwesomeIconView(FontAwesomeIcon.PLAY);
    private final FontAwesomeIconView pauseIcon = new FontAwesomeIconView(FontAwesomeIcon.PAUSE);
    private final ObservableList<Path> playListFiles = FXCollections.observableArrayList();
    //    private MediaPlayer player;
    //    public AnchorPane mediaViewPane;
    @FXML
    private StackPane sp;
    @FXML
    private MediaView mediaView;
    @FXML
    private JFXButton playBtn;
    @FXML
    private JFXButton preBtn;
    @FXML
    private JFXButton nextBtn;
    @FXML
    private JFXSlider timeSlider;
    @FXML
    private JFXSlider volumeSlider;
    @FXML
    private OctIconView volume;
    @FXML
    private Label statusLabel;
    private double previousValue = 50;
    private boolean isMuted = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeSlider.setValue(0);
        // volume config
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            playBtn.requestFocus();
            previousValue = (double) oldValue;
            if (null != mediaView.getMediaPlayer()) {
                // multiply duration by percentage calculated by
                // slider position
                if (volumeSlider.getValue() > 0) {
                    volume.setIcon(OctIcon.UNMUTE);
                } else if (volumeSlider.getValue() == 0) {
                    volume.setIcon(OctIcon.MUTE);
                }
                mediaView.getMediaPlayer().setVolume(volumeSlider.getValue() / 100.0);
            }
        });
        // responsive mediaView
        mediaView.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                DoubleProperty mvw = mediaView.fitWidthProperty();
                DoubleProperty mvh = mediaView.fitHeightProperty();
                mvw.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                mvh.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height").subtract(32).subtract(86));
                mediaView.setPreserveRatio(true);
            }

            applyDragAndDropFeatures(mediaView.getScene());
        });
        // icons style
        playIcon.setGlyphSize(22);
        playIcon.setStyle("-fx-fill: white;");
        pauseIcon.setGlyphSize(22);
        pauseIcon.setStyle("-fx-fill: white;");
    }

    @FXML
    private void OpenFileAction(ActionEvent event) {
        try {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);
            if (file == null) {
                return;
            }
            Media m = new Media(file.toURI().toURL().toString());
            System.out.println("RRR: " + m.getSource());
//            if (mediaView.getMediaPlayer() != null) {
//                mediaView.getMediaPlayer().dispose();
//            }
            playVideo(file.toURI().toURL().toString(), file.getName());
//            MediaPlayer player = new MediaPlayer(m);
//            mediaView.setMediaPlayer(player);
            playBtnClick(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeAction() {
        ((Stage) playBtn.getScene().getWindow()).close();
    }

    @FXML
    private void playBtnClick(Event event) {
        try {
            MediaPlayer player = mediaView.getMediaPlayer();
            if (null == player) {
                event.consume();
            } else {
                MediaPlayer.Status status = player.getStatus();
                if (status == MediaPlayer.Status.PLAYING) {
                    player.pause();
                    statusLabel.setText("paused");
                    playBtn.setGraphic(playIcon);
                } else {
                    player.play();
                    statusLabel.setText("playing");
                    playBtn.setGraphic(pauseIcon);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preBtnClick(Event event) {
        MediaPlayer player = mediaView.getMediaPlayer();
        if (null == player) {
            event.consume();
        } else {
            double d = player.getCurrentTime().toSeconds();
            d = d - 10;
            player.seek(new Duration(d * 1000));
        }
    }

    private void nextBtnClick(Event event) {
        MediaPlayer player = mediaView.getMediaPlayer();
        if (null == player) {
            event.consume();
        } else {
            double d = player.getCurrentTime().toSeconds();
            d = d + 10;
            player.seek(new Duration(d * 1000));
        }
    }

    @FXML
    private void keyEv(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            nextBtnClick(keyEvent);
        } else if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            preBtnClick(keyEvent);
        } else if (keyEvent.getCode().equals(KeyCode.SPACE)) {
            playBtnClick(keyEvent);
        }
    }

    @FXML
    private void muteUnmute(Event event) {
        if (isMuted) {
            // umMute
            isMuted = false;
            volumeSlider.setValue(previousValue);
        } else {
            // mute
            isMuted = true;
            previousValue = volumeSlider.getValue();
            volumeSlider.setValue(0);
        }
    }

    @FXML
    private void scrollEv(ScrollEvent scrollEvent) {
        if (scrollEvent.getDeltaY() > 0) {
            volumeSlider.setValue(volumeSlider.getValue() == 100 ? 100 : (volumeSlider.getValue() + 1));
        } else {
            volumeSlider.setValue(volumeSlider.getValue() == 0 ? 0 : (volumeSlider.getValue() - 1));
        }
    }

    @FXML
    public void mediaViewClicked(MouseEvent event) {
        playBtn.fire();
    }

    public void applyDragAndDropFeatures(Scene scene) {
        try {
            scene.setOnDragOver((dragEvent) -> {
                Dragboard db = dragEvent.getDragboard();
                if (db.hasFiles()) {
                    dragEvent.acceptTransferModes(TransferMode.COPY);
                } else {
                    dragEvent.consume();
                }
            });

            scene.setOnDragDropped((dragEvent) -> {
                Dragboard db = dragEvent.getDragboard();
                if (db.hasFiles()) {
                    for (Path filePath : FileUtils.convertListFileToListPath(db.getFiles())) {
                        try {
                            if (PropertiesUtils.readFormats().contains("*" + filePath.toAbsolutePath().toString().substring(filePath.toAbsolutePath().toString().length() - 4))) {
                                if (null != mediaView.getMediaPlayer())
                                    mediaView.getMediaPlayer().stop();
                                playListFiles.add(filePath);
                                playVideo(filePath.toFile().toURI().toURL().toString(), filePath.toFile().getName());
                            } else {
                                System.out.println("ERROR !");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            scene.addEventFilter(KeyEvent.KEY_PRESSED, (keyEvent) -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    ((Stage) scene.getWindow()).setFullScreen(false);
                }
            });

            mediaView.addEventFilter(MouseEvent.MOUSE_PRESSED, (mouseEvent) -> {
                if (mouseEvent.getButton().equals(
                        MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        ((Stage) scene.getWindow()).setFullScreen(!((Stage) scene.getWindow()).isFullScreen());
                    }
                }
            });

//            scene.addEventFilter(MouseEvent.MOUSE_MOVED, (mouseEvent) -> {
//                if (stage.isFullScreen()) {
//                    showTempMediaControlBar();
//                } else {
//                    showConstantMediaControlBar();
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void prevFileAction(ActionEvent event) {
    }

    @FXML
    public void nextFileAction(ActionEvent event) {
    }

    @FXML
    public void stopBtnClick(ActionEvent event) {
        MediaPlayer player = mediaView.getMediaPlayer();
        if (null != player) {
            statusLabel.setText("stopped");
            playBtn.setGraphic(playIcon);
            player.stop();
            timeSlider.setValue(0);
        } else {
            System.out.println("TTT");
            event.consume();
        }
    }

    private void checkAndStopMediaPlayer() {
        if (null != mediaView.getMediaPlayer()) {
            stopBtnClick(null);
            mediaView.setMediaPlayer(null);
        }
    }

    private void playVideo(String MEDIA_URL, String fileName) {
        try {
            System.out.println("=============================");
//            String MEDIA_URL_FOR_MEDIA = URLEncoder.encode(MEDIA_URL, "UTF-8");
//            MEDIA_URL_FOR_MEDIA = "file:/"
//                    + (MEDIA_URL_FOR_MEDIA).replace("\\", "/").replace("+", "%20");
            System.out.println("GG: " + MEDIA_URL);
            Media media = new Media(MEDIA_URL);
            // create media player
            checkAndStopMediaPlayer();
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            bindMediaPlayerControls(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            System.out.println("fileName : " + fileName);
            ((Stage) mediaView.getScene().getWindow()).setTitle(fileName + " - MediaPlayerFX");
            playBtn.fire();
//            mediaView.autosize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindMediaPlayerControls(final MediaPlayer player) {
        player.setOnPlaying(() -> {
            //when player gets ready..
            timeSlider.setMin(0);
            System.out.println(player.getMedia().getSource() + ": " + player.getMedia().getDuration().toMinutes());
            timeSlider.setMax(player.getMedia().getDuration().toMinutes());
            timeSlider.setValue(0);
        });
        //listener on player...
        player.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            Duration d = player.getCurrentTime();
            timeSlider.setValue(d.toMinutes());
        });
        timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (timeSlider.isPressed()) {
                double val = timeSlider.getValue();
                player.seek(new Duration(val * 60 * 1000));
                playBtn.requestFocus();
            }
        });
        player.setVolume(volumeSlider.getValue() / 100.0);

        player.setOnEndOfMedia(() -> {
            System.out.println("end : "+player.getMedia().getSource());
            System.out.println("end media ..");
            stopBtnClick(null);

        });

    }

}
