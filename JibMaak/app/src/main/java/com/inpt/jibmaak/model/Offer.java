package com.inpt.jibmaak.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Objects;

public class Offer implements Parcelable {
    protected String id;
    protected int poidsDispo, prixKg;
    protected String lieuDepart, lieuArrivee;
    protected Date dateDepart, dateArrivee;
    protected User user;
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

    public Offer(String id, int poidsDispo, int prixKg, String lieuDepart, String lieuArrivee,
                 Date dateDepart, Date dateArrivee, User user) {
        this.id = id;
        this.poidsDispo = poidsDispo;
        this.prixKg = prixKg;
        this.lieuDepart = lieuDepart;
        this.lieuArrivee = lieuArrivee;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.user = user;
    }

    protected Offer(Parcel in){
        this.user = in.readParcelable(User.class.getClassLoader());
        this.id = in.readString();
        this.poidsDispo = in.readInt();
        this.prixKg = in.readInt();
        this.lieuDepart = in.readString();
        this.lieuArrivee = in.readString();
        this.dateDepart = new Date(in.readLong());
        this.dateArrivee = new Date(in.readLong());
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

    public String getLieuArrivee() {
        return lieuArrivee;
    }

    public void setLieuArrivee(String lieuArrivee) {
        this.lieuArrivee = lieuArrivee;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user,flags);
        dest.writeString(this.id);
        dest.writeInt(this.poidsDispo);
        dest.writeInt(this.prixKg);
        dest.writeString(this.lieuDepart);
        dest.writeString(this.lieuArrivee);
        dest.writeLong(this.dateDepart.getTime());
        dest.writeLong(this.dateArrivee.getTime());
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
                Objects.equals(lieuArrivee, offer.lieuArrivee) &&
                Objects.equals(dateDepart, offer.dateDepart) &&
                Objects.equals(dateArrivee, offer.dateArrivee) &&
                Objects.equals(user, offer.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, poidsDispo, prixKg, lieuDepart, lieuArrivee, dateDepart, dateArrivee, user);
    }
}

