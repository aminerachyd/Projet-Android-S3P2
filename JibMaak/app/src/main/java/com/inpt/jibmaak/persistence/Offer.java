package com.inpt.jibmaak.persistence;

import java.util.Date;

public class Offer {
    protected int id;
    protected int livreurId, poidsDispo, prixKg;
    protected String depart, destination;
    protected Date dateDepart;

    public Offer() {

    }

    public Offer(int id, int livreurId, int poidsDispo, int prixKg, String depart, String destination, Date dateDepart) {
        this.id = id;
        this.livreurId = livreurId;
        this.poidsDispo = poidsDispo;
        this.prixKg = prixKg;
        this.depart = depart;
        this.destination = destination;
        this.dateDepart = dateDepart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivreurId() {
        return livreurId;
    }

    public void setLivreurId(int livreurId) {
        this.livreurId = livreurId;
    }

    public int getPoidsDispo() {
        return poidsDispo;
    }

    public void setPoidsDispo(int poidsDispo) {
        this.poidsDispo = poidsDispo;
    }

    public int getPrixKg() {
        return prixKg;
    }

    public void setPrixKg(int prixKg) {
        this.prixKg = prixKg;
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

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }
}

