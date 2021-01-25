package com.inpt.jibmaak.repository;

import com.inpt.jibmaak.model.User;

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
        UNEXPECTED_LOGOUT,
        REGISTER,
        REGISTER_ERROR
    }

    protected Action action;
    protected User user;

    public AuthAction(){}

    public AuthAction(Action action, User user) {
        this.action = action;
        this.user = user;
    }

    public Action getAction() {
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

