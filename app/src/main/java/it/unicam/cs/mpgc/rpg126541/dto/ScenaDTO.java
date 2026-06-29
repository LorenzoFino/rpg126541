package it.unicam.cs.mpgc.rpg126541.dto;

import java.util.List;

/**
 * Dati di una scena da mostrare nella view.
 * Contiene solo il testo narrativo e la lista dei bottoni (SceltaDTO):
 * il controller non ha bisogno di vedere i model interni (Scena, Scelta).
 */
public record ScenaDTO(String testo, List<SceltaDTO> scelte) {
}
