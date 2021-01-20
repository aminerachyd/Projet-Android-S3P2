package com.inpt.jibmaak.persistence;

public interface DAO<T> {
    /**
     * Fonction pour créer une entité
     *
     * @return L'entité créée
     */
    public T createEntity(T entity);

    /**
     * Fonction pour mettre à jour une entité
     *
     * @param entityId
     * @return
     */
    public T updateEntity(int entityId);

    /**
     * Fonction récupérer une entité
     *
     * @param entityId
     * @return
     */
    public T getEntity(int entityId);


    /**
     * Fonction pour supprimer une entité
     *
     * @param entityId
     * @return
     */
    public T deleteEntity(int entityId);
}
