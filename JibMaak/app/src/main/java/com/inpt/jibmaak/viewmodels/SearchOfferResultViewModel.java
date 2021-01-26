package com.inpt.jibmaak.viewmodels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.inpt.jibmaak.adapters.OfferAdapter;
import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.repository.Resource;

import java.util.ArrayList;

public class SearchOfferResultViewModel extends ViewModel {
    protected MutableLiveData<Resource<ArrayList<Offer>>> offersData;
    protected OfferSearchCriteria criteria;
    protected Pagination page;
    protected OfferAdapter offerAdapter;

    @ViewModelInject
    public SearchOfferResultViewModel(){
        offersData = new MutableLiveData<>();
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

    public OfferAdapter getOfferAdapter() {
        return offerAdapter;
    }

    public void setOfferAdapter(OfferAdapter offerAdapter) {
        this.offerAdapter = offerAdapter;
    }
}
