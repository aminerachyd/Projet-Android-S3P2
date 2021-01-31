package com.inpt.jibmaak.services;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.SearchOfferCriteria;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/** Service qui utilise Retrofit pour effectuer des opérations de crud sur les offres */
public interface RetrofitOfferService {
    @POST("/offers")
    Call<ServerResponse<SearchResponse>> searchOffers(@Query("page") int page
            , @Query("limit") int limit, @Body SearchOfferCriteria filtres);

    @GET("/offers/user/{id}")
    Call<ServerResponse<SearchResponse>> getOffersOf(@Path("id") String id,@Query("page") int page
            , @Query("limit") int limit);

    @POST("/offer")
    Call<ServerResponse<String>> createOffer(@Body Offer offer);

    @GET("/offer/{id}")
    Call<ServerResponse<Offer>> getOffer(@Path("id") String id);

    @PUT("/offer/{id}")
    Call<ServerResponse<String>> updateOffer(@Path("id") String id,@Body Offer offer);

    @DELETE("/offer/{id}")
    Call<ServerResponse<String>> deleteOffer(@Path("id") String id);


}
