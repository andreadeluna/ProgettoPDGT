<a><img src='https://raw.githubusercontent.com/andreadeluna/ProgettoPDGT/master/img/icona.png' alt='icon' height='100'/></a>

# Let's Order!
Semplice API che si interfaccia con l'API Places di Google ed effettua il parsing dei dati ricevuti. Inserisce i dati in una struttura di tipo JSON e permette così la visualizzazione e la consultazione da parte degli utenti.

# Licenza
Con licenza GPLv3: http://www.gnu.org/licenses/gpl-3.0.html

# Descrizione
Per quanto riguarda l'Array di elementi contenenti i ristoranti, esso presenterà due varianti a seconda di come viene effettuata la ricerca. Il ristorante verrà autenticato e identificato mediante un codice ID univoco, generato all'atto di creazione dell'Array durante il parsing dei dati. Quindi, nel caso in cui si voglia effettuare un'autenticazione lato server per accedere ai dati e alle ordinazioni di ogni ristorante, sarà sufficiente fruire di tale ID identicativo.
Nel caso in cui l'utente richieda la connessione diretta al ristorante, l'array conterrà anche il menù dello stesso, in modo da permetterne l'acquisizione da parte dell'Applicazione, per permettere così di effettuare l'ordinazione e la prenotazione.

# Metodi
- Ricerca tramite luogo:

    Cercando il ristorante tramite luogo, sarà possibile all'utente visualizzare una lista di ristoranti presenti nel luogo da lui inserito, visionando così i dati di base quali id, nome, indirizzo, apertura, posti liberi e valutazione.

<div align="center"><a><img src='https://raw.githubusercontent.com/andreadeluna/ProgettoPDGT/master/img/array_luogo.png' height='400' alt='icon'/></a></div>

- Ricerca tramite nome del ristorante

    Cercando il ristorante tramite il nome, sarà possibile all'utente visualizzare, oltre alle informazioni di base, anche gli orari di apertura, le recensioni di Google dei clienti, il sito web, il numero di telefono e il menù.

    * Dati ristorante

    <div align="center"><a><img src='https://raw.githubusercontent.com/andreadeluna/ProgettoPDGT/master/img/dati_ristorante.png' height='200' alt='icon'/></a></div>


    * Menù

    <div align="center"><a><img src='https://raw.githubusercontent.com/andreadeluna/ProgettoPDGT/master/img/menu_ristorante.png' height='200' alt='icon'/></a></div>


    * Orari

    <div align="center"><a><img src='https://raw.githubusercontent.com/andreadeluna/ProgettoPDGT/master/img/orari_ristorante.png' height='200' alt='icon'/></a></div>


    * Feedback

    <div align="center"><a><img src='https://raw.githubusercontent.com/andreadeluna/ProgettoPDGT/master/img/recensioni_ristorante.png' height='200' alt='icon'/></a></div>

