package it.unicam.cs.mpgc.rpg126541.service;

import it.unicam.cs.mpgc.rpg126541.model.TipoStatistica;
import it.unicam.cs.mpgc.rpg126541.persistence.CaricatoreContenutiJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per GiocoServiceImpl: avanzamento della storia e applicazione delle scelte.
 * Usa il caricatore reale così le missioni sono quelle effettive del gioco.
 */
class GiocoServiceTest {

    private GiocoService servizio;

    @BeforeEach
    void creaPartitaEAvviaMissione() {
        servizio = new GiocoServiceImpl(new CaricatoreContenutiJson());
        servizio.nuovaPartita("Salvatore");
        servizio.avviaMissione("missione_primo_incontro");
    }

    // Concetto: applicare una scelta modifica le statistiche del giocatore,
    // che partono tutte dal valore iniziale di 50.
    @Test
    void applicaSceltaModificaAlmenoUnaStatistica() {
        servizio.applicaScelta(0);

        boolean statisticaCambiata = false;
        for (TipoStatistica tipo : TipoStatistica.values()) {
            if (servizio.getPartita().getGiocatore().getStatistica(tipo) != 50) {
                statisticaCambiata = true;
            }
        }
        assertTrue(statisticaCambiata);
    }

    // Concetto: dopo aver percorso tutte le scene della missione fino in fondo,
    // missioneTerminata() segnala che non ci sono più scene successive.
    @Test
    void missioneTerminataDopoTutteLeScelte() {
        while (!servizio.missioneTerminata()) {
            servizio.applicaScelta(0);
        }

        assertTrue(servizio.missioneTerminata());
    }
}
