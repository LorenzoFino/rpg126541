package it.unicam.cs.mpgc.rpg126541.model;

import java.util.Map;

/**
 * Rappresenta una scelta che il giocatore può fare in una scena.
 * Contiene il testo visibile, gli effetti sulle statistiche (es. +10 Rispetto, -5 Ricchezza)
 * e l'id della scena a cui porta.
 * Non ha un id proprio, quindi non sovrascrive equals/hashCode.
 */
public class Scelta {

    private final String testo;
    private final Map<TipoStatistica, Integer> effetti;
    private final String idScenaSuccessiva;

    public Scelta(String testo, Map<TipoStatistica, Integer> effetti, String idScenaSuccessiva) {
        this.testo = testo;
        this.effetti = effetti;
        this.idScenaSuccessiva = idScenaSuccessiva;
    }

    public String getTesto() {
        return testo;
    }

    /** Restituisce la mappa degli effetti sulle statistiche (delta positivi o negativi). */
    public Map<TipoStatistica, Integer> getEffetti() {
        return effetti;
    }

    /** Restituisce l'id della scena successiva, oppure null se questa scelta termina la missione. */
    public String getIdScenaSuccessiva() {
        return idScenaSuccessiva;
    }

    @Override
    public String toString() {
        return "Scelta{testo='" + testo + "', idScenaSuccessiva='" + idScenaSuccessiva + "'}";
    }
}
