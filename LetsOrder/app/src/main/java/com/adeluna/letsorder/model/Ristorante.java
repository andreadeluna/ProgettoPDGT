package com.adeluna.letsorder.model;

import org.w3c.dom.Attr;

public class Ristorante {

    private String id;
    private String nome;
    private String indirizzo;
    private String apertura;
    private String posti_liberi;


    public Ristorante(String nome, String indirizzo, String apertura, String posti_liberi){
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.apertura = apertura;
        this.posti_liberi = posti_liberi;
    }


    public Ristorante(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getApertura() {
        return apertura;
    }

    public void setApertura(String apertura) {
        this.apertura = apertura;
    }

    public String getPosti() {
        return posti_liberi;
    }

    public void setPosti(String posti_liberi) {
        this.posti_liberi = posti_liberi;
    }

}
