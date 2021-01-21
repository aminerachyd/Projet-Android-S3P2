package com.inpt.jibmaak.repository;

/** Un objet wrapper qui contient une ressource avec un statut */
public class Resource<T> {
    /** Les différents status possibles des operations */
    enum Status{
        /** L'operation a rencontrée une erreur inconnue */
        ERROR,
        /** L'operation n'est pas permise */
        UNAUTHORIZED,
        /** L'operation a été correctement effectuée */
        OK
    }
    private T resource;
    private Status status;

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