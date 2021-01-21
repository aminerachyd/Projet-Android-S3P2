package com.inpt.jibmaak.repository;

import androidx.lifecycle.LiveData;

import com.inpt.jibmaak.model.Offer;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Implementation de OfferRepository qui utilise un back-end distant comme
 * source de données et Retrofit
 */
public class RetrofitOfferRepository implements OfferRepository{
    public final static String BASE_URL = ""; //TODO: Change url
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

    // TODO : Methodes
    @Override
    public LiveData<Resource<Offer>> getOffer(int offerId) {
        return offerData;
    }

    @Override
    public LiveData<Resource<List<Offer>>> searchOffer(OfferSearchCriteria criteria) {
        return searchData;
    }

    @Override
    public LiveData<Resource<Offer>> updateOffer(Offer offerToUpdate) {
        return offerData;
    }

    @Override
    public LiveData<Resource<Offer>> deleteOffer(int offerId) {
        return offerData;
    }

    @Override
    public LiveData<Resource<Offer>> createOffer(Offer offerToCreate) {
        return offerData;
    }
}
