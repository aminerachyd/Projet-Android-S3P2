package com.inpt.jibmaak.repository;

import java.util.Date;

/** Classe qui décrit les criteres d'une recherche sur les offres */
public class OfferSearchCriteria {
    protected String depart,destination;
    protected Date departAvant,departApres;
    protected Date arriveAvant,arriveApres;
    protected Integer minPrixKg,maxPrixKg;
    protected Integer minPoidsDisponible,maxPoidsDisponible;

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

    public Date getDepartAvant() {
        return departAvant;
    }

    public void setDepartAvant(Date departAvant) {
        this.departAvant = departAvant;
    }

    public Date getDepartApres() {
        return departApres;
    }

    public void setDepartApres(Date departApres) {
        this.departApres = departApres;
    }

    public Date getArriveAvant() {
        return arriveAvant;
    }

    public void setArriveAvant(Date arriveAvant) {
        this.arriveAvant = arriveAvant;
    }

    public Date getArriveApres() {
        return arriveApres;
    }

    public void setArriveApres(Date arriveApres) {
        this.arriveApres = arriveApres;
    }

    public Integer getMinPrixKg() {
        return minPrixKg;
    }

    public void setMinPrixKg(Integer minPrixKg) {
        this.minPrixKg = minPrixKg;
    }

    public Integer getMaxPrixKg() {
        return maxPrixKg;
    }

    public void setMaxPrixKg(Integer maxPrixKg) {
        this.maxPrixKg = maxPrixKg;
    }

    public Integer getMinPoidsDisponible() {
        return minPoidsDisponible;
    }

    public void setMinPoidsDisponible(Integer minPoidsDisponible) {
        this.minPoidsDisponible = minPoidsDisponible;
    }

    public Integer getMaxPoidsDisponible() {
        return maxPoidsDisponible;
    }

    public void setMaxPoidsDisponible(Integer maxPoidsDisponible) {
        this.maxPoidsDisponible = maxPoidsDisponible;
    }
}
