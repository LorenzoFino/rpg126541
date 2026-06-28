package it.unicam.cs.mpgc.rpg126541.service;

import it.unicam.cs.mpgc.rpg126541.model.Giocatore;
import it.unicam.cs.mpgc.rpg126541.model.Missione;
import it.unicam.cs.mpgc.rpg126541.model.Partita;
import it.unicam.cs.mpgc.rpg126541.model.Rango;
import it.unicam.cs.mpgc.rpg126541.model.Scelta;
import it.unicam.cs.mpgc.rpg126541.model.Scena;
import it.unicam.cs.mpgc.rpg126541.model.TipoStatistica;
import it.unicam.cs.mpgc.rpg126541.persistence.CaricatoreContenuti;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementazione concreta di GiocoService.
 * Carica i contenuti una volta sola alla costruzione, poi li usa per
 * navigare tra scene e applicare le scelte del giocatore.
 */
public class GiocoServiceImpl implements GiocoService {

    // Condizioni di avanzamento: modificare qui per cambiare le regole di progressione.
    // Per LUOGOTENENTE basta avere abbastanza Rispetto.
    private static final CondizioneAvanzamento CONDIZIONE_LUOGOTENENTE =
            p -> p.getGiocatore().getStatistica(TipoStatistica.RISPETTO) >= 70;

    // Per BOSS servono Rispetto alto, Lealtà solida E aver completato la prima missione.
    private static final CondizioneAvanzamento CONDIZIONE_BOSS =
            p -> p.getGiocatore().getStatistica(TipoStatistica.RISPETTO) >= 90
              && p.getGiocatore().getStatistica(TipoStatistica.LEALTA) >= 65
              && p.flagUguale("missione_debito_completata", "true");

    private final List<Missione> missioni;
    private Partita partita;

    public GiocoServiceImpl(CaricatoreContenuti caricatore) {
        this.missioni = caricatore.caricaMissioni();
    }

    @Override
    public void nuovaPartita(String nomeGiocatore) {
        Giocatore giocatore = new Giocatore(UUID.randomUUID().toString(), nomeGiocatore);
        partita = new Partita(giocatore);
    }

    @Override
    public void avviaMissione(String idMissione) {
        Missione missione = trovaMissione(idMissione);
        partita.setIdMissioneCorrente(idMissione);
        partita.setIdScenaCorrente(missione.getScene().get(0).getId());
    }

    @Override
    public Scena getScenaCorrente() {
        if (partita == null || partita.getIdScenaCorrente() == null) {
            return null;
        }
        Missione missione = trovaMissione(partita.getIdMissioneCorrente());
        return trovaScena(missione, partita.getIdScenaCorrente());
    }

    @Override
    public void applicaScelta(Scelta scelta) {
        // 1. Applica ogni effetto della scelta alle statistiche del giocatore
        for (Map.Entry<TipoStatistica, Integer> effetto : scelta.getEffetti().entrySet()) {
            partita.getGiocatore().modificaStatistica(effetto.getKey(), effetto.getValue());
        }

        // 2. Avanza alla scena successiva (null = fine missione)
        partita.setIdScenaCorrente(scelta.getIdScenaSuccessiva());

        // 3. Se la missione è finita, salva un flag che la ricorda
        if (scelta.getIdScenaSuccessiva() == null) {
            partita.setFlag(partita.getIdMissioneCorrente() + "_completata", "true");
        }

        // 4. Verifica se il giocatore ha guadagnato un nuovo rango
        aggiornaRango();
    }

    @Override
    public boolean missioneTerminata() {
        return partita != null && partita.getIdScenaCorrente() == null;
    }

    @Override
    public Partita getPartita() {
        return partita;
    }

    // --- metodi privati di supporto ---

    private void aggiornaRango() {
        Giocatore giocatore = partita.getGiocatore();
        Rango attuale = giocatore.getRango();

        if (attuale == Rango.ASPIRANTE && CONDIZIONE_LUOGOTENENTE.soddisfatta(partita)) {
            giocatore.setRango(Rango.LUOGOTENENTE);
        } else if (attuale == Rango.LUOGOTENENTE && CONDIZIONE_BOSS.soddisfatta(partita)) {
            giocatore.setRango(Rango.BOSS);
        }
    }

    private Missione trovaMissione(String idMissione) {
        return missioni.stream()
                .filter(m -> m.getId().equals(idMissione))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missione non trovata: " + idMissione));
    }

    private Scena trovaScena(Missione missione, String idScena) {
        return missione.getScene().stream()
                .filter(s -> s.getId().equals(idScena))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Scena non trovata: " + idScena));
    }
}
