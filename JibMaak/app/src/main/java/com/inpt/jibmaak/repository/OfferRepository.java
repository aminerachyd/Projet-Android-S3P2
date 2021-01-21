package com.inpt.jibmaak.repository;

import androidx.lifecycle.LiveData;


import com.inpt.jibmaak.model.Offer;

import java.util.List;

/** Repository en charge de la gestion des offres */
public interface OfferRepository {
    /**
     * Recupere l'offre correspondant à l'id
     * @param offerId l'identifiant de l'offre qu'on cherche
     * @return Une resource observable contenant l'offre (ou null si elle n'est pas trouvé)
     */
    public LiveData<Resource<Offer>> getOffer(int offerId) ;

    /**
     * Recupere toutes les offres vérifiant un ensemble de critere
     * @param criteria Un objet contenant les criteres de la recherche
     * @return Une resource observable contenant la liste des offres
     */
    public LiveData<Resource<List<Offer>>> searchOffer(OfferSearchCriteria criteria);

    /**
     * Met à jour une offre à jour. Son identifiant n'est pas mis à jour
     * @param offerToUpdate L'offre à mettre à jour
     * @return Une resource observable contenant l'offre mise à jour
     */
    public LiveData<Resource<Offer>> updateOffer(Offer offerToUpdate);

    /**
     * Supprime une offre
     * @param offerId L'identifiant de l'offre à supprimer
     * @return Une resource observable contenant le résultat de l'operation
     */
    public LiveData<Resource<Offer>> deleteOffer(int offerId);

    /**
     * Cree une offre
     * @param offerToCreate L'offre à créer
     * @return Une resource observable contenant l'offre créee
     */
    public LiveData<Resource<Offer>> createOffer(Offer offerToCreate);

}
