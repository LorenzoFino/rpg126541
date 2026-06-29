package it.unicam.cs.mpgc.rpg126541.dto;

/**
 * Dati di una singola scelta da mostrare nella view come bottone.
 * L'indice identifica la posizione nella lista scelte della scena corrente:
 * il controller lo passa al service per sapere quale scelta è stata fatta.
 */
public record SceltaDTO(String testo, int indice) {
}
