package com.inpt.jibmaak.services;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAuthService {
    @POST("/auth")
    Call<ServerResponse<JsonObject>> login(@Body HashMap<String,String> body);

    @GET("/auth")
    Call<ServerResponse<String>> checkLogin();

    @POST("/user")
    Call<ServerResponse<String>> register(@Body HashMap<String,String> body);
}
