package it.unicam.cs.mpgc.rpg126541;

import it.unicam.cs.mpgc.rpg126541.controller.MenuController;
import it.unicam.cs.mpgc.rpg126541.persistence.CaricatoreContenutiJson;
import it.unicam.cs.mpgc.rpg126541.persistence.RepositoryPartita;
import it.unicam.cs.mpgc.rpg126541.persistence.RepositoryPartitaJson;
import it.unicam.cs.mpgc.rpg126541.service.GiocoService;
import it.unicam.cs.mpgc.rpg126541.service.GiocoServiceImpl;
import it.unicam.cs.mpgc.rpg126541.util.AppScene;
import it.unicam.cs.mpgc.rpg126541.util.NavigatoreSchermate;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto di ingresso dell'applicazione.
 * Questo è il "composition root": l'unico posto dove vengono create
 * le implementazioni concrete dei servizi, che vengono poi passate
 * ai controller come interfacce (Dependency Inversion).
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Legge del Silenzio");
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setResizable(false);

        // Creazione dei servizi concreti (solo qui, mai nei controller).
        GiocoService giocoService = new GiocoServiceImpl(new CaricatoreContenutiJson());
        RepositoryPartita repository = new RepositoryPartitaJson();

        // vai() restituisce il controller appena creato dall'FXMLLoader.
        NavigatoreSchermate.inizializza(stage);
        MenuController menuController = (MenuController) NavigatoreSchermate.vai(AppScene.MENU);
        menuController.setServizi(giocoService, repository);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
