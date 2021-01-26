package com.inpt.jibmaak.services;

import com.inpt.jibmaak.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/** Service qui utilise Retrofit pour récuperer les données distantes */
public interface RetrofitUserService {
    @GET("/user/{id}")
    Call<ServerResponse<User>> getUser(@Path("id") String id);

    @PUT("/user/{id}")
    Call<ServerResponse<String>> updateUser(@Path("id") String id, @Body User user);

    @DELETE("/user/{id}")
    Call<ServerResponse<String>> deleteUser(@Path("id") String id);

}
