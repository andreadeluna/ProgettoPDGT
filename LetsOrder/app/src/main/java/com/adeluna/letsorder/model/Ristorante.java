package com.adeluna.letsorder.model;

import org.w3c.dom.Attr;

public class Ristorante {

    private String id;
    private String nome;
    private String indirizzo;
    private String apertura;


    public Ristorante(String nome, String indirizzo){
        this.nome = nome;
        this.indirizzo = indirizzo;
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

}
