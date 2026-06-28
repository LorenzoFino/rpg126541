package it.unicam.cs.mpgc.rpg126541.model;

import java.util.List;
import java.util.Objects;

/**
 * Rappresenta una missione del gioco, composta da una sequenza di scene.
 * L'identità è basata sull'id.
 */
public class Missione {

    private final String id;
    private final String titolo;
    private final List<Scena> scene;

    public Missione(String id, String titolo, List<Scena> scene) {
        this.id = id;
        this.titolo = titolo;
        this.scene = scene;
    }

    public String getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public List<Scena> getScene() {
        return scene;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Missione altra)) return false;
        return Objects.equals(id, altra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Missione{id='" + id + "', titolo='" + titolo + "'}";
    }
}
