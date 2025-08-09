# 🔓 XVanish — Ultimate Vanish Plugin

**XVanish** è un plugin leggero, altamente configurabile e potente per server Minecraft (Spigot / Paper) — pensato per lo staff che deve monitorare i giocatori in modo discreto con un'interfaccia semplice e messaggi personalizzabili.

---

## 🌟 Caratteristiche principali

### 🎮 Vanish System
- **Toggle Vanish:** attiva/disattiva la modalità vanish con un singolo comando.  
- **Effetti:** invisibilità, night vision, speed boost e volo migliorato.  
- **Nascondi in Tablist:** possibilità di nascondere i giocatori vanished dalla lista tab (configurabile).  
- **Notifiche staff:** avvisa lo staff quando un giocatore entra/esce da vanish.

### 🔒 Controllo Interazioni
- **Blocca interazioni:** impedisce break/place dei blocchi mentre si è vanished.  
- **Restrizioni sugli oggetti:** blocca drop/pickup degli item.  
- **Protezione entità:** previene danni da/a giocatori vanished.  
- **Mob ignore:** i mob ignorano i giocatori vanished (se abilitato).

### ⚡ Ottimizzazioni delle prestazioni
- **Design leggero:** impatto minimo sul server.  
- **Task ottimizzati:** scheduling efficiente per aggiornamenti real-time.  
- **Strutture dati veloci:** utilizzo di `HashSet` e `HashMap` per lookup rapidi.

### 🎨 Personalizzazione
- **Messaggi configurabili:** supporto per color codes e placeholder.  
- **Sistema permessi:** nodi granulari per controllo accessi.  
- **Eventi configurabili:** abilita/disabilita restrizioni da `config.yml`.

---

## 📋 Requisiti

**Minecraft**
- Server: **Spigot** o **Paper**  
- Versioni supportate: **1.13.x — 1.21.x**  
- Dipendenze: *Nessuna* (plugin standalone)

**Sviluppo**
- Java: **8+**  
- Build tool: **Maven**

**File opzionali**
- `config.yml` — generato automaticamente al primo avvio

---

## 🚀 Installazione

### Per proprietari di server
1. Scarica `XVanish.jar` dalla pagina Releases o da SpigotMC.  
2. Copia `XVanish.jar` nella cartella `plugins/` del server.  
3. Riavvia il server per generare `plugins/XVanish/config.yml`.  
4. Personalizza `config.yml` a piacere.  
5. Ricarica (opzionale): `/plugman reload XVanish` oppure riavvia il server.

### Per sviluppatori
```bash
# Clona il repository
git clone https://github.com/EventExecutor/xVanish.git

# Entra nella cartella
cd XVanish

# Build con Maven
mvn clean package

# Il jar compilato sarà in target/XVanish.jar
````

---

## 📁 Struttura del progetto (esempio)

```
XVanish/
├── src/main/java/it/nash/xvanish/
│   ├── commands/        # Classi comandi
│   ├── config/          # Gestione config
│   ├── listeners/       # Listener eventi
│   ├── managers/        # Manager core
│   ├── tasks/           # Task schedulati
│   └── utils/           # Utility
├── src/main/resources/
│   ├── config.yml       # Config di default
│   └── plugin.yml       # Metadata plugin
├── pom.xml
├── README.md
└── LICENSE
```

---

## 🎯 Uso

### Avvio

* Metti `XVanish.jar` nella cartella `plugins/` e avvia il server.
* Il plugin genera `config.yml` automaticamente.

### Comandi

```
/vanish  (o /v)        # Toggle vanish
/vanish list           # Lista giocatori in vanish
```

**Permessi**

```
xvanish.use   # Permette di togglare vanish
xvanish.see   # Vede i vanished e riceve notifiche
xvanish.list  # Vede la lista dei vanished
```

---

## ⚙️ Configurazione (esempio `config.yml`)

```yaml
messages:
  vanish_enabled: "&8(&b&lXVANISH&8) &bVanish &e-> &cAbilitata&f!"
  vanish_disabled: "&8(&b&lXVANISH&8) &bVanish &e-> &cDisabilitata&f!"
  no_permission: "&8(&b&lXVANISH&8) &cNon hai il permesso per usare questo comando!"
  not_player: "&8(&b&lXVANISH&8) &cSolo i giocatori possono usare questo comando!"
  vanish_list_header: "&8(&b&lXVANISH&8) &bPlayer in vanish &7({count})&7: "
  vanish_list_none: "&8(&b&lXVANISH&8) &cNessun player è in vanish"
  staff_notify_enter: "&8(&b&lXVANISH&8) &e{player} &7è entrato in vanish"
  staff_notify_exit: "&8(&b&lXVANISH&8) &e{player} &7è uscito dalla vanish"
  vanish_effects_enabled: "&8(&b&lXVANISH&8) &bEffetti abilitati!"
  plugin_enabled: "&8(&b&lXVANISH&8) &aPlugin caricato con successo!"
  plugin_disabled: "&8(&b&lXVANISH&8) &cPlugin disabilitato"
  staff_vanished_count: "&8(&b&lXVANISH&8) &bCi sono &e{count} &7staff in vanish"

