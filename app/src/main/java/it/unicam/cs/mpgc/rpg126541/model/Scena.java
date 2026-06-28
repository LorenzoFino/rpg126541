package it.unicam.cs.mpgc.rpg126541.model;

import java.util.List;
import java.util.Objects;

/**
 * Rappresenta una singola scena di gioco: un momento narrativo con testo
 * e una lista di scelte disponibili per il giocatore.
 * L'identità è basata sull'id.
 */
public class Scena {

    private final String id;
    private final String testo;
    private final List<Scelta> scelte;

    public Scena(String id, String testo, List<Scelta> scelte) {
        this.id = id;
        this.testo = testo;
        this.scelte = scelte;
    }

    public String getId() {
        return id;
    }

    public String getTesto() {
        return testo;
    }

    public List<Scelta> getScelte() {
        return scelte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scena altra)) return false;
        return Objects.equals(id, altra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Scena{id='" + id + "'}";
    }
}
