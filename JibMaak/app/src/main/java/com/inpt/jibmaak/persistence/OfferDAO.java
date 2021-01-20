package com.inpt.jibmaak.persistence;

public class OfferDAO implements DAO<Offer> {

    @Override
    public Offer createEntity(Offer entity) {
        // TODO Enregistrement dans la base de données

        return entity;
    }

    @Override
    public Offer updateEntity(int entityId) {
        // TODO Update dans la base de données

        return null;
    }

    @Override
    public Offer getEntity(int entityId) {
        // TODO Récupération depuis la base de données

        return null;
    }

    @Override
    public Offer deleteEntity(int entityId) {
        // TODO Suppressiosn depuis la base de données

        return null;
    }
}
