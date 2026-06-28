package it.unicam.cs.mpgc.rpg126541.service;

import it.unicam.cs.mpgc.rpg126541.model.Partita;
import it.unicam.cs.mpgc.rpg126541.model.Scena;
import it.unicam.cs.mpgc.rpg126541.model.Scelta;

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

    /** Restituisce la scena corrente della partita in corso. */
    Scena getScenaCorrente();

    /**
     * Applica la scelta del giocatore: aggiorna le statistiche e avanza
     * alla scena successiva indicata nella scelta stessa.
     */
    void applicaScelta(Scelta scelta);

    /**
     * Restituisce true se la missione corrente è terminata,
     * ovvero se la scena corrente non ha una scena successiva (fine ramo narrativo).
     */
    boolean missioneTerminata();

    /** Restituisce lo stato completo della partita in corso. */
    Partita getPartita();
}
