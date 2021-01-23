package com.inpt.jibmaak.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAuthService {
    @POST("/auth")
    @FormUrlEncoded
    Call<AuthResponse> login(@Field("email") String email, @Field("password") String password);

    @GET("/auth")
    Call<AuthResponse> checkLogin();
}
