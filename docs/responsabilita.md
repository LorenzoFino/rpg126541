# Responsabilità del sistema — Legge del Silenzio

> Derivato da `docs/requisiti.md` — versione 1.0, 27 giugno 2026.

Per ogni package si elencano le classi/interfacce, la loro responsabilità singola (SRP), i requisiti che soddisfano e i principali metodi o attributi attesi. Le dipendenze tra package sono esplicitate alla fine.

---

## Package `model` — rappresentare il dominio

Il model contiene solo POJO puri. **Non importa nulla di JavaFX, niente di Gson, niente di service.** Chiunque (un'app mobile, un test) può usarlo senza tirarsi dietro dipendenze grafiche.

---

### `TipoStatistica` (enum)
**Responsabilità:** nominare le quattro statistiche del gioco in modo tipizzato, evitando stringhe magiche sparse nel codice.

Valori: `RISPETTO`, `RICCHEZZA`, `ASTUZIA`, `LEALTA`.

Requisiti: R09.

---

### `Rango` (enum)
**Responsabilità:** rappresentare i tre livelli di rango del protagonista e conoscere la soglia di Rispetto necessaria per raggiungerli.

Valori: `ASPIRANTE`, `LUOGOTENENTE` (soglia 60), `BOSS` (soglia 85).

Attributo: `int sogliaRispetto` — ogni valore dell'enum conosce la propria soglia.

Requisiti: R11, R12.

> Perché la soglia sta qui e non nel service? Perché è un dato del dominio ("cosa significa essere LUOGOTENENTE"), non una regola di business arbitraria. Se domani la soglia cambia, si modifica l'enum, non la logica del service.

---

### `Giocatore`
**Responsabilità:** rappresentare il protagonista con il suo nome, le sue statistiche e il suo rango. Applicare gli effetti di una scelta alle statistiche rispettando i limiti 0–100.

Attributi: `String nome`, `Map<TipoStatistica, Integer> statistiche`, `Rango rango`.

Metodi chiave:
- `int getStatistica(TipoStatistica tipo)` — restituisce il valore corrente.
- `void applicaEffetto(TipoStatistica tipo, int delta)` — aggiunge delta, clampando a [0, 100].
- `equals` / `hashCode` basati sul `nome` (identità logica del personaggio).

Requisiti: R09, R10, R11, R27.

---

### `Luogo`
**Responsabilità:** rappresentare un punto della mappa (Bar Ciangretta, Porto, ecc.) con le informazioni necessarie per visualizzarlo e per sapere quale missione vi è associata.

Attributi: `String id`, `String nome`, `String descrizione`, `String idMissione`.

Metodi chiave: getter puri; `equals` / `hashCode` basati su `id`.

Requisiti: R13, R14, R15, R27.

---

### `Missione`
**Responsabilità:** rappresentare una missione come contenitore di scene ordinate, identificabile univocamente.

Attributi: `String id`, `String titolo`, `String idScenaIniziale`, `List<Scena> scene`.

Metodi chiave: `Scena trovaScena(String idScena)` — cerca la scena con quell'id tra quelle della missione; `equals` / `hashCode` basati su `id`.

Requisiti: R17, R20, R27.

---

### `Scena`
**Responsabilità:** rappresentare un singolo momento narrativo con il testo da mostrare e le scelte disponibili al giocatore.

Attributi: `String id`, `String testo`, `List<Scelta> scelte`.

Metodi chiave: getter puri.

Requisiti: R18, R20.

---

### `Scelta`
**Responsabilità:** rappresentare un'opzione che il giocatore può prendere in una scena, con i suoi effetti sulle statistiche e l'id della scena successiva.

Attributi: `String testo`, `Map<TipoStatistica, Integer> effetti`, `String idScenaSuccessiva`.

> `idScenaSuccessiva` uguale a `"fine"` (o `null`) segnala la fine della missione — è una convenzione di dominio, non una regola del service.

Metodi chiave: getter puri.

Requisiti: R19, R20.

---

### `Partita`
**Responsabilità:** contenere tutto lo stato salvabile di una sessione di gioco: chi gioca, cosa ha completato, da dove continua.

Attributi: `Giocatore giocatore`, `Set<String> missioniCompletate` (insieme degli id), `String dataUltimoSalvataggio`.

Metodi chiave: `boolean missioneCompletata(String idMissione)`, `void segnaCompletata(String idMissione)`.

Requisiti: R22, R24.

---

## Package `service` — applicare le regole del gioco

Il service orchestra la logica: prende decisioni, aggiorna il model, risponde a domande sullo stato. **Non sa nulla di JavaFX.** I controller lo usano tramite interfaccia (Dependency Inversion, R26).

---

### Interfaccia `GiocoService`
**Responsabilità:** definire il contratto della logica di gioco senza legarla a un'implementazione specifica.

Metodi:
- `void applicaScelta(Scelta scelta)` — applica gli effetti al giocatore e aggiorna il rango.
- `void segnazioneCompletata(String idMissione)` — marca la missione come terminata nella partita corrente.
- `boolean missioneDisponibile(String idMissione)` — `true` se non è già completata.
- `Partita getPartitaCorrente()` — espone lo stato corrente (usato dal controller per leggere statistiche da mostrare).
- `void nuovaPartita(String nomeGiocatore)` — crea una nuova partita con statistiche iniziali a 50 e rango ASPIRANTE.
- `void caricaPartita(Partita partita)` — imposta la partita corrente da un salvataggio.

Requisiti: R09, R10, R12, R15, R19, R20, R26.

---

### Classe `GiocoServiceImpl` (implementa `GiocoService`)
**Responsabilità:** implementare la logica: applicare effetti alle statistiche, aggiornare il rango automaticamente dopo ogni modifica, tenere traccia delle missioni completate.

Regola del rango (R12): dopo ogni `applicaScelta`, legge `Rispetto` corrente e controlla se il rango deve avanzare confrontando con `Rango.sogliaRispetto`. Il rango non retrocede mai.

Requisiti: R09, R10, R11, R12, R15, R19, R20, R26.

---

## Package `persistence` — salvare e caricare i dati

Due responsabilità distinte: la **partita** (stato del giocatore, dinamico, cambia a ogni sessione) e i **contenuti** (missioni e luoghi, statici, vengono solo letti).

---

### Interfaccia `RepositoryPartita`
**Responsabilità:** definire il contratto per salvare e caricare lo stato di una partita, senza dire nulla su come (file JSON, database, cloud...).

Metodi:
- `void salva(Partita partita, int slot)` — scrive la partita nel file del dato slot (1, 2 o 3).
- `Partita carica(int slot)` — legge e restituisce la partita dallo slot. Lancia un'eccezione controllata se il file è assente o corrotto.
- `List<String> slotDisponibili()` — restituisce le etichette degli slot che hanno già un salvataggio (usata per popolare la lista nel menu).

Requisiti: R22, R23, R24, R25, R26.

---

### Classe `RepositoryPartitaJson` (implementa `RepositoryPartita`)
**Responsabilità:** serializzare e deserializzare la `Partita` in JSON usando Gson, leggendo e scrivendo nella cartella `saves/` accanto all'eseguibile.

Gestisce il caso file mancante o JSON corrotto restituendo un messaggio d'errore leggibile (R25), senza far crashare l'app.

Requisiti: R22, R23, R24, R25.

---

### Interfaccia `CaricatoreContenuti`
**Responsabilità:** definire il contratto per ottenere la lista dei luoghi e delle missioni disponibili nel gioco.

Metodi:
- `List<Luogo> caricaLuoghi()` — restituisce i luoghi della mappa.
- `List<Missione> caricaMissioni()` — restituisce tutte le missioni con le loro scene e scelte.

Requisiti: R13, R17, R21, R26.

---

### Classe `CaricatoreContenutiJson` (implementa `CaricatoreContenuti`)
**Responsabilità:** leggere `luoghi.json` e `missioni.json` dalle risorse del progetto (`resources/.../dati/`) e deserializzarli in oggetti del model tramite Gson.

Se un file è mancante, lancia un'eccezione chiara (contenuto obbligatorio: senza missioni il gioco non può funzionare).

Requisiti: R13, R17, R21.

---

## Package `controller` — collegare la GUI alla logica

I controller JavaFX sono **snelli**: leggono eventi dalla GUI, delegano al service, aggiornano i componenti visivi. Non contengono logica di gioco. Dipendono dalle interfacce `GiocoService`, `RepositoryPartita` e `CaricatoreContenuti`, mai dalle implementazioni (R26).

---

### `MenuController`
**Responsabilità:** gestire la schermata iniziale: avviare una nuova partita, mostrare i salvataggi disponibili e caricarli, chiudere l'applicazione.

Comportamenti:
- Disabilita il bottone "Inizia" finché il campo nome è vuoto (R06).
- Chiede a `RepositoryPartita.slotDisponibili()` la lista degli slot e la mostra (R07).
- Alla conferma nuova partita, chiama `GiocoService.nuovaPartita(nome)` e naviga alla mappa (R05, R06).
- Alla selezione di uno slot, chiama `RepositoryPartita.carica(slot)` poi `GiocoService.caricaPartita(...)` e naviga alla mappa (R07, R24).
- Mostra messaggio d'errore se il caricamento fallisce (R25).

Requisiti: R05, R06, R07, R08.

---

### `MappaController`
**Responsabilità:** mostrare la mappa con i luoghi cliccabili, il pannello statistiche aggiornato e il bottone di salvataggio.

Comportamenti:
- Costruisce i bottoni dei luoghi leggendo `CaricatoreContenuti.caricaLuoghi()` (R13, R14).
- Al click su un luogo, chiede a `GiocoService.missioneDisponibile(id)`: se sì avvia la scena, altrimenti mostra il messaggio "Hai già completato questa missione" (R15).
- Aggiorna il pannello statistiche ogni volta che torna dalla schermata scena (R16).
- Al click su "Salva", chiede al giocatore quale slot usare e chiama `RepositoryPartita.salva(partita, slot)` (R22, R23).

Requisiti: R13, R14, R15, R16, R22, R23.

---

### `ScenaController`
**Responsabilità:** mostrare il testo narrativo della scena corrente, i bottoni delle scelte e aggiornare la UI in risposta al click del giocatore.

Comportamenti:
- Riceve la `Scena` corrente e la `Missione` dall'inizializzazione (passate da `MappaController`).
- Mostra il testo (R18) e genera da 2 a 4 bottoni dalle scelte della scena (R18, R19).
- Al click su una scelta: chiama `GiocoService.applicaScelta(scelta)`, poi `GiocoService.trovaScenaSuccessiva(...)`. Se l'id successivo è `"fine"`, chiama `GiocoService.segnazioneCompletata(idMissione)` e torna alla mappa. Altrimenti carica la scena successiva (R19, R20).
- Aggiorna il pannello statistiche dopo ogni scelta (R16).

Requisiti: R16, R18, R19, R20.

---

## Package `util` — servizi trasversali

Codice che non appartiene né al dominio né alla GUI ma serve a entrambi.

---

### `NavigatoreSchermate`
**Responsabilità:** centralizzare il cambio di schermata FXML con un `FadeTransition`, in modo che i controller non replichino questa logica ovunque (DRY).

Metodo: `void naviga(Stage stage, String fxmlPath)` — carica l'FXML, applica un `FadeTransition` di 400 ms e imposta la nuova scena sullo Stage.

Requisiti: R31.

> Un solo posto dove vivono le transizioni: se domani si vuole cambiare durata o tipo di transizione, si modifica un metodo solo.

---

## Riepilogo delle dipendenze tra package

```
controller  →  service (tramite interfaccia GiocoService)
controller  →  persistence (tramite interfacce RepositoryPartita, CaricatoreContenuti)
controller  →  util (NavigatoreSchermate)
controller  →  model (solo per leggere dati da mostrare nella GUI)

service     →  model (GiocoServiceImpl conosce Giocatore, Partita, Scelta, ecc.)

persistence →  model (serializza/deserializza Partita, Missione, Luogo, ecc.)

model       →  (nessuna dipendenza esterna)
util        →  (solo JavaFX per la transizione; niente di model o service)
```

**Regola fondamentale (R04, R26):** le frecce non risalgono mai verso JavaFX dai package `model` e `service`. Tutto ciò che ha `import javafx.*` vive solo in `controller`, `util` e `Main`.

---

*Fine documento — versione 1.0, 27 giugno 2026.*
