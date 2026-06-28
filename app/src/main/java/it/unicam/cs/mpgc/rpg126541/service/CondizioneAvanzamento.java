package it.unicam.cs.mpgc.rpg126541.service;

import it.unicam.cs.mpgc.rpg126541.model.Partita;

/**
 * Condizione che determina se un giocatore può avanzare di rango.
 * È un'interfaccia funzionale: ogni condizione si esprime con una sola riga di lambda.
 */
@FunctionalInterface
public interface CondizioneAvanzamento {

    /** Restituisce true se la partita corrente soddisfa i requisiti di avanzamento. */
    boolean soddisfatta(Partita partita);
}
