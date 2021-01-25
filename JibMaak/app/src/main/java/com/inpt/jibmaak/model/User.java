package com.inpt.jibmaak.model;

import java.util.Objects;

public class User {

    protected String id;
    protected String email;
    protected String nom;
    protected String prenom;
    protected String telephone;

    public User(){

    }

    public User(String id, String email, String nom, String prenom, String telephone) {
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                email.equals(user.email) &&
                nom.equals(user.nom) &&
                prenom.equals(user.prenom) &&
                telephone.equals(user.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, nom, prenom, telephone);
    }
}
