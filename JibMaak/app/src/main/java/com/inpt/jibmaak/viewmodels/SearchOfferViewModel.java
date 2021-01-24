package com.inpt.jibmaak.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.repository.AuthManager;
import com.inpt.jibmaak.repository.OfferRepository;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.repository.RetrofitOfferRepository;

import java.util.List;

/** ViewModel associé à l'activité SearchOfferActivity */
public class SearchOfferViewModel extends ViewModel {
    protected OfferSearchCriteria criteria = new OfferSearchCriteria();
    protected MutableLiveData<Resource<List<Offer>>> searchOffersData;
    protected MutableLiveData<Resource<Offer>> offerData;
    protected AuthManager authManager;
    protected OfferRepository offerRepo;

    @ViewModelInject
    public SearchOfferViewModel(@Assisted SavedStateHandle savedState,
                                RetrofitOfferRepository offerRepo, AuthManager authManager){
        searchOffersData = new MutableLiveData<>();
        offerData = new MutableLiveData<>();
        this.offerRepo = offerRepo;
        this.offerRepo.setSearchData(searchOffersData);
        this.offerRepo.setOfferData(offerData);
        this.authManager = authManager;
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

    public AuthManager getAuthManager() {
        return authManager;
    }

    public void setAuthManager(AuthManager authManager) {
        this.authManager = authManager;
    }
}
