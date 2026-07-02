package it.unicam.cs.mpgc.rpg126541.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per Giocatore: identità basata sull'id e vincoli sulle statistiche.
 */
class GiocatoreTest {

    // Concetto: due Giocatore sono "uguali" se e solo se hanno lo stesso id,
    // indipendentemente dal nome. equals e hashCode devono essere coerenti tra loro.
    @Test
    void giocatoriConStessoIdSonoUguali_conIdDiversoNo() {
        Giocatore g1 = new Giocatore("id-1", "Salvatore");
        Giocatore g2 = new Giocatore("id-1", "Un altro nome");
        Giocatore g3 = new Giocatore("id-2", "Salvatore");

        assertTrue(g1.equals(g2) && g1.hashCode() == g2.hashCode());
        assertNotEquals(g1, g3);
    }

    // Concetto: modificaStatistica applica il delta ma tiene il valore sempre
    // nell'intervallo [0, 100], troncando invece di andare oltre i limiti.
    @Test
    void modificaStatisticaAumentaEBloccaAiLimiti() {
        Giocatore giocatore = new Giocatore("id-1", "Salvatore");

        giocatore.modificaStatistica(TipoStatistica.RISPETTO, 10);
        assertEquals(60, giocatore.getStatistica(TipoStatistica.RISPETTO));

        giocatore.modificaStatistica(TipoStatistica.RISPETTO, -1000);
        assertEquals(0, giocatore.getStatistica(TipoStatistica.RISPETTO));

        giocatore.modificaStatistica(TipoStatistica.RISPETTO, 1000);
        assertEquals(100, giocatore.getStatistica(TipoStatistica.RISPETTO));
    }
}
