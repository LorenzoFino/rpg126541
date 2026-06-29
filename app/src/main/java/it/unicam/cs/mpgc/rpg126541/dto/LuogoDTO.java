package it.unicam.cs.mpgc.rpg126541.dto;

/**
 * Dati di un luogo della mappa da mostrare nella view come bottone cliccabile.
 * Se haMissione è true, idMissione contiene l'id da passare al service per avviarla.
 */
public record LuogoDTO(String id, String nome, String descrizione, boolean haMissione, String idMissione) {
}
