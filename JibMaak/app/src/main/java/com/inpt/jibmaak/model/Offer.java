package com.inpt.jibmaak.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Objects;

public class Offer implements Parcelable {
    protected String id;
    protected int poidsDispo, prixKg;
    protected String lieuDepart, lieuDestination;
    protected Date dateDepart,dateArrive;
    protected User proprietaire;
    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    public Offer(){}

    public Offer(String id, int poidsDispo, int prixKg, String lieuDepart, String lieuDestination,
                 Date dateDepart, Date dateArrive, User proprietaire) {
        this.id = id;
        this.poidsDispo = poidsDispo;
        this.prixKg = prixKg;
        this.lieuDepart = lieuDepart;
        this.lieuDestination = lieuDestination;
        this.dateDepart = dateDepart;
        this.dateArrive = dateArrive;
        this.proprietaire = proprietaire;
    }

    protected Offer(Parcel in){
        this.proprietaire = in.readParcelable(User.class.getClassLoader());
        this.id = in.readString();
        this.poidsDispo = in.readInt();
        this.prixKg = in.readInt();
        this.lieuDepart = in.readString();
        this.lieuDestination = in.readString();
        this.dateDepart = new Date(in.readLong());
        this.dateArrive = new Date(in.readLong());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLieuDepart() {
        return lieuDepart;
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    public String getLieuDestination() {
        return lieuDestination;
    }

    public void setLieuDestination(String lieuDestination) {
        this.lieuDestination = lieuDestination;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateArrive() {
        return dateArrive;
    }

    public void setDateArrive(Date dateArrive) {
        this.dateArrive = dateArrive;
    }

    public User getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(User proprietaire) {
        this.proprietaire = proprietaire;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.proprietaire,flags);
        dest.writeString(this.id);
        dest.writeInt(this.poidsDispo);
        dest.writeInt(this.prixKg);
        dest.writeString(this.lieuDepart);
        dest.writeString(this.lieuDestination);
        dest.writeLong(this.dateDepart.getTime());
        dest.writeLong(this.dateArrive.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return poidsDispo == offer.poidsDispo &&
                prixKg == offer.prixKg &&
                Objects.equals(id, offer.id) &&
                Objects.equals(lieuDepart, offer.lieuDepart) &&
                Objects.equals(lieuDestination, offer.lieuDestination) &&
                Objects.equals(dateDepart, offer.dateDepart) &&
                Objects.equals(dateArrive, offer.dateArrive) &&
                Objects.equals(proprietaire, offer.proprietaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, poidsDispo, prixKg, lieuDepart, lieuDestination, dateDepart, dateArrive, proprietaire);
    }
}

