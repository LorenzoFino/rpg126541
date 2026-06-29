package it.unicam.cs.mpgc.rpg126541.dto;

/**
 * Snapshot delle statistiche del giocatore per il pannello HUD.
 * Tutti i valori sono già pronti per la view: interi da 0 a 100 e rango come stringa.
 * Il controller non ha bisogno di importare Giocatore né Rango.
 */
public record StatisticheDTO(
        String nomeGiocatore,
        int rispetto,
        int ricchezza,
        int astuzia,
        int lealta,
        String nomeRango
) {
}
