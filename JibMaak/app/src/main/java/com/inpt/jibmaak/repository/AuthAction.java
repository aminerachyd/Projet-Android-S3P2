package com.inpt.jibmaak.repository;

import com.inpt.jibmaak.model.User;

import java.util.ArrayList;
import java.util.List;

/** Classe enveloppe qui contient un code action pour connaitre le résultat d'une opération
 * liée à l'authentification. Elle possede également un attribut user pour stocker l'utilisateur
 * connecté
 */
public class AuthAction {
    public enum Action {
        LOGIN,
        LOGIN_ERROR,
        LOGIN_INCORRECT,
        LOGOUT,
        LOGOUT_ERROR,
        UNAUTHORIZED,
        REGISTER,
        REGISTER_ERROR
    }

    protected Action action;
    protected User user;
    protected List<String> consommateurs = new ArrayList<>();

    public AuthAction(){ }

    public AuthAction(Action action, User user) {
        this.action = action;
        this.user = user;
    }

    /**
     *
     * @return l'action qui vient d'être réalisée
     */
    public Action getAction() {
        return action;
    }

    /**
     * Permet à un consommateur de consommer une seule fois une action
     * @param consommateur Celui qui veut consommer l'action
     * @return L'action si le consommateur ne l'a pas encore consommée, null dans le cas contraire
     */
    public Action getAction(String consommateur){
        if (consommateurs.contains(consommateur))
            return null;
        consommateurs.add(consommateur);
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

