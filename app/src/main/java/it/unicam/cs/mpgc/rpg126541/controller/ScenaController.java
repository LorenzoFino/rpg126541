package it.unicam.cs.mpgc.rpg126541.controller;

import it.unicam.cs.mpgc.rpg126541.dto.SceltaDTO;
import it.unicam.cs.mpgc.rpg126541.dto.ScenaDTO;
import it.unicam.cs.mpgc.rpg126541.dto.StatisticheDTO;
import it.unicam.cs.mpgc.rpg126541.persistence.RepositoryPartita;
import it.unicam.cs.mpgc.rpg126541.service.GiocoService;
import it.unicam.cs.mpgc.rpg126541.util.AppScene;
import it.unicam.cs.mpgc.rpg126541.util.NavigatoreSchermate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Controller della schermata di gioco.
 * Mostra il testo narrativo della scena corrente, il pannello statistiche
 * e i bottoni per le scelte del giocatore.
 * Dopo ogni scelta aggiorna la view o torna alla mappa se la missione è finita.
 */
public class ScenaController {

    @FXML private Label labelTestoScena;
    @FXML private VBox pannelloScelte;

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
     * Inietta le dipendenze e popola la schermata con la scena corrente.
     */
    public void setServizi(GiocoService giocoService, RepositoryPartita repositoryPartita) {
        this.giocoService = giocoService;
        this.repositoryPartita = repositoryPartita;
        mostraScenaCorrente();
    }

    /** Aggiorna testo, statistiche e bottoni in base alla scena corrente del service. */
    private void mostraScenaCorrente() {
        aggiornaStatistiche();
        ScenaDTO scena = giocoService.getScenaDTO();
        labelTestoScena.setText(scena.testo());
        creaBottoniScelte(scena.scelte());
    }

    /** Aggiorna etichette e barre del pannello dossier con i valori attuali del giocatore. */
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
     * Svuota il pannello delle scelte e ricrea un bottone per ogni SceltaDTO.
     * Il bottone passa il proprio indice al service quando viene cliccato.
     */
    private void creaBottoniScelte(List<SceltaDTO> scelte) {
        pannelloScelte.getChildren().clear();
        for (SceltaDTO scelta : scelte) {
            Button btn = new Button(scelta.testo());
            btn.getStyleClass().add("bottone-scelta");
            btn.setOnAction(e -> onSceltaCliccata(scelta.indice()));
            pannelloScelte.getChildren().add(btn);
        }
    }

    /**
     * Gestisce il click su una scelta: la applica tramite il service,
     * poi mostra la scena successiva o torna alla mappa se la missione è finita.
     */
    private void onSceltaCliccata(int indice) {
        giocoService.applicaScelta(indice);
        if (giocoService.missioneTerminata()) {
            tornaAllaMappa();
        } else {
            mostraScenaCorrente();
        }
    }

    /** Naviga alla schermata mappa e le inietta i servizi aggiornati. */
    private void tornaAllaMappa() {
        try {
            MappaController mappaController = (MappaController) NavigatoreSchermate.vai(AppScene.MAPPA);
            mappaController.setServizi(giocoService, repositoryPartita);
        } catch (IOException e) {
            System.err.println("[Scena] Errore nel ritorno alla mappa: " + e.getMessage());
        }
    }
}
