package com.inpt.jibmaak.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.repository.OfferRepository;
import com.inpt.jibmaak.repository.Resource;

import java.util.ArrayList;

public class SearchOfferResultViewModel extends ViewModel {
    protected MutableLiveData<Resource<ArrayList<Offer>>> offersData;
    protected OfferRepository offerRepository;
    protected ArrayList<Offer> savedOffers;
    protected OfferSearchCriteria criteria;
    protected Pagination page;
    protected boolean searchFinished;

    @ViewModelInject
    public SearchOfferResultViewModel(@Assisted SavedStateHandle savedStateHandle,
                                      OfferRepository offerRepository){
        this.offersData = new MutableLiveData<>();
        this.offerRepository = offerRepository;
        this.offerRepository.setSearchData(this.offersData);
        this.searchFinished = false;
    }

    public void continueSearch(){
        if (!searchFinished){
            page.setPage(page.getPage()+1);
            offerRepository.searchOffer(criteria,page);
        }
    }

    public void refresh() {
        page.setPage(0);
        searchFinished = false;
        continueSearch();
    }

    public MutableLiveData<Resource<ArrayList<Offer>>> getOffersData() {
        return offersData;
    }

    public OfferSearchCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(OfferSearchCriteria criteria) {
        this.criteria = criteria;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public ArrayList<Offer> getSavedOffers() {
        return savedOffers;
    }

    public void setSavedOffers(ArrayList<Offer> savedOffers) {
        this.savedOffers = savedOffers;
    }

    public boolean isSearchFinished() {
        return searchFinished;
    }

    public void setSearchFinished(boolean searchFinished) {
        this.searchFinished = searchFinished;
    }
}
