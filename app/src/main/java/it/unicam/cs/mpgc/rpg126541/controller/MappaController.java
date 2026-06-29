package it.unicam.cs.mpgc.rpg126541.controller;

import it.unicam.cs.mpgc.rpg126541.dto.LuogoDTO;
import it.unicam.cs.mpgc.rpg126541.dto.StatisticheDTO;
import it.unicam.cs.mpgc.rpg126541.persistence.RepositoryPartita;
import it.unicam.cs.mpgc.rpg126541.service.GiocoService;
import it.unicam.cs.mpgc.rpg126541.util.AppScene;
import it.unicam.cs.mpgc.rpg126541.util.NavigatoreSchermate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controller della mappa di Palermo.
 * Crea i bottoni dei luoghi programmaticamente (le posizioni sono definite qui,
 * non nel JSON, perché sono dati di presentazione, non di gioco).
 * Aggiorna il pannello statistiche a ogni accesso alla schermata.
 */
public class MappaController {

    @FXML private AnchorPane areaMappa;
    @FXML private Label labelStato;

    @FXML private Label labelNome;
    @FXML private Label labelRango;

    @FXML private ProgressBar barRispetto;
    @FXML private Label valRispetto;
    @FXML private ProgressBar barRicchezza;
    @FXML private Label valRicchezza;
    @FXML private ProgressBar barAstuzia;
    @FXML private Label valAstuzia;
    @FXML private ProgressBar barLealta;
    @FXML private Label valLealta;

    private GiocoService giocoService;
    private RepositoryPartita repositoryPartita;

    /**
     * Chiamato dal controller precedente dopo aver caricato l'FXML.
     * Inietta le dipendenze e popola la schermata.
     */
    public void setServizi(GiocoService giocoService, RepositoryPartita repositoryPartita) {
        this.giocoService = giocoService;
        this.repositoryPartita = repositoryPartita;
        aggiornaStatistiche();
        creaBottoniLuoghi();
    }

    /** Aggiorna le etichette e le barre del pannello dossier con i valori attuali. */
    private void aggiornaStatistiche() {
        StatisticheDTO s = giocoService.getStatisticheDTO();

        labelNome.setText(s.nomeGiocatore());
        labelRango.setText(s.nomeRango());

        barRispetto.setProgress(s.rispetto() / 100.0);
        valRispetto.setText(String.valueOf(s.rispetto()));

        barRicchezza.setProgress(s.ricchezza() / 100.0);
        valRicchezza.setText(String.valueOf(s.ricchezza()));

        barAstuzia.setProgress(s.astuzia() / 100.0);
        valAstuzia.setText(String.valueOf(s.astuzia()));

        barLealta.setProgress(s.lealta() / 100.0);
        valLealta.setText(String.valueOf(s.lealta()));
    }

    /**
     * Crea un bottone per ogni luogo e lo posiziona nell'AnchorPane
     * in base alle coordinate definite in getPosizione().
     */
    private void creaBottoniLuoghi() {
        for (LuogoDTO luogo : giocoService.getLuoghiDTO()) {
            Button btn = new Button(luogo.nome());
            btn.getStyleClass().add("bottone-luogo");
            btn.setOnAction(e -> apriLuogo(luogo));

            double[] pos = getPosizione(luogo.id());
            AnchorPane.setTopAnchor(btn, pos[0]);
            AnchorPane.setLeftAnchor(btn, pos[1]);

            areaMappa.getChildren().add(btn);
        }
    }

    /**
     * Gestisce il click su un luogo della mappa.
     * Se il luogo ha una missione, la avvia e naviga alla schermata scena.
     * Altrimenti mostra la descrizione del luogo nella barra di stato.
     */
    private void apriLuogo(LuogoDTO luogo) {
        if (!luogo.haMissione()) {
            labelStato.setText(luogo.descrizione());
            return;
        }
        try {
            giocoService.avviaMissione(luogo.idMissione());
            ScenaController scenaController = (ScenaController) NavigatoreSchermate.vai(AppScene.SCENA);
            scenaController.setServizi(giocoService, repositoryPartita);
        } catch (IOException e) {
            System.err.println("[Mappa] Errore nel caricamento della scena: " + e.getMessage());
        }
    }

    /**
     * Restituisce la posizione [top, left] del bottone per il luogo indicato.
     * Le coordinate si riferiscono all'AnchorPane della mappa (area ~780×700 px).
     */
    private double[] getPosizione(String idLuogo) {
        return switch (idLuogo) {
            case "bar_ciangretta"       -> new double[]{200, 290};
            case "porto"                -> new double[]{330, 70};
            case "chiesa_santa_rosalia" -> new double[]{140, 490};
            case "bisca_monreale"       -> new double[]{430, 350};
            default                     -> new double[]{200, 200};
        };
    }
}
