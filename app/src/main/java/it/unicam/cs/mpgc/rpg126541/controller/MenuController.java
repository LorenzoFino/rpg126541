package it.unicam.cs.mpgc.rpg126541.controller;

import it.unicam.cs.mpgc.rpg126541.persistence.RepositoryPartita;
import it.unicam.cs.mpgc.rpg126541.service.GiocoService;
import it.unicam.cs.mpgc.rpg126541.util.AppScene;
import it.unicam.cs.mpgc.rpg126541.util.NavigatoreSchermate;
import javafx.application.Platform;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Controller del menu principale.
 * Riceve i servizi tramite setServizi(): dipende dalle interfacce (DIP),
 * non dalle implementazioni concrete, che vengono create solo in Main.
 */
public class MenuController {

    private GiocoService giocoService;
    private RepositoryPartita repositoryPartita;

    /**
     * Chiamato da Main dopo aver caricato l'FXML.
     * Passa le dipendenze come interfacce: il controller non sa
     * (e non deve sapere) quale implementazione concreta sta usando.
     */
    public void setServizi(GiocoService giocoService, RepositoryPartita repositoryPartita) {
        this.giocoService = giocoService;
        this.repositoryPartita = repositoryPartita;
    }

    @FXML
    private void nuovaPartita() {
        try {
            NavigatoreSchermate.vai(AppScene.MAPPA);
        } catch (IOException e) {
            System.err.println("[Menu] Errore nel caricamento della mappa: " + e.getMessage());
        }
    }

    @FXML
    private void caricaPartita() {
        // TODO: mostrare la lista dei salvataggi e caricare la partita scelta.
        // repositoryPartita.salvataggiDisponibili() restituirà gli slot esistenti.
        System.out.println("[Menu] Carica partita: funzionalità da implementare.");
    }

    @FXML
    private void esci() {
        Platform.exit();
    }
}
