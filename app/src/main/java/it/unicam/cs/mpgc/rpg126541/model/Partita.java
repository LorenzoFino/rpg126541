package it.unicam.cs.mpgc.rpg126541.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Rappresenta lo stato completo di una partita in corso.
 * È l'oggetto che viene serializzato e salvato su file.
 * Contiene il giocatore, il punto in cui si trova nella storia,
 * e i flag che ricordano le scelte già fatte.
 */
public class Partita {

    private final Giocatore giocatore;
    private String idMissioneCorrente;
    private String idScenaCorrente;
    private String nomeSlotCorrente;
    private final Map<String, String> flag;

    public Partita(Giocatore giocatore) {
        this.giocatore = giocatore;
        this.idMissioneCorrente = null;
        this.idScenaCorrente = null;
        this.flag = new HashMap<>();
    }

    public Giocatore getGiocatore() {
        return giocatore;
    }

    public String getIdMissioneCorrente() {
        return idMissioneCorrente;
    }

    public void setIdMissioneCorrente(String idMissioneCorrente) {
        this.idMissioneCorrente = idMissioneCorrente;
    }

    public String getIdScenaCorrente() {
        return idScenaCorrente;
    }

    public void setIdScenaCorrente(String idScenaCorrente) {
        this.idScenaCorrente = idScenaCorrente;
    }

    public String getNomeSlotCorrente() {
        return nomeSlotCorrente;
    }

    public void setNomeSlotCorrente(String nomeSlotCorrente) {
        this.nomeSlotCorrente = nomeSlotCorrente;
    }

    /** Imposta un flag di gioco (es. "porto_accordo" → "completato"). */
    public void setFlag(String chiave, String valore) {
        flag.put(chiave, valore);
    }

    /** Restituisce il valore di un flag, oppure null se non è mai stato impostato. */
    public String getFlag(String chiave) {
        return flag.get(chiave);
    }

    /** Verifica se un flag esiste ed ha un certo valore. */
    public boolean flagUguale(String chiave, String valore) {
        return valore.equals(flag.get(chiave));
    }

    public Map<String, String> getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return "Partita{giocatore=" + giocatore + ", missione='" + idMissioneCorrente
                + "', scena='" + idScenaCorrente + "'}";
    }
}
