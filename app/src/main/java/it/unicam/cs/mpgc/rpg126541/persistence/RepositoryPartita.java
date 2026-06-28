package it.unicam.cs.mpgc.rpg126541.persistence;

import it.unicam.cs.mpgc.rpg126541.model.Partita;

import java.util.List;

/**
 * Contratto per il salvataggio e il caricamento delle partite.
 * L'implementazione concreta decide il formato (JSON, DB, ecc.).
 */
public interface RepositoryPartita {

    /** Salva la partita nello slot indicato (sovrascrive se già esiste). */
    void salva(Partita partita, String nomeSlot);

    /** Carica la partita dallo slot indicato; restituisce null se lo slot non esiste. */
    Partita carica(String nomeSlot);

    /** Restituisce i nomi di tutti gli slot di salvataggio disponibili. */
    List<String> salvataggiDisponibili();
}
