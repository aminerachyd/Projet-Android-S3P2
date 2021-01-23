package com.inpt.jibmaak.repository;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.services.AuthResponse;
import com.inpt.jibmaak.services.RetrofitAuthService;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Classe chargée de l'authentification de l'utilisateur */
public class AuthManager {
    public enum AuthAction{
        LOGOUT,
        LOGIN,
        LOGOUT_ERROR,
        LOGIN_ERROR,
        UNEXPECTED_LOGOUT,
        UNAUTHORIZED,
        LOGIN_INCORRECT
    }

    protected SharedPreferences sharedPreferences;
    protected RetrofitAuthService authService;
    protected User user;
    protected MutableLiveData<AuthAction> authAction;
    public static final String BASE_URL = "https://www.text.com";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String USER = "user";

    @Inject
    public AuthManager(RetrofitAuthService authService,SharedPreferences sharedPreferences){
        this.authService = authService;
        this.sharedPreferences = sharedPreferences;
        this.authAction = new MutableLiveData<>();
    }
    public void saveTokens(HashMap<String,String> tokens){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(ACCESS_TOKEN,tokens.get(ACCESS_TOKEN));
        edit.apply();
    }

    public HashMap<String,String> getTokens(){
        String access = sharedPreferences.getString(ACCESS_TOKEN,null);
        if (access == null)
            return null;
        HashMap<String,String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN,access);
        return tokens;
    }

    public void saveUserLogged(){
        Gson gson = new Gson();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(USER,gson.toJson(user));
        edit.apply();
    }

    protected User getUserLogged(){
        Gson gson = new Gson();
        String json_user = sharedPreferences.getString(USER,null);
        if (json_user == null)
            return null;
        return gson.fromJson(json_user,User.class);
    }

    public void logout(boolean isUnexpected){
        //TODO : serveur ?
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(ACCESS_TOKEN);
        edit.remove(USER);
        edit.apply();
        user = null;
        authAction.setValue(isUnexpected ? AuthAction.UNEXPECTED_LOGOUT : AuthAction.LOGOUT);
    }

    public void refreshAccessToken(){
        //TODO: token rafraichi/ tokens invalides
    }

    public void login(String username,String password){
        authService.login(username,password).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!response.isSuccessful()){
                    authAction.setValue(response.code() == 400 ? AuthAction.LOGIN_INCORRECT
                            : AuthAction.LOGIN_ERROR);
                }
                else{
                    // On recupere le token
                    AuthResponse authResponse = response.body();
                    HashMap<String,String> tokens = new HashMap<>();
                    tokens.put(ACCESS_TOKEN,authResponse.getPayload());
                    saveTokens(tokens);
                    user = new User();
                    // On demande les infos
                    getUserInfo();
                }
            }
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                authAction.setValue(AuthAction.LOGIN_ERROR);
            }
        });
    }

    public void getUserInfo(){
        // On recupere les informations sur l'utilisateur connecté
        if (sharedPreferences.contains(ACCESS_TOKEN)){
            authService.checkLogin().enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (!response.isSuccessful()){
                        if (response.code() == 401 || response.code() == 400){
                            // Pas authentifié : on recommence l'authentification
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.remove(ACCESS_TOKEN);
                            edit.apply();
                        }
                        // Pour les autres erreurs on ne supprime pas le token
                        authAction.setValue(AuthAction.LOGIN_ERROR);
                    }
                    else{
                        // On recupere l'utilisateur
                        Gson gson = new Gson();
                        AuthResponse authResponse = response.body();
                        user = gson.fromJson(authResponse.getPayload(),User.class);
                        saveUserLogged();
                        authAction.setValue(AuthAction.LOGIN); // Authentification reussie
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    authAction.setValue(AuthAction.LOGIN_ERROR);
                }
            });
        }
        else{
            authAction.setValue(AuthAction.LOGIN_ERROR);
        }
    }

    public void unauthorizedAction(){
        authAction.setValue(AuthAction.UNAUTHORIZED);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public RetrofitAuthService getAuthService() {
        return authService;
    }

    public void setAuthService(RetrofitAuthService authService) {
        this.authService = authService;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public LiveData<AuthAction> getAuthAction() {
        return authAction;
    }
}
