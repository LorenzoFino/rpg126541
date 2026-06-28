package it.unicam.cs.mpgc.rpg126541.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg126541.model.Luogo;
import it.unicam.cs.mpgc.rpg126541.model.Missione;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Implementazione di {@link CaricatoreContenuti} che legge i dati di gioco
 * da file JSON inclusi nelle risorse dell'applicazione.
 */
public class CaricatoreContenutiJson implements CaricatoreContenuti {

    private static final String PERCORSO_LUOGHI =
            "/it/unicam/cs/mpgc/rpg126541/dati/luoghi.json";
    private static final String PERCORSO_MISSIONI =
            "/it/unicam/cs/mpgc/rpg126541/dati/missioni.json";

    private final Gson gson = new Gson();

    @Override
    public List<Luogo> caricaLuoghi() {
        Type tipo = new TypeToken<List<Luogo>>() {}.getType();
        return leggiJson(PERCORSO_LUOGHI, tipo);
    }

    @Override
    public List<Missione> caricaMissioni() {
        Type tipo = new TypeToken<List<Missione>>() {}.getType();
        return leggiJson(PERCORSO_MISSIONI, tipo);
    }

    private <T> List<T> leggiJson(String percorso, Type tipo) {
        InputStream stream = getClass().getResourceAsStream(percorso);
        if (stream == null) {
            System.err.println("[Contenuti] File non trovato nelle risorse: " + percorso);
            return Collections.emptyList();
        }
        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            List<T> risultato = gson.fromJson(reader, tipo);
            return risultato != null ? risultato : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("[Contenuti] Errore nella lettura di " + percorso + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
