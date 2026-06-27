package it.unicam.cs.mpgc.rpg126541.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Rappresenta il protagonista della partita: Salvatore Ciangretta.
 * Tiene traccia del nome, delle quattro statistiche e del rango corrente.
 * L'identità è basata sull'id: due Giocatore con lo stesso id sono la stessa entità.
 */
public class Giocatore {

    private static final int STATISTICA_INIZIALE = 50;
    private static final int STATISTICA_MIN = 0;
    private static final int STATISTICA_MAX = 100;

    private final String id;
    private final String nome;
    private final Map<TipoStatistica, Integer> statistiche;
    private Rango rango;

    public Giocatore(String id, String nome) {
        this.id = id;
        this.nome = nome;
        this.rango = Rango.ASPIRANTE;
        this.statistiche = new EnumMap<>(TipoStatistica.class);
        for (TipoStatistica tipo : TipoStatistica.values()) {
            statistiche.put(tipo, STATISTICA_INIZIALE);
        }
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Rango getRango() {
        return rango;
    }

    public void setRango(Rango rango) {
        this.rango = rango;
    }

    public int getStatistica(TipoStatistica tipo) {
        return statistiche.get(tipo);
    }

    /**
     * Modifica una statistica applicando un delta (positivo o negativo).
     * Il valore rimane sempre tra STATISTICA_MIN e STATISTICA_MAX.
     */
    public void modificaStatistica(TipoStatistica tipo, int delta) {
        int nuovoValore = statistiche.get(tipo) + delta;
        nuovoValore = Math.max(STATISTICA_MIN, Math.min(STATISTICA_MAX, nuovoValore));
        statistiche.put(tipo, nuovoValore);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Giocatore altro)) return false;
        return Objects.equals(id, altro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Giocatore{id='" + id + "', nome='" + nome + "', rango=" + rango + "}";
    }
}
