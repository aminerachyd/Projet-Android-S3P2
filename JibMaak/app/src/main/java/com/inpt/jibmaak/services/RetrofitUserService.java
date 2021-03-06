package com.inpt.jibmaak.services;

import com.inpt.jibmaak.model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/** Service qui utilise Retrofit pour effectuer des opérations de crud sur les utilisateurs */
public interface RetrofitUserService {
    @GET("/user/{id}")
    Call<ServerResponse<User>> getUser(@Path("id") String id);

    @PUT("/user/{id}")
    Call<ServerResponse<String>> updateUser(@Path("id") String id,
                                            @Body HashMap<String,String> userToUpdate);

    @DELETE("/user/{id}")
    Call<ServerResponse<String>> deleteUser(@Path("id") String id);

}
