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
        OK,
        /** Une erreur du serveur */
        SERVER_ERROR,
        /** Une erreur mais liée à la requete */
        REQUEST_ERROR
    }
    /** Les différentes opérations de crud possibles */
    public enum Operation{
        CREATE,
        READ,
        UPDATE,
        DELETE
    }
    protected T resource;
    protected Status status;
    protected Operation operation;
    protected boolean consumed = false;

    public T getResource() {
        if (!consumed)
            consumed = true;
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

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}