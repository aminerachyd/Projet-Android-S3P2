package com.inpt.jibmaak.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.repository.OfferRepository;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.repository.RetrofitOfferRepository;

import java.util.ArrayList;

/** ViewModel associé à l'activité SearchOfferActivity */
public class SearchOfferViewModel extends ViewModel {
    protected OfferSearchCriteria criteria;
    protected Pagination page;
    protected MutableLiveData<Resource<ArrayList<Offer>>> searchOffersData;
    protected OfferRepository offerRepo;


    @ViewModelInject
    public SearchOfferViewModel(@Assisted SavedStateHandle savedState,
                                RetrofitOfferRepository offerRepo){
        this.offerRepo = offerRepo;
        searchOffersData = new MutableLiveData<>();
        criteria = new OfferSearchCriteria();
        page = new Pagination(1,3);
        this.offerRepo.setSearchData(searchOffersData);
    }

    public void chercherOffres(OfferSearchCriteria rechercheCriteria,Pagination paginate) {
        offerRepo.searchOffer(rechercheCriteria,paginate);
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

    public OfferRepository getOfferRepo() {
        return offerRepo;
    }

    public void setOfferRepo(OfferRepository offerRepo) {
        this.offerRepo = offerRepo;
    }

    public LiveData<Resource<ArrayList<Offer>>> getSearchOffersData() {
        return searchOffersData;
    }
}
