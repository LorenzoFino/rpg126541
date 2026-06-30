package it.unicam.cs.mpgc.rpg126541.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.unicam.cs.mpgc.rpg126541.model.Partita;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione di RepositoryPartita che salva le partite come file JSON
 * nella cartella "saves/" usando Gson e BufferedWriter/Reader.
 */
public class RepositoryPartitaJson implements RepositoryPartita {

    private static final String CARTELLA_SALVATAGGI = "saves";
    private static final String ESTENSIONE = ".json";

    private final Gson gson;

    public RepositoryPartitaJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void salva(Partita partita, String nomeSlot) {
        File cartella = new File(CARTELLA_SALVATAGGI);
        if (!cartella.exists()) {
            cartella.mkdirs();
        }
        File file = new File(cartella, nomeSlot + ESTENSIONE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(partita, writer);
            partita.setNomeSlotCorrente(nomeSlot);
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio di '" + nomeSlot + "': " + e.getMessage());
        }
    }

    @Override
    public Partita carica(String nomeSlot) {
        File file = new File(CARTELLA_SALVATAGGI, nomeSlot + ESTENSIONE);
        if (!file.exists()) {
            System.err.println("Salvataggio '" + nomeSlot + "' non trovato.");
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Partita partita = gson.fromJson(reader, Partita.class);
            if (partita != null) {
                partita.setNomeSlotCorrente(nomeSlot);
            }
            return partita;
        } catch (IOException e) {
            System.err.println("Errore nel caricamento di '" + nomeSlot + "': " + e.getMessage());
            return null;
        } catch (JsonSyntaxException e) {
            System.err.println("File di salvataggio '" + nomeSlot + "' corrotto o non valido: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> salvataggiDisponibili() {
        File cartella = new File(CARTELLA_SALVATAGGI);
        List<String> nomi = new ArrayList<>();
        if (!cartella.exists() || !cartella.isDirectory()) {
            return nomi;
        }
        File[] files = cartella.listFiles((dir, nome) -> nome.endsWith(ESTENSIONE));
        if (files != null) {
            for (File f : files) {
                nomi.add(f.getName().replace(ESTENSIONE, ""));
            }
        }
        return nomi;
    }
}
