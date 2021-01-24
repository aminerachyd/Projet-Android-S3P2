package com.inpt.jibmaak.services;

import android.content.SharedPreferences;

import com.inpt.jibmaak.repository.AuthManager;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/** Intercepteur chargé d'ajouter le token aux requetes */
public class AuthInterceptor implements Interceptor {
    protected SharedPreferences sharedPreferences;
    @Inject
    public AuthInterceptor(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        if (!sharedPreferences.contains(AuthManager.ACCESS_TOKEN))
            return chain.proceed(oldRequest);
        Request newRequest = oldRequest.newBuilder()
                .header("x-auth-token",
                        sharedPreferences.getString(AuthManager.ACCESS_TOKEN,""))
                .build();
        return chain.proceed(newRequest);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
