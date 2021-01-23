package com.inpt.jibmaak.repository;

import androidx.lifecycle.MutableLiveData;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.services.RetrofitOfferService;

import java.util.List;

import javax.inject.Inject;

/** Implementation de OfferRepository qui utilise un back-end distant comme
 * source de données et Retrofit
 */
public class RetrofitOfferRepository implements OfferRepository{
    public final static String BASE_URL = "http://www.test.com"; //TODO: Change url
    protected RetrofitOfferService offerService;
    protected MutableLiveData<Resource<List<Offer>>> searchData;
    protected MutableLiveData<Resource<Offer>> offerData;

    @Inject
    public RetrofitOfferRepository(RetrofitOfferService offerService){
        this.offerService = offerService;
    }

    public RetrofitOfferService getOfferService() {
        return offerService;
    }

    public void setOfferService(RetrofitOfferService offerService) {
        this.offerService = offerService;
    }

    // TODO : methodes
    @Override
    public void getOffer(int offerId) {

    }

    @Override
    public void searchOffer(OfferSearchCriteria criteria) {

    }

    @Override
    public void updateOffer(Offer offerToUpdate) {

    }

    @Override
    public void deleteOffer(int offerId) {

    }

    @Override
    public void createOffer(Offer offerToCreate) {

    }

    @Override
    public MutableLiveData<Resource<List<Offer>>> getSearchData() {
        return searchData;
    }

    @Override
    public void setSearchData(MutableLiveData<Resource<List<Offer>>> searchData) {
        this.searchData = searchData;
    }

    @Override
    public MutableLiveData<Resource<Offer>> getOfferData() {
        return offerData;
    }

    @Override
    public void setOfferData(MutableLiveData<Resource<Offer>> offerData) {
        this.offerData = offerData;
    }
}
