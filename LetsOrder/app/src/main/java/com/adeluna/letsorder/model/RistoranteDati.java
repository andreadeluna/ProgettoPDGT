package com.adeluna.letsorder.model;

public class RistoranteDati {

    private String nome;
    private String indirizzo;
    private String posti_liberi;
    private String apertura;
    private String valutazione;
    private String numtel;
    private String sitoweb;



    public RistoranteDati(String nome,
                          String indirizzo,
                          String posti_liberi,
                          String apertura,
                          String valutazione,
                          String numtel,
                          String sitoweb){

        this.nome = nome;
        this.indirizzo = indirizzo;
        this.posti_liberi = posti_liberi;
        this.apertura = apertura;
        this.valutazione = valutazione;
        this.numtel = numtel;
        this.sitoweb = sitoweb;


    }


    public RistoranteDati(){

    }

    public String getNomeRist() {
        return nome;
    }

    public void setNomeRist(String nome) {
        this.nome = nome;
    }

    public String getIndirizzoRist() {
        return indirizzo;
    }

    public void setIndirizzoRist(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getPostiRist() {
        return posti_liberi;
    }

    public void setPostiRist(String posti_liberi) {
        this.posti_liberi = posti_liberi;
    }

    public String getAperturaRist() {
        return apertura;
    }

    public void setAperturaRist(String apertura) {
        this.apertura = apertura;
    }

    public String getValutazioneRist() {
        return valutazione;
    }

    public void setValutazioneRist(String valutazione) {
        this.valutazione = valutazione;
    }

    public String getNumRist() {
        return numtel;
    }

    public void setNumRist(String numtel) {
        this.numtel = numtel;
    }

    public String getSitoRist() {
        return sitoweb;
    }

    public void setSitoRist(String sitoweb) {
        this.sitoweb = sitoweb;
    }



}

