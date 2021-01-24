package com.inpt.jibmaak.repository;

/** Classe enveloppe qui contient une ressource (User,Offer) avec un statut */
public class Resource<T> {
    /** Les différents status possibles des operations */
    public enum Status{
        /** L'operation a rencontrée une erreur inconnue */
        ERROR,
        /** L'operation n'est pas permise */
        UNAUTHORIZED,
        /** L'operation a été correctement effectuée */
        OK
    }
    protected T resource;
    protected Status status;

    public T getResource() {
        return resource;
    }

    public void setResource(T resource) {
        this.resource = resource;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}