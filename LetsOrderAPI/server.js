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




    // Funzione principale: permette di estrapolare i dati dall'API Places 
    // in base al tipo di ricerca e di inserire i dati in una struttura di tipo json
    function richiesta(google_key) {

        if (req.query.tipo == "luogo" && req.query.lista != undefined) {
            
            var data_store = {};
            data_store["lista"] = [];

            var uri = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=ristoranti+a+" +
                      req.query.lista +
                      "&key=" +
                      google_key;

            var options = {
                uri: uri,
                json: true
            };


            request(options, function(error, response, body) {

                var status = body["status"];

                if (status != "ZERO_RESULTS") {

                    for (var i = 0; i < body["results"].length; i++) {

                        var id = body["results"][i]["place_id"];
                        var nome = body["results"][i]["name"];
                        var indirizzo = body["results"][i]["formatted_address"];
                        var valutazione = body["results"][i]["rating"];
                        var posizione = body["results"][i]["geometry"]["location"]["lat"] +
                                        "," +
                                        body["results"][i]["geometry"]["location"]["lng"];
                        var nposti_liberi = body["results"][i]["number"]


                        // Creazione di un array all'i esimo elemento

                        data_store["lista"][i] = {};


                        // Try/Catch per i ristoranti i quali l'orario di apertura non è specificato
            
                        try {

                                var apertura = body["results"][i]["opening_hours"]["open_now"]
                                               .toString()
                                               .replace("true", "Aperto")
                                               .replace("false", "Chiuso");

                        } 
                        catch (Exception) {

                                var apertura = null;

                        }


                        // Se il ristorante è chiuso i posti disponibili saranno 0
                        if(apertura == "Aperto"){

                            posti_liberi = rando(0, 50);

                        }
                        else{

                            posti_liberi = 0;

                        }

                        

                        data_store["lista"][i]["id"] = id;
                        data_store["lista"][i]["nome"] = nome;
                        data_store["lista"][i]["indirizzo"] = indirizzo;
                        data_store["lista"][i]["posizione"] = posizione;
                        data_store["lista"][i]["posti liberi"] = posti_liberi;
                        data_store["lista"][i]["apertura"] = apertura;
                        data_store["lista"][i]["valutazione"] = valutazione;

                    }

                    if (data_store["lista"] == ""){

                        res.status(404).send("Not found");

                    } 
                    else{

                        res.send(data_store);

                    } 

                }
                else{ // Se la richiesta non è stata specificata correttamente o se non sono presenti risultati

                    res.status(404).send("Not found");

                }

            });

        }


        if (req.query.tipo == "id" && req.query.lista != undefined) {

            diretto(req.query.lista);

        }


        if (req.query.tipo == "diretto" && req.query.lista != undefined) {

            var data_store = {};
            data_store["lista"] = [];

            var uri = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" +
                      req.query.lista +
                      "&key=" +
                      google_key;

            var options = {
                uri: uri,
                json: true
            };

            request(options, function(error, response, body) {

                var id = body["results"][0]["place_id"];
                diretto(id);

            });

        }

    }




    // Funzione che permette di gestire la ricerca mediante tipo diretto
    function diretto(luogo) {

        var data_store = {};
        data_store["lista"] = [];

        var uri = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" +
                  luogo +
                  "&language=it&key=" +
                  google_key;
        
        var options = {
            uri: uri,
            json: true
        };

        request(options, function(error, response, body) {

            var nome = body["result"]["name"];
            var indirizzo = body["result"]["formatted_address"];
            var valutazione = body["result"]["rating"];
            var posizione = body["result"]["geometry"]["location"]["lat"] +
                            "," +
                            body["result"]["geometry"]["location"]["lng"];
            var posti_liberi = body["result"]["number"];
            var numero = body["result"]["international_phone_number"];
            var sitoweb = body["result"]["website"];
            var reviews = body["result"]["reviews"];
            var photo = body["result"]["photos"][0]["photo_reference"];


            // Creazione di un array all'i esimo elemento

            data_store["lista"][0] = {};


            // Try/Catch per i ristoranti i quali l'orario di apertura non è specificato
      
            try {

                    var apertura = body["result"]["opening_hours"]["open_now"]
                                   .toString()
                                   .replace("true", "Aperto")
                                   .replace("false", "Chiuso");

            } 
            catch (Exception) {

                     var apertura = null;
            }


            
            // Se il ristorante è chiuso i posti disponibili saranno 0
            if(apertura == "Aperto"){

                posti_liberi = rando(0, 50);

            }
            else{

                posti_liberi = 0;

            }


            data_store["lista"][0]["nome"] = nome;
            data_store["lista"][0]["indirizzo"] = indirizzo;
            data_store["lista"][0]["posizione"] = posizione;
            data_store["lista"][0]["posti liberi"] = posti_liberi;
            data_store["lista"][0]["apertura"] = apertura;
            data_store["lista"][0]["valutazione"] = valutazione;
            data_store["lista"][0]["photo"] = photo;

            
            // Sequenza di controlli sui valori e impostazione dei relativi valori di default

            if (numero == undefined){

                data_store["lista"][0]["numtell"] = null;

            } 
            else{

                data_store["lista"][0]["numtell"] = numero;

            } 

            if (sitoweb == undefined){

                data_store["lista"][0]["sitoweb"] = null;

            } 
            else{

                data_store["lista"][0]["sitoweb"] = sitoweb;

            }

            data_store["orari"] = [];

            try {

                    var weekday_text = body["result"]["opening_hours"]["weekday_text"];
                    if (apertura != "Aperto"){

                        data_store["orari"][0] = weekday_text;

                    } 
                    else{

                        data_store["orari"][0] = null;

                    } 
            } 
            catch (Exception) {}

            if (reviews == undefined){

                data_store["feedback"] = null;

            } 
            else{

                data_store["feedback"] = reviews;

            } 

            if (data_store["lista"] == ""){ // Se la richiesta non è stata specificata correttamente o se non sono presenti risultati

                res.status(404).send("Not found");

            } 
            else{

                res.send(data_store);
                
            } 

        });

    }

});