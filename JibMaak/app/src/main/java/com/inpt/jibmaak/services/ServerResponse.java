package com.inpt.jibmaak.services;

/** Une classe qui correspond à une réponse du serveur
 * Le type T est le type du payload associé à la réponse
 * */
public class ServerResponse<T> {
    protected String message;
    protected T payload; // Le payload (varie en fonction de la requete)
    protected String error; // Une erreur eventuelle

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