permissions:
  vanish_use: "xvanish.use"
  vanish_see: "xvanish.see"
  vanish_list: "xvanish.list"

events:
  block_break: true
  block_place: true
  item_drop: true
  item_pickup: true
  entity_damage: true
  player_interact: true
  inventory_click: true
  entity_target: true
  toggle_flight: true
  hide_from_tablist: true
```

**Opzioni importanti**

* Placeholder support: `{player}`, `{count}`
* Color codes: usa `&` seguito da codice colore (es. `&c`, `&b`, etc.)
* Disabilita eventi non necessari per risparmiare risorse

---

## 📊 Esempi di output

**Vanish abilitato**

```
&8(&b&lXVANISH&8) &bVanish &e-> &cAbilitata&f!
&8(&b&lXVANISH&8) &bEffetti abilitati!
```

**Vanish disabilitato**

```
&8(&b&lXVANISH&8) &bVanish &e-> &cDisabilitata&f!
```

**Lista vanish**

```
&8(&b&lXVANISH&8) &bPlayer in vanish &7(2)&7: &ePlayer1, &ePlayer2
```

---

## 🔧 Risoluzione problemi

**Plugin non si carica**

```
[Server] Plugin XVanish failed to load
```

* Verifica Java 8+ installato.
* Assicurati di usare Spigot/Paper 1.13.x — 1.21.x.
* Controlla `server.log` per errori di startup (stacktrace).

**Config non generato**

* Elimina la cartella `plugins/XVanish` e riavvia il server per forzare la rigenerazione.

**Problemi di prestazioni**

* Disabilita eventi non necessari in `config.yml`.
* Usa Paper per migliori ottimizzazioni.
* Verifica conflitti con altri plugin (es. listener che cancellano eventi globalmente).

---

## ⚠️ Avvertenze legali & uso responsabile

**Uso consentito**

* Monitoraggio dello staff su server di proprietà o con autorizzazione.

**Uso vietato**

* Uso non autorizzato su server altrui o per attività illegali.

*I dev non sono responsabili per usi illeciti o non autorizzati del plugin.*

---

## 🤝 Contribuire

1. Fork del repo.
2. Crea un branch feature: `git checkout -b feature/tuo-feature`.
3. Commit: `git commit -m "Add your feature"`.
4. Push e apri una PR.

**Idee per contribuire**

* Supporto ad ulteriori effetti.
* Maggior personalizzazione della tablist.
* Localizzazioni / lingue aggiuntive.

---

## 📝 Changelog

**v1.0.0 (current)**

* Toggle vanish con effetti (invisibilità, night vision, speed).
* Nascondi in tablist per giocatori non staff.
* Eventi e permessi configurabili.
* Notifiche staff e lista vanish.

---

## 🛡️ Dettagli prestazioni

* **Uso memoria:** basso (≈ 10–50 MB)
* **CPU:** basso (task ottimizzati)
* **Frequenza task:** aggiornamenti ogni 20 tick (1 secondo) — configurabile nel codice

**Suggerimenti**

* Preferire Paper per server ad alto carico.
* Limitare il numero di vanished su macchine con risorse ridotte.

---

## 📞 Supporto

* Segnala bug su **GitHub Issues**.
* Contatti Discord: `ObsidianWorks` (coming soon), `blacked10469`, `ashcodee_`
  *(Aggiorna questi riferimenti con i contatti ufficiali del tuo progetto.)*

---

## 📄 Licenza

Questo progetto è rilasciato sotto **Apache License 2.0**. Vedi il file `LICENSE` per i dettagli.

---

## ⚡ Creato con ❤️ per la community Minecraft da **EventExecutor & BinaryCodee**

---
