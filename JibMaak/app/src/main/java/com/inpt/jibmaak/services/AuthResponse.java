package com.inpt.jibmaak.services;

/** Une classe qui correspond à la réponse JSON du serveur durant l'authentification */
public class AuthResponse {
    protected String message;
    protected String payload; // Le payload (varie en fonction de la requete)
    protected String error; // Une erreur eventuelle

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
