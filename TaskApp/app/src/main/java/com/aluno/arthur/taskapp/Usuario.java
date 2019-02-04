package com.aluno.arthur.taskapp;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nome;
    private String token;
    private String fotoUrl;

    public Usuario() {
    }

    public Usuario(String nome, String token, String fotoUrl) {
        this.nome = nome;
        this.token = token;
        this.fotoUrl = fotoUrl;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
