package com.inpt.jibmaak.repository;

import androidx.lifecycle.MutableLiveData;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.services.RetrofitOfferService;
import com.inpt.jibmaak.services.SearchResponse;
import com.inpt.jibmaak.services.ServerResponse;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Implementation de OfferRepository qui utilise un back-end distant comme
 * source de données et Retrofit
 */
public class RetrofitOfferRepository implements OfferRepository{
    protected RetrofitOfferService offerService;
    protected MutableLiveData<Resource<ArrayList<Offer>>> searchData;

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
    public void searchOffer(OfferSearchCriteria criteria, Pagination page) {
        offerService.searchOffers(page.getPage(),page.getLimit(),criteria).enqueue(new Callback<ServerResponse<SearchResponse>>() {
            @Override
            public void onResponse(Call<ServerResponse<SearchResponse>> call, Response<ServerResponse<SearchResponse>> response) {
                Resource<ArrayList<Offer>> result = new Resource<>();
                if (!response.isSuccessful())
                    result.setStatus(Resource.Status.ERROR);
                else{
                    ServerResponse<SearchResponse> serverResponse = response.body();
                    if (serverResponse == null){
                        result.setStatus(Resource.Status.ERROR);
                    }
                    else{
                        result.setStatus(Resource.Status.OK);
                        result.setResource(response.body().getPayload().getOffers());
                    }
                }
                searchData.setValue(result);
            }

            @Override
            public void onFailure(Call<ServerResponse<SearchResponse>> call, Throwable t) {
                Resource<ArrayList<Offer>> result = new Resource<>();
                result.setStatus(Resource.Status.ERROR);
                searchData.setValue(result);
            }
        });
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
    public MutableLiveData<Resource<ArrayList<Offer>>> getSearchData() {
        return searchData;
    }

    @Override
    public void setSearchData(MutableLiveData<Resource<ArrayList<Offer>>> searchData) {
        this.searchData = searchData;
    }
}
