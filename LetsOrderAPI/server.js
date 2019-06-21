// API sviluppata da: Andrea De Luna
// Università degli Studi di Urbino Carlo Bo
// Progetto per la sessione estiva di PDGT


// Librerie
var express = require("express");
var app = express();
var request = require("request");
var rando = require("random-number-in-range");

// Porta
var listener = app.listen(process.env.PORT || 3000, function() {});


// Creazione della struttura del file json
app.get("/", function(req, res) {

    var json = [];

    // Se non viene specificato il tipo di ricerca
    if (req.query.tipo == undefined) { // Stampa messaggio di errore

        var warning = {

            STATUS: "1",
            CODE: res.statusCode,
            MESSAGE: "Per informazioni o aiuto vai qui -> https://github.com/andreadeluna/ProgettoPDGT",
            DATA: [],
            TOTALS: []

        };

        res.json(warning);

    }
    else{ // Generazione delle chiavi per usufruire dell'API Places di Google

        var google_key1 = ["TOKEN", "TOKEN"];

        var random = rando(1, google_key1.length);
        var google_key = google_key1[random];

        richiesta(google_key);

    }












});