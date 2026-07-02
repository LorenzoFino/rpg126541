package it.unicam.cs.mpgc.rpg126541.persistence;

import it.unicam.cs.mpgc.rpg126541.model.Giocatore;
import it.unicam.cs.mpgc.rpg126541.model.Partita;
import it.unicam.cs.mpgc.rpg126541.model.TipoStatistica;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per RepositoryPartitaJson: salvataggio e ricaricamento da file.
 */
class RepositoryPartitaJsonTest {

    // Concetto: dopo salva() e carica(), la partita ricaricata deve avere
    // lo stesso nome del giocatore e le stesse statistiche di quella originale.
    @Test
    void salvaECaricaMantengonoNomeEStatistiche(@TempDir Path cartellaTemporanea) {
        RepositoryPartitaJson repository = new RepositoryPartitaJson(cartellaTemporanea.toString());
        Giocatore giocatore = new Giocatore("id-1", "Salvatore");
        giocatore.modificaStatistica(TipoStatistica.RISPETTO, 20);
        Partita originale = new Partita(giocatore);

        repository.salva(originale, "slot_test");
        Partita ricaricata = repository.carica("slot_test");

        assertEquals(originale.getGiocatore().getNome(), ricaricata.getGiocatore().getNome());
        assertEquals(
                originale.getGiocatore().getStatistica(TipoStatistica.RISPETTO),
                ricaricata.getGiocatore().getStatistica(TipoStatistica.RISPETTO)
        );
    }
}
