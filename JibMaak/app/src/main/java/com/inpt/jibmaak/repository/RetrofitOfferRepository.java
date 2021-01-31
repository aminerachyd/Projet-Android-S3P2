package com.inpt.jibmaak.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.model.SearchOfferCriteria;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.services.RetrofitOfferService;
import com.inpt.jibmaak.services.SearchResponse;
import com.inpt.jibmaak.services.ServerResponse;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.inpt.jibmaak.repository.Resource.Operation.CREATE;
import static com.inpt.jibmaak.repository.Resource.Operation.DELETE;
import static com.inpt.jibmaak.repository.Resource.Operation.READ;
import static com.inpt.jibmaak.repository.Resource.Operation.UPDATE;
import static com.inpt.jibmaak.repository.Resource.Status.ERROR;
import static com.inpt.jibmaak.repository.Resource.Status.OK;
import static com.inpt.jibmaak.repository.Resource.Status.REQUEST_ERROR;
import static com.inpt.jibmaak.repository.Resource.Status.SERVER_ERROR;

/** Implementation de OfferRepository qui utilise un back-end distant comme
 * source de données et Retrofit
 */
public class RetrofitOfferRepository implements OfferRepository{
    protected RetrofitOfferService offerService;
    protected MutableLiveData<Resource<ArrayList<Offer>>> searchData;
    protected MutableLiveData<Resource<String>> operationResultData;

    @Inject
    public RetrofitOfferRepository(RetrofitOfferService offerService){
        this.offerService = offerService;
        this.searchData = new MutableLiveData<>();
        this.operationResultData = new MutableLiveData<>();
    }

    public RetrofitOfferService getOfferService() {
        return offerService;
    }

    public void setOfferService(RetrofitOfferService offerService) {
        this.offerService = offerService;
    }

    @Override
    public LiveData<Resource<ArrayList<Offer>>> getSearchData() {
        return searchData;
    }

    @Override
    public void setSearchData(MutableLiveData<Resource<ArrayList<Offer>>> searchData) {
        this.searchData = searchData;
    }

    @Override
    public LiveData<Resource<String>> getResultData() {
        return this.operationResultData;
    }

    @Override
    public void setResultData(MutableLiveData<Resource<String>> resultData) {
        this.operationResultData = resultData;
    }


    @Override
    public void getOffer(String offerId) {
        offerService.getOffer(offerId).enqueue(new Callback<ServerResponse<Offer>>() {
            @Override
            public void onResponse(Call<ServerResponse<Offer>> call, Response<ServerResponse<Offer>> response) {
                Resource<ArrayList<Offer>> result = new Resource<>();
                result.setOperation(READ);
                if (!response.isSuccessful()){
                    result.setStatus(response.code() == 400 ? REQUEST_ERROR : SERVER_ERROR);
                }
                else{
                    Offer offer = response.body().getPayload();
                    ArrayList<Offer> offers = new ArrayList<>();
                    offers.add(offer);
                    result.setStatus(OK);
                    result.setResource(offers);
                }
                searchData.setValue(result);
            }

            @Override
            public void onFailure(Call<ServerResponse<Offer>> call, Throwable t) {
                Resource<ArrayList<Offer>> result = new Resource<>();
                result.setStatus(ERROR);
                result.setOperation(READ);
                searchData.setValue(result);
            }
        });
    }

    @Override
    public void searchOffer(SearchOfferCriteria criteria, Pagination page) {
        User user = criteria.getUser();
        if (user != null){
            getOfferOf(user,page);
            return;
        }
        offerService.searchOffers(page.getPage(),page.getLimit(),criteria).enqueue(new Callback<ServerResponse<SearchResponse>>() {
            @Override
            public void onResponse(Call<ServerResponse<SearchResponse>> call, Response<ServerResponse<SearchResponse>> response) {
                Resource<ArrayList<Offer>> result = new Resource<>();
                result.setOperation(READ);
                if (!response.isSuccessful())
                    result.setStatus(response.code() == 400 ? REQUEST_ERROR : SERVER_ERROR);
                else{
                    result.setStatus(OK);
                    result.setResource(response.body().getPayload().getOffers());
                }
                searchData.setValue(result);
            }

            @Override
            public void onFailure(Call<ServerResponse<SearchResponse>> call, Throwable t) {
                Resource<ArrayList<Offer>> result = new Resource<>();
                result.setStatus(ERROR);
                result.setOperation(READ);
                searchData.setValue(result);
            }
        });
    }

    protected void getOfferOf(User user,Pagination page) {
        offerService.getOffersOf(user.getId(),page.getPage(),page.getLimit()).enqueue(new Callback<ServerResponse<SearchResponse>>() {
            @Override
            public void onResponse(Call<ServerResponse<SearchResponse>> call, Response<ServerResponse<SearchResponse>> response) {
                Resource<ArrayList<Offer>> result = new Resource<>();
                result.setOperation(READ);
                if (!response.isSuccessful())
                    result.setStatus(SERVER_ERROR);
                else{
                    result.setStatus(OK);
                    ArrayList<Offer> offers = response.body().getPayload().getOffers();
                    for (Offer offer : offers)
                        offer.setUser(user);
                    result.setResource(offers);
                }
                searchData.setValue(result);
            }

            @Override
            public void onFailure(Call<ServerResponse<SearchResponse>> call, Throwable t) {
                Resource<ArrayList<Offer>> result = new Resource<>();
                result.setStatus(ERROR);
                result.setOperation(READ);
                searchData.setValue(result);
            }
        });
    }

    @Override
    public void updateOffer(Offer offerToUpdate) {
        offerService.updateOffer(offerToUpdate.getId(),offerToUpdate)
                .enqueue(new CrudCallback<>(UPDATE, operationResultData));
    }

    @Override
    public void deleteOffer(String offerId) {
        offerService.deleteOffer(offerId)
                .enqueue(new CrudCallback<>(DELETE, operationResultData));
    }

    @Override
    public void createOffer(Offer offerToCreate) {
        offerService.createOffer(offerToCreate)
                .enqueue(new CrudCallback<>(CREATE, operationResultData));
    }
}
