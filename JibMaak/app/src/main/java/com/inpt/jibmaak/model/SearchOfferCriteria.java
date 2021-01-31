package com.inpt.jibmaak.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/** Classe qui décrit les criteres d'une recherche sur les offres */
public class SearchOfferCriteria implements Parcelable {
    protected String depart,destination;
    protected Date departAvant,departApres;
    protected Date arriveAvant,arriveApres;
    protected Integer minPrixKg,maxPrixKg;
    protected Integer minPoidsDisponible;
    protected User user;

    public SearchOfferCriteria(){}

    protected SearchOfferCriteria(Parcel in) {
        depart = in.readString();
        destination = in.readString();
        if (in.readByte() == 0) {
            minPrixKg = null;
        } else {
            minPrixKg = in.readInt();
        }
        if (in.readByte() == 0) {
            maxPrixKg = null;
        } else {
            maxPrixKg = in.readInt();
        }
        if (in.readByte() == 0) {
            minPoidsDisponible = null;
        } else {
            minPoidsDisponible = in.readInt();
        }
        user = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(depart);
        dest.writeString(destination);
        if (minPrixKg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minPrixKg);
        }
        if (maxPrixKg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxPrixKg);
        }
        if (minPoidsDisponible == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minPoidsDisponible);
        }
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchOfferCriteria> CREATOR = new Creator<SearchOfferCriteria>() {
        @Override
        public SearchOfferCriteria createFromParcel(Parcel in) {
            return new SearchOfferCriteria(in);
        }

        @Override
        public SearchOfferCriteria[] newArray(int size) {
            return new SearchOfferCriteria[size];
        }
    };

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
