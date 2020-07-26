package com.bayoumi.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoadScreens {

    private static LoadScreens loadScreens = null;

    public Parent root_Homepage;
    public Parent root_About;

    private LoadScreens() {
        loadAll();
    }

    public static LoadScreens getInstance() {
        if (loadScreens == null) {
            loadScreens = new LoadScreens();
        }
        return loadScreens;
    }


    private void loadAll() {
        try {
            root_Homepage =  FXMLLoader.load(getClass().getResource("/com/bayoumi/fxml/homepage/homepage.fxml"));
        } catch (Exception ex) {
            Logger.error(null, ex, getClass().getName() + ".loadAll()");
        }
    }
}
