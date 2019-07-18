package com.wojciechsliz.macrocalc;

import com.wojciechsliz.macrocalc.datamodel.Datasource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));
        primaryStage.setTitle("MacroCalc");
        primaryStage.setScene(new Scene(root, 648, 370));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        Datasource.getInstance().open();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
