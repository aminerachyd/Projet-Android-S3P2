package com.inpt.jibmaak.repository;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.AuthAction.Action;
import com.inpt.jibmaak.services.AuthResponse;
import com.inpt.jibmaak.services.RetrofitAuthService;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Classe chargée de l'authentification de l'utilisateur */
public class AuthManager {

    protected SharedPreferences sharedPreferences;
    protected RetrofitAuthService authService;
    protected User user;
    protected MutableLiveData<AuthAction> authActionData;
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String USER = "user";

    @Inject
    public AuthManager(RetrofitAuthService authService,SharedPreferences sharedPreferences){
        this.authService = authService;
        this.sharedPreferences = sharedPreferences;
        this.authActionData = new MutableLiveData<>();
        init();
    }

    public void init(){
        user = getUserLogged();
        if (user != null){
            AuthAction authAction = new AuthAction(Action.LOGIN,user);
            authActionData.setValue(authAction);
        }
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
        AuthAction authAction = new AuthAction();
        authAction.setAction(isUnexpected ? Action.UNEXPECTED_LOGOUT : Action.LOGOUT);
        authAction.setUser(null);
        authActionData.setValue(authAction);
    }

    public void login(String username,String password){
        authService.login(username,password).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!response.isSuccessful()){
                    AuthAction authAction = new AuthAction();
                    authAction.setUser(null);
                    authAction.setAction(response.code() == 400 ? Action.LOGIN_INCORRECT
                            : Action.LOGIN_ERROR);
                    authActionData.setValue(authAction);
                }
                else{
                    // On recupere le token
                    AuthResponse authResponse = response.body();
                    HashMap<String,String> tokens = new HashMap<>();
                    tokens.put(ACCESS_TOKEN,authResponse.getPayload());
                    saveTokens(tokens);
                    // On demande les infos
                    getUserInfo();
                }
            }
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                authActionData.setValue(new AuthAction(Action.LOGIN_ERROR,null));
            }
        });
    }

    public void getUserInfo(){
        // On recupere les informations sur l'utilisateur connecté
        authService.checkLogin().enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!response.isSuccessful()){
                    // Pas authentifié : on recommence l'authentification
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.remove(ACCESS_TOKEN);
                    edit.apply();
                    authActionData.setValue(new AuthAction(Action.LOGIN_ERROR,null));
                }
                else{
                    // On recupere l'utilisateur
                    Gson gson = new Gson();
                    AuthResponse authResponse = response.body();
                    User userFromJson = gson.fromJson(authResponse.getPayload(),User.class);
                    if (userFromJson == null){
                        authActionData.setValue(new AuthAction(Action.LOGIN_ERROR,null));
                        return;
                    }
                    user = userFromJson;
                    saveUserLogged();
                    authActionData.setValue(new AuthAction(Action.LOGIN,userFromJson)); // Authentification reussie
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.remove(ACCESS_TOKEN);
                edit.apply();
                authActionData.setValue(new AuthAction(Action.LOGIN_ERROR,null));
            }
        });
    }

    public void unauthorizedAction(){
        authActionData.setValue(new AuthAction(Action.UNAUTHORIZED,null));
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

    public LiveData<AuthAction> getAuthActionData() {
        return authActionData;
    }
}
