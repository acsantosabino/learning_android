package com.aluno.arthur.lista2;

import java.io.Serializable;

public class Pokemon implements Serializable {
    private int num;
    private String nome;
    private int imagem;
    private String tipo;
    private String descricao;

    public Pokemon(int num, String nome) {
        this.num = num;
        this.nome = nome;
    }

    public Pokemon(int num, String nome, int imagem) {
        this.num = num;
        this.nome = nome;
        this.imagem = imagem;
    }

    public Pokemon(int num, String nome, int imagem, String tipo, String descricao) {
        this.num = num;
        this.nome = nome;
        this.imagem = imagem;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
