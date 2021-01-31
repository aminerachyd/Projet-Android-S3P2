package com.inpt.jibmaak.repository;

import com.inpt.jibmaak.model.User;

/** Classe enveloppe qui contient un code action pour connaitre le résultat d'une opération
 * liée à l'authentification. Elle possede également un attribut user pour stocker l'utilisateur
 * connecté
 */
public class AuthAction {
    public enum Action {
        ERROR,
        LOGIN,
        LOGIN_INCORRECT,
        LOGOUT,
        LOGOUT_UNEXPECTED,
        REGISTER,
        REGISTER_INCORRECT,
        UNAUTHORIZED
    }

    protected Action action;
    protected User user;
    protected boolean consumed = false;

    public AuthAction(){ }

    public AuthAction(Action action, User user) {
        this.action = action;
        this.user = user;
    }

    /**
     * Renvoie l'action sans la consommer
     * @return l'action qui vient d'être réalisée
     */
    public Action peekAction() {
        return action;
    }

    /**
     * Permet à un consommateur de consommer une seule fois une action
     * @return L'action si le consommateur ne l'a pas encore consommée, null dans le cas contraire
     */
    public Action getAction(){
        if (consumed)
            return null;
        consumed = true;
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

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }
}

