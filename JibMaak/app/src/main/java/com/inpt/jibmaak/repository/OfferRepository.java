package com.inpt.jibmaak.repository;

import androidx.lifecycle.LiveData;


import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;

import java.util.List;

/** Repository en charge de la gestion des offres */
public interface OfferRepository {
    /**
     * Recupere l'offre correspondant à l'id
     * @param offerId l'identifiant de l'offre qu'on cherche
     */
    void getOffer(int offerId) ;

    /**
     * Recupere toutes les offres vérifiant un ensemble de critere
     * @param criteria Un objet contenant les criteres de la recherche
     */
    void searchOffer(OfferSearchCriteria criteria);

    /**
     * Met à jour une offre à jour. Son identifiant n'est pas mis à jour
     * @param offerToUpdate L'offre à mettre à jour
     */
    void updateOffer(Offer offerToUpdate);

    /**
     * Supprime une offre
     * @param offerId L'identifiant de l'offre à supprimer
     */
    void deleteOffer(int offerId);

    /**
     * Cree une offre
     * @param offerToCreate L'offre à créer
     */
    void createOffer(Offer offerToCreate);

    LiveData<Resource<List<Offer>>> getSearchData();

    void setSearchData(LiveData<Resource<List<Offer>>> searchData);

    LiveData<Resource<Offer>> getOfferData();

    void setOfferData(LiveData<Resource<Offer>> offerData);

}
