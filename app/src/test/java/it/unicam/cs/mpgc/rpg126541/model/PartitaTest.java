package it.unicam.cs.mpgc.rpg126541.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per Partita: gestione dei flag di gioco.
 */
class PartitaTest {

    // Concetto: setFlag registra una coppia chiave/valore che getFlag può poi
    // rileggere, e flagUguale confronta il valore memorizzato con quello atteso.
    @Test
    void setFlagPermetteDiLeggerloEConfrontarlo() {
        Partita partita = new Partita(new Giocatore("id-1", "Salvatore"));

        partita.setFlag("missione_primo_incontro_completata", "true");

        assertEquals("true", partita.getFlag("missione_primo_incontro_completata"));
        assertTrue(partita.flagUguale("missione_primo_incontro_completata", "true"));
        assertFalse(partita.flagUguale("missione_primo_incontro_completata", "false"));
    }
}
