package com.inpt.jibmaak.model;

import java.util.Date;

public class Offer {
    protected int id,user_id;
    protected int poids_disponible, prix_kg;
    protected String depart, destination;
    protected Date date_depart,date_arrive;

    public Offer() {

    }

    public Offer(int id, int user_id, int poids_disponible, int prix_kg, String depart, String destination, Date date_depart, Date date_arrive) {
        this.id = id;
        this.user_id = user_id;
        this.poids_disponible = poids_disponible;
        this.prix_kg = prix_kg;
        this.depart = depart;
        this.destination = destination;
        this.date_depart = date_depart;
        this.date_arrive = date_arrive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPoids_disponible() {
        return poids_disponible;
    }

    public void setPoids_disponible(int poids_disponible) {
        this.poids_disponible = poids_disponible;
    }

    public int getPrix_kg() {
        return prix_kg;
    }

    public void setPrix_kg(int prix_kg) {
        this.prix_kg = prix_kg;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(Date date_depart) {
        this.date_depart = date_depart;
    }

    public Date getDate_arrive() {
        return date_arrive;
    }

    public void setDate_arrive(Date date_arrive) {
        this.date_arrive = date_arrive;
    }
}

