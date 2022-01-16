package com.bayoumi;

import com.bayoumi.util.LoadScreens;
import com.bayoumi.util.Logger;
import com.bayoumi.util.Utility;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Launcher extends Application {
    public static Long startTime;
    private Parent root = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        System.out.println("init start ...");
        // init logger
        startTime = System.currentTimeMillis();
        Logger.init();
        Logger.info("App Launched");

        LoadScreens screens = LoadScreens.getInstance();
        root = screens.root_Homepage;
        System.out.println("init end ...");
    }
    @Override
    public void start(Stage primaryStage) {
        // Scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        // Stage
        Utility.ExitKeyCodeCombination(scene, primaryStage);
        Utility.SetAppDecoration(primaryStage);
        primaryStage.setOnCloseRequest((e) -> Utility.exitProgramAction());
        primaryStage.show();
    }
}
