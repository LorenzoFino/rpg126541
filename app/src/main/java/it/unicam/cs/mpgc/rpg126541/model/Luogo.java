package it.unicam.cs.mpgc.rpg126541.model;

import java.util.Objects;

/**
 * Rappresenta un luogo della mappa di Palermo (es. Bar Ciangretta, Porto).
 * Ogni luogo può avere associata una missione, oppure no (idMissione = null).
 * L'identità è basata sull'id.
 */
public class Luogo {

    private final String id;
    private final String nome;
    private final String descrizione;
    private final String idMissione;

    public Luogo(String id, String nome, String descrizione, String idMissione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.idMissione = idMissione;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    /** Restituisce l'id della missione associata, oppure null se il luogo non ha missioni. */
    public String getIdMissione() {
        return idMissione;
    }

    public boolean haMissione() {
        return idMissione != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Luogo altro)) return false;
        return Objects.equals(id, altro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Luogo{id='" + id + "', nome='" + nome + "'}";
    }
}
