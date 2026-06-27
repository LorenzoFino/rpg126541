# Requisiti — Legge del Silenzio

> Progetto d'esame — Metodologie di Programmazione, A.A. 2025/26.
> Studente: Lorenzo Fino — Matricola: 126541.

I requisiti sono scritti in forma verificabile: ogni requisito descrive un comportamento osservabile e misurabile, non un'intenzione generica.

---

## 1. Requisiti generali di sistema

**R01** — Il sistema si avvia con il comando `./gradlew run` senza alcuna configurazione manuale aggiuntiva (niente variabili d'ambiente, niente SDK da installare a parte un JDK 25).

**R02** — Il progetto compila correttamente con il comando `./gradlew build` su qualsiasi macchina che abbia un JDK 25.

**R03** — Tutte le classi Java del progetto si trovano nel package `it.unicam.cs.mpgc.rpg126541` o in un suo sotto-package. Nessuna classe esiste fuori da questo package.

**R04** — La logica di gioco (package `model` e `service`) non contiene nessun import da `javafx.*`. Il codice JavaFX è confinato ai controller e alla classe `Main`.

---

## 2. Schermata: Menu principale

**R05** — All'avvio, il sistema mostra una schermata di menu principale con esattamente tre azioni: "Nuova partita", "Carica partita" ed "Esci".

**R06** — Premendo "Nuova partita", il sistema chiede al giocatore di inserire il nome del protagonista (campo di testo); il nome non può essere vuoto. Se il campo è vuoto, il bottone di conferma è disabilitato oppure appare un messaggio di errore visibile.

**R07** — Premendo "Carica partita", il sistema mostra la lista dei salvataggi disponibili (slot con nome partita e data di salvataggio). Se non esiste alcun salvataggio, appare un messaggio "Nessuna partita salvata".

**R08** — Premendo "Esci", l'applicazione si chiude.

---

## 3. Personaggio e statistiche

**R09** — Il giocatore ha esattamente quattro statistiche numeriche intere: **Rispetto**, **Ricchezza**, **Astuzia**, **Lealtà**. Ogni statistica parte da 50 all'inizio di una nuova partita.

**R10** — Il valore di ciascuna statistica è sempre compreso tra 0 e 100 (inclusi). Se un effetto porterebbe una statistica sotto 0, essa si ferma a 0; se porterebbe sopra 100, si ferma a 100.

**R11** — Il giocatore ha un rango che può essere uno di tre valori: `ASPIRANTE`, `LUOGOTENENTE`, `BOSS`. Il rango iniziale è `ASPIRANTE`.

**R12** — Il rango avanza automaticamente quando il giocatore raggiunge le seguenti soglie di Rispetto: da `ASPIRANTE` a `LUOGOTENENTE` con Rispetto ≥ 60; da `LUOGOTENENTE` a `BOSS` con Rispetto ≥ 85. Il rango non retrocede mai.

---

## 4. Schermata: Mappa di Palermo

**R13** — Dopo l'avvio di una partita, il sistema mostra una schermata "Mappa di Palermo" con almeno cinque luoghi cliccabili (es. Bar Ciangretta, Porto, Chiesa, Casa dei rivali, Bisca). Ogni luogo è un bottone o un'area cliccabile posizionata su uno sfondo.

**R14** — Ciascun luogo nella mappa mostra almeno il proprio nome. Al passaggio del mouse (hover), il luogo cambia aspetto visivamente (colore, bordo o evidenziazione) per segnalare che è interagibile.

**R15** — Cliccando un luogo, il sistema avvia la missione associata a quel luogo se essa è disponibile e non ancora completata. Se la missione è già stata completata, il sistema mostra un messaggio ("Hai già completato questa missione") e non avvia la scena.

**R16** — Il pannello statistiche (Rispetto, Ricchezza, Astuzia, Lealtà e Rango) è visibile nella schermata mappa e si aggiorna ogni volta che una statistica cambia.

---

## 5. Missioni e scene

**R17** — Il gioco contiene almeno 4 missioni complete, ciascuna composta da almeno 2 scene.

**R18** — Ogni scena mostra: un testo narrativo (almeno 30 parole), il pannello delle statistiche aggiornato, e da 2 a 4 bottoni di scelta.

**R19** — Ogni scelta ha un testo descrittivo (es. "Accetta l'accordo") e applica effetti a una o più statistiche. Gli effetti sono valori interi (positivi o negativi) specificati nel file JSON della missione.

**R20** — Dopo aver scelto un'opzione, il sistema carica la scena successiva indicata da quella scelta. Se la scena successiva ha `id` uguale a `null` o a una stringa speciale (es. `"fine"`), la missione è considerata conclusa e il sistema torna alla mappa.

**R21** — Il contenuto di missioni, scene e scelte è caricato interamente da file JSON presenti in `src/main/resources`. Non è codificato direttamente nel codice Java. Aggiungere una nuova missione richiede solo di aggiungere un file JSON, senza modificare codice.

---

## 6. Persistenza

**R22** — Il sistema offre la funzione "Salva partita" accessibile dalla schermata mappa. Premendo il bottone, lo stato corrente della partita (statistiche, rango, missioni completate) viene salvato in un file `.json` nella cartella `saves/` nella directory di lavoro dell'applicazione.

**R23** — Il sistema permette di salvare fino a 3 slot distinti (es. `salvataggio_1.json`, `salvataggio_2.json`, `salvataggio_3.json`). Il giocatore sceglie quale slot usare.

**R24** — Caricando una partita salvata, il sistema ripristina esattamente lo stato al momento del salvataggio: stesso nome, stesse statistiche, stesso rango, stesse missioni marcate come completate.

**R25** — Se il file di salvataggio è assente o corrotto (JSON non valido), il sistema mostra un messaggio di errore leggibile ("Impossibile caricare il salvataggio: file non valido") senza mandare in crash l'applicazione.

---

## 7. Requisiti di architettura e qualità

**R26** — Ogni interfaccia del package `service` e `persistence` ha almeno una classe di implementazione concreta. I controller JavaFX dipendono dalle interfacce, non dalle implementazioni.

**R27** — La classe `Giocatore`, la classe `Missione` e la classe `Luogo` ridefiniscono `equals()` e `hashCode()` in base al loro identificativo logico (es. un campo `id`), in modo che due istanze con lo stesso id risultino uguali anche se create separatamente.

**R28** — Nessuna classe supera le 150 righe di codice (esclusi blank e commenti). Se una classe tende a crescere oltre questo limite, va divisa.

**R29** — I nomi di classi, metodi e variabili sono in italiano e descrivono chiaramente il loro scopo (es. `applicaScelta`, `caricaMissione`, `statistiche`). Sono vietati nomi come `tmp`, `data2`, `obj`, `x`.

---

## 8. Requisiti di interfaccia grafica

**R30** — L'intera grafica usa una palette coerente: sfondi neri/antracite (`#0d0d0d`–`#1a1a1a`), accenti oro/ambra (`#c8a24b`), testo bianco sporco (`#e8e4d8`). Tutti i colori e i font sono definiti nel file `stile.css`; nessun colore o font è hardcoded nei controller Java.

**R31** — Il cambio di schermata (es. da mappa a scena) è accompagnato da un `FadeTransition` con durata di 300-500 ms.

**R32** — L'applicazione è utilizzabile a una risoluzione di almeno 1280×720 pixel senza che i testi vengano troncati o i bottoni sovrapposti.

---

*Fine documento — versione 1.0, 27 giugno 2026.*
