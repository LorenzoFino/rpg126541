package it.unicam.cs.mpgc.rpg126541.persistence;

import it.unicam.cs.mpgc.rpg126541.model.Luogo;
import it.unicam.cs.mpgc.rpg126541.model.Missione;

import java.util.List;

/**
 * Interfaccia per il caricamento dei contenuti di gioco (luoghi e missioni).
 * Nasconde il dettaglio di come i dati vengono letti (JSON, DB, ecc.).
 */
public interface CaricatoreContenuti {

    /** Restituisce la lista di tutti i luoghi della mappa. */
    List<Luogo> caricaLuoghi();

    /** Restituisce la lista di tutte le missioni con le loro scene e scelte. */
    List<Missione> caricaMissioni();
}
