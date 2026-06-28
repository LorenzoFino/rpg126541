package it.unicam.cs.mpgc.rpg126541.util;

/**
 * Elenca le schermate dell'applicazione e il percorso del loro file FXML.
 */
public enum AppScene {

    MENU("/it/unicam/cs/mpgc/rpg126541/fxml/menu.fxml"),
    MAPPA("/it/unicam/cs/mpgc/rpg126541/fxml/mappa.fxml"),
    SCENA("/it/unicam/cs/mpgc/rpg126541/fxml/scena.fxml");

    private final String pathFxml;

    AppScene(String pathFxml) {
        this.pathFxml = pathFxml;
    }

    public String getPathFxml() {
        return pathFxml;
    }
}
