package com.bayoumi.util;


import com.bayoumi.Launcher;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.util.HashMap;

public class Utility {


    //========= Helper Objects =========
    private static final String CLASS_NAME = Utility.class.getName();

    public static void ExitKeyCodeCombination(Scene scene, Stage stage) {
        try {
            HashMap<KeyCombination, Runnable> hashMap = new HashMap<>();
            // CTRL + Q
            KeyCombination kc1 = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
            Runnable rn1 = () -> {
                exitProgramAction();
                stage.close();
            };
            hashMap.put(kc1, rn1);
            scene.getAccelerators().putAll(hashMap);
        } catch (Exception ex) {
            Logger.error(null, ex, CLASS_NAME + ".ExitKeyCodeCombination()");
        }
    }

    public static void SetAppDecoration(Stage stage) {
        stage.setTitle("MediaPlayerFX");
        setIcon(stage);
    }

    public static void setIcon(Stage stage) {
        stage.getIcons().clear();
        stage.getIcons().add(new Image("/com/bayoumi/image/icon.png"));
    }

    public static void exitProgramAction() {
        // code
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Long exitTime = System.currentTimeMillis();
            Logger.info("App closed - Used for "
                    + (exitTime - Launcher.startTime) + " ms\n");
        }));
//        System.exit(0);
    }

}