package it.unicam.cs.mpgc.rpg126541.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Gestisce la navigazione tra le schermate dell'applicazione.
 * Tiene un riferimento allo Stage principale e, ad ogni chiamata a vai(),
 * carica il file FXML corrispondente e aggiorna la scena mostrata.
 */
public class NavigatoreSchermate {

    private static final String PATH_CSS = "/it/unicam/cs/mpgc/rpg126541/css/stile.css";

    private static Stage stage;

    /** Deve essere chiamato una sola volta, all'avvio, per registrare lo Stage principale. */
    public static void inizializza(Stage stagePrincipale) {
        stage = stagePrincipale;
    }

    /**
     * Carica il file FXML della schermata indicata, lo mostra nello Stage
     * e restituisce il controller creato dall'FXMLLoader.
     * Il chiamante può fare il cast al tipo concreto e iniettare i servizi
     * tramite un metodo setter prima che l'utente interagisca.
     *
     * @return il controller associato all'FXML caricato
     * @throws IOException se il file FXML non viene trovato o contiene errori
     */
    public static Object vai(AppScene schermata) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                NavigatoreSchermate.class.getResource(schermata.getPathFxml())
        );
        Parent root = loader.load();
        Scene scene = new Scene(root);

        String urlCss = NavigatoreSchermate.class.getResource(PATH_CSS) != null
                ? NavigatoreSchermate.class.getResource(PATH_CSS).toExternalForm()
                : null;
        if (urlCss != null) {
            scene.getStylesheets().add(urlCss);
        }

        stage.setScene(scene);
        return loader.getController();
    }
}
