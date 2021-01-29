package com.inpt.jibmaak.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;

import java.util.ArrayList;

/** Repository en charge de la gestion des offres
 * Pour récuperer les resultats des actions, il faut observer les variables {@link LiveData} renvoyées
 * par les methodes {@link #getResultData()} et {@link #getSearchData()}
 *
 */
public interface OfferRepository {
    /**
     * Recupere l'offre correspondant à l'id
     * @param offerId l'identifiant de l'offre qu'on cherche
     */
    void getOffer(String offerId) ;

    /**
     * Recupere toutes les offres vérifiant un ensemble de critere
     * @param criteria Un objet contenant les criteres de la recherche
     * @param paginate Un objet qui définit la page à recuperer
     */
    void searchOffer(OfferSearchCriteria criteria, Pagination paginate);

    /**
     * Met à jour une offre à jour. Son identifiant n'est pas mis à jour
     * @param offerToUpdate L'offre à mettre à jour
     */
    void updateOffer(Offer offerToUpdate);

    /**
     * Supprime une offre
     * @param offerId L'identifiant de l'offre à supprimer
     */
    void deleteOffer(String offerId);

    /**
     * Cree une offre
     * @param offerToCreate L'offre à créer
     */
    void createOffer(Offer offerToCreate);

    LiveData<Resource<ArrayList<Offer>>> getSearchData();

    void setSearchData(MutableLiveData<Resource<ArrayList<Offer>>> searchData);

    LiveData<Resource<String>> getResultData();

    void setResultData(MutableLiveData<Resource<String>> resultData);



}
