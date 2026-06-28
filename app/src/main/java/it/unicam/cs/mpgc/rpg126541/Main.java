package it.unicam.cs.mpgc.rpg126541;

import it.unicam.cs.mpgc.rpg126541.util.AppScene;
import it.unicam.cs.mpgc.rpg126541.util.NavigatoreSchermate;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Legge del Silenzio");
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setResizable(false);

        NavigatoreSchermate.inizializza(stage);
        NavigatoreSchermate.vai(AppScene.MENU);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
