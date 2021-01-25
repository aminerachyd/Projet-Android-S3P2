package com.inpt.jibmaak.services;

import com.inpt.jibmaak.model.OfferSearchCriteria;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitOfferService {
    // TODO : méthodes pour Retrofit
    @POST("/offers")
    Call<ServerResponse<SearchResponse>> searchOffers(@Query("page") int page
            , @Query("limit") int limit, @Body OfferSearchCriteria filtres);
}
