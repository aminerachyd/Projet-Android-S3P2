package com.inpt.jibmaak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.repository.OfferRepository;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.repository.RetrofitOfferRepository;

import java.util.List;

/** ViewModel associé à l'activité SearchOfferActivity */
public class SearchOfferViewModel extends ViewModel {
    protected OfferSearchCriteria criteria = new OfferSearchCriteria();
    protected LiveData<Resource<List<Offer>>> searchOffersData = new MutableLiveData<>();
    protected LiveData<Resource<Offer>> offerData = new MutableLiveData<>();
    protected OfferRepository offerRepo;

    public SearchOfferViewModel(){
        offerRepo = new RetrofitOfferRepository();
        offerRepo.setSearchData(searchOffersData);
        offerRepo.setOfferData(offerData);
    }

    public void chercherOffres(OfferSearchCriteria rechercheCriteria) {
        offerRepo.searchOffer(rechercheCriteria);
    }

    public OfferSearchCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(OfferSearchCriteria criteria) {
        this.criteria = criteria;
    }

    public OfferRepository getOfferRepo() {
        return offerRepo;
    }

    public void setOfferRepo(OfferRepository offerRepo) {
        this.offerRepo = offerRepo;
    }

    public LiveData<Resource<List<Offer>>> getSearchOffersData() {
        return searchOffersData;
    }

    public LiveData<Resource<Offer>> getOfferData() {
        return offerData;
    }
}
