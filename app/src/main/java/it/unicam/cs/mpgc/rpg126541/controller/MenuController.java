package it.unicam.cs.mpgc.rpg126541.controller;

import it.unicam.cs.mpgc.rpg126541.model.Partita;
import it.unicam.cs.mpgc.rpg126541.persistence.RepositoryPartita;
import it.unicam.cs.mpgc.rpg126541.service.GiocoService;
import it.unicam.cs.mpgc.rpg126541.util.AppScene;
import it.unicam.cs.mpgc.rpg126541.util.NavigatoreSchermate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller del menu principale.
 * Gestisce nuova partita, caricamento da file ed uscita.
 */
public class MenuController {

    private GiocoService giocoService;
    private RepositoryPartita repositoryPartita;

    public void setServizi(GiocoService giocoService, RepositoryPartita repositoryPartita) {
        this.giocoService = giocoService;
        this.repositoryPartita = repositoryPartita;
    }

    @FXML
    private void nuovaPartita() {
        try {
            giocoService.nuovaPartita("Salvatore Ciangretta");
            MappaController mappaController = (MappaController) NavigatoreSchermate.vai(AppScene.MAPPA);
            mappaController.setServizi(giocoService, repositoryPartita);
        } catch (IOException e) {
            System.err.println("[Menu] Errore nel caricamento della mappa: " + e.getMessage());
        }
    }

    @FXML
    private void caricaPartita() {
        List<String> slots = repositoryPartita.salvataggiDisponibili();

        if (slots.isEmpty()) {
            Alert avviso = new Alert(Alert.AlertType.INFORMATION);
            avviso.setTitle("Nessun salvataggio");
            avviso.setHeaderText(null);
            avviso.setContentText("Non ci sono partite salvate.");
            avviso.showAndWait();
            return;
        }

        ChoiceDialog<String> dialogo = new ChoiceDialog<>(slots.get(0), slots);
        dialogo.setTitle("Carica Partita");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Scegli il salvataggio:");
        Optional<String> scelta = dialogo.showAndWait();

        if (scelta.isEmpty()) {
            return; // l'utente ha premuto Annulla
        }

        Partita partitaCaricata = repositoryPartita.carica(scelta.get());
        if (partitaCaricata == null) {
            Alert errore = new Alert(Alert.AlertType.ERROR);
            errore.setTitle("Errore");
            errore.setHeaderText(null);
            errore.setContentText("File di salvataggio corrotto o non leggibile.");
            errore.showAndWait();
            return;
        }

        giocoService.impostaPartita(partitaCaricata);
        navigaDopoCaricamento(partitaCaricata);
    }

    /**
     * Naviga alla scena corretta dopo il caricamento:
     * se era nel mezzo di una missione torna alla scena interrotta,
     * altrimenti va alla mappa.
     */
    private void navigaDopoCaricamento(Partita partita) {
        try {
            if (partita.getIdScenaCorrente() != null) {
                ScenaController scenaController = (ScenaController) NavigatoreSchermate.vai(AppScene.SCENA);
                scenaController.setServizi(giocoService, repositoryPartita);
            } else {
                MappaController mappaController = (MappaController) NavigatoreSchermate.vai(AppScene.MAPPA);
                mappaController.setServizi(giocoService, repositoryPartita);
            }
        } catch (IOException e) {
            System.err.println("[Menu] Errore nel caricamento della schermata: " + e.getMessage());
        }
    }

    @FXML
    private void esci() {
        Platform.exit();
    }
}
