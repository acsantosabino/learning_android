package com.aluno.arthur.leiturama.models;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class User implements Serializable {

    public String id;
    public String name;
    public String phone;
    public String email;
    public int n_read;
    public int n_borrowed;
    public int n_lent;

    public User() {
    }

    public User(String id, String name, String phone, String email, int n_read, int n_borrowed, int n_lent) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.n_read = n_read;
        this.n_borrowed = n_borrowed;
        this.n_lent = n_lent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getN_read() {
        return n_read;
    }

    public void setN_read(int n_read) {
        this.n_read = n_read;
    }

    public int getN_borrowed() {
        return n_borrowed;
    }

    public void setN_borrowed(int n_borrowed) {
        this.n_borrowed = n_borrowed;
    }

    public int getN_lent() {
        return n_lent;
    }

    public void setN_lent(int n_lent) {
        this.n_lent = n_lent;
    }
}
