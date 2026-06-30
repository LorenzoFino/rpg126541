package it.unicam.cs.mpgc.rpg126541.service;

import it.unicam.cs.mpgc.rpg126541.dto.LuogoDTO;
import it.unicam.cs.mpgc.rpg126541.dto.ScenaDTO;
import it.unicam.cs.mpgc.rpg126541.dto.StatisticheDTO;
import it.unicam.cs.mpgc.rpg126541.model.Partita;

import java.util.List;

/**
 * Orchestratore della logica di gioco.
 * Gestisce lo scorrere della storia: avvia missioni, restituisce la scena corrente,
 * applica le scelte del giocatore e segnala quando una missione è terminata.
 * Non dipende da JavaFX: i controller la usano tramite questa interfaccia.
 */
public interface GiocoService {

    /** Avvia una nuova partita con il nome del protagonista indicato. */
    void nuovaPartita(String nomeGiocatore);

    /**
     * Entra nella missione identificata dall'id dato, posizionando il giocatore
     * sulla sua prima scena.
     */
    void avviaMissione(String idMissione);

    /** Restituisce i dati della scena corrente pronti per la view (testo + lista scelte). */
    ScenaDTO getScenaDTO();

    /**
     * Applica la scelta all'indice dato nella lista della scena corrente:
     * aggiorna le statistiche e avanza alla scena successiva.
     */
    void applicaScelta(int indiceScelta);

    /**
     * Restituisce true se la missione corrente è terminata,
     * ovvero se la scena corrente non ha una scena successiva (fine ramo narrativo).
     */
    boolean missioneTerminata();

    /** Restituisce lo stato completo della partita in corso. */
    Partita getPartita();

    /** Ripristina lo stato di gioco da una partita caricata da file. */
    void impostaPartita(Partita partita);

    /** Restituisce uno snapshot delle statistiche del giocatore, pronto per la view. */
    StatisticheDTO getStatisticheDTO();

    /** Restituisce la lista dei luoghi della mappa come DTO, pronti per la view. */
    List<LuogoDTO> getLuoghiDTO();
}
