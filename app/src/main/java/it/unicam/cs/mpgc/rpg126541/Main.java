package it.unicam.cs.mpgc.rpg126541;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("RPG - Lorenzo Fino");
        Scene scene = new Scene(new StackPane(label), 640, 480);
        stage.setTitle("RPG 126541");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}