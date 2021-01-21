package com.inpt.jibmaak.repository;

import androidx.lifecycle.LiveData;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Implementation de OfferRepository qui utilise un back-end distant comme
 * source de données et Retrofit
 */
public class RetrofitOfferRepository implements OfferRepository{
    public final static String BASE_URL = "http://www.test.com"; //TODO: Change url
    protected RetrofitOfferService offerService;
    protected LiveData<Resource<List<Offer>>> searchData;
    protected LiveData<Resource<Offer>> offerData;

    public RetrofitOfferRepository(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        offerService = retrofit.create(RetrofitOfferService.class);
    }

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
    public LiveData<Resource<List<Offer>>> getSearchData() {
        return searchData;
    }

    @Override
    public void setSearchData(LiveData<Resource<List<Offer>>> searchData) {
        this.searchData = searchData;
    }

    @Override
    public LiveData<Resource<Offer>> getOfferData() {
        return offerData;
    }

    @Override
    public void setOfferData(LiveData<Resource<Offer>> offerData) {
        this.offerData = offerData;
    }
}
