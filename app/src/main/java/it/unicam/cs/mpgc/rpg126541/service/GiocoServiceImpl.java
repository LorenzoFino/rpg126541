package it.unicam.cs.mpgc.rpg126541.service;

import it.unicam.cs.mpgc.rpg126541.dto.LuogoDTO;
import it.unicam.cs.mpgc.rpg126541.dto.SceltaDTO;
import it.unicam.cs.mpgc.rpg126541.dto.ScenaDTO;
import it.unicam.cs.mpgc.rpg126541.dto.StatisticheDTO;
import it.unicam.cs.mpgc.rpg126541.model.Giocatore;
import it.unicam.cs.mpgc.rpg126541.model.Luogo;
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
    // Per LUOGOTENENTE serve abbastanza Rispetto E aver chiuso il primo capitolo della storia
    // (senza il vincolo sulla missione, il Rispetto da solo può superare la soglia già a metà
    // della prima missione, promuovendo il giocatore troppo presto).
    private static final CondizioneAvanzamento CONDIZIONE_LUOGOTENENTE =
            p -> p.getGiocatore().getStatistica(TipoStatistica.RISPETTO) >= 70
              && p.flagUguale("missione_primo_incontro_completata", "true");

    // Per BOSS servono Rispetto molto alto, Lealtà molto alta E aver completato l'ultima
    // missione (la resa dei conti con i Ferrandino, il vero traguardo della storia).
    private static final CondizioneAvanzamento CONDIZIONE_BOSS =
            p -> p.getGiocatore().getStatistica(TipoStatistica.RISPETTO) >= 95
              && p.getGiocatore().getStatistica(TipoStatistica.LEALTA) >= 90
              && p.flagUguale("missione_resa_dei_conti_completata", "true");

    // Ordine narrativo fisso: una missione è disponibile solo se la precedente è completata.
    private static final List<String> ORDINE_MISSIONI = List.of(
            "missione_primo_incontro",
            "missione_guardia_corrotta",
            "missione_traditore",
            "missione_resa_dei_conti"
    );

    private final List<Missione> missioni;
    private final List<Luogo> luoghi;
    private Partita partita;

    public GiocoServiceImpl(CaricatoreContenuti caricatore) {
        this.missioni = caricatore.caricaMissioni();
        this.luoghi = caricatore.caricaLuoghi();
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
    public ScenaDTO getScenaDTO() {
        Scena scenaCorrente = getScenaCorrente();
        List<SceltaDTO> scelteDTO = new java.util.ArrayList<>();
        for (int i = 0; i < scenaCorrente.getScelte().size(); i++) {
            scelteDTO.add(new SceltaDTO(scenaCorrente.getScelte().get(i).getTesto(), i));
        }
        return new ScenaDTO(scenaCorrente.getTesto(), scelteDTO);
    }

    @Override
    public void applicaScelta(int indiceScelta) {
        Scena scenaCorrente = getScenaCorrente();
        Scelta scelta = scenaCorrente.getScelte().get(indiceScelta);

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

    private Scena getScenaCorrente() {
        Missione missione = trovaMissione(partita.getIdMissioneCorrente());
        return trovaScena(missione, partita.getIdScenaCorrente());
    }

    @Override
    public boolean missioneTerminata() {
        return partita != null && partita.getIdScenaCorrente() == null;
    }

    @Override
    public Partita getPartita() {
        return partita;
    }

    @Override
    public void impostaPartita(Partita partita) {
        this.partita = partita;
    }

    @Override
    public StatisticheDTO getStatisticheDTO() {
        Giocatore g = partita.getGiocatore();
        return new StatisticheDTO(
                g.getNome(),
                g.getStatistica(TipoStatistica.RISPETTO),
                g.getStatistica(TipoStatistica.RICCHEZZA),
                g.getStatistica(TipoStatistica.ASTUZIA),
                g.getStatistica(TipoStatistica.LEALTA),
                g.getRango().name()
        );
    }

    @Override
    public List<LuogoDTO> getLuoghiDTO() {
        return luoghi.stream()
                .map(l -> new LuogoDTO(l.getId(), l.getNome(), l.getDescrizione(),
                        l.haMissione(), l.getIdMissione()))
                .toList();
    }

    @Override
    public boolean missioneCompletata(String idMissione) {
        return partita.flagUguale(idMissione + "_completata", "true");
    }

    @Override
    public boolean missioneDisponibile(String idMissione) {
        int indice = ORDINE_MISSIONI.indexOf(idMissione);
        if (indice <= 0) {
            return true;
        }
        return missioneCompletata(ORDINE_MISSIONI.get(indice - 1));
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
