package com.inpt.jibmaak.repository;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.AuthAction.Action;
import com.inpt.jibmaak.services.RetrofitAuthService;
import com.inpt.jibmaak.services.ServerResponse;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Classe chargée de l'authentification de l'utilisateur */
public class AuthManager {

    protected SharedPreferences sharedPreferences;
    protected RetrofitAuthService authService;
    protected MutableLiveData<User> userData;
    protected MutableLiveData<AuthAction> authActionData;
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String USER = "user";

    @Inject
    public AuthManager(RetrofitAuthService authService,SharedPreferences sharedPreferences){
        this.authService = authService;
        this.sharedPreferences = sharedPreferences;
        this.authActionData = new MutableLiveData<>();
        this.userData = new MutableLiveData<>();
        init();
    }

    public void init(){
        userData.setValue(getUserLogged());
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
        edit.putString(USER,gson.toJson(userData.getValue()));
        edit.apply();
    }

    public User getUserLogged(){
        Gson gson = new Gson();
        String json_user = sharedPreferences.getString(USER,null);
        if (json_user == null)
            return null;
        return gson.fromJson(json_user,User.class);
    }

    public void logout(){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(ACCESS_TOKEN);
        edit.remove(USER);
        edit.apply();
        AuthAction authAction = new AuthAction();
        authAction.setAction(Action.LOGOUT);
        authAction.setUser(null);
        authActionData.setValue(authAction);
        userData.setValue(null);
    }

    public void login(String username,String password){
        HashMap<String,String> credentials = new HashMap<>();
        credentials.put("email",username);
        credentials.put("password",password);
        authService.login(credentials).enqueue(new Callback<ServerResponse<JsonObject>>() {
            @Override
            public void onResponse(Call<ServerResponse<JsonObject>> call, Response<ServerResponse<JsonObject>> response) {
                if (!response.isSuccessful()){
                    AuthAction authAction = new AuthAction();
                    authAction.setUser(null);
                    authAction.setAction(response.code() == 400 ? Action.LOGIN_INCORRECT
                            : Action.LOGIN_ERROR);
                    authActionData.setValue(authAction);
                }
                else{
                    JsonObject body = response.body().getPayload();
                    Gson gson = new Gson();
                    // On recupere le token
                    String token = body.get("token").getAsString();
                    HashMap<String,String> tokens = new HashMap<>();
                    tokens.put(ACCESS_TOKEN,token);
                    saveTokens(tokens);
                    // On recupere l'utilisateur
                    User user = gson.fromJson(body,User.class);
                    authActionData.setValue(new AuthAction(Action.LOGIN,user)); // Authentification reussie
                    userData.setValue(user);
                    saveUserLogged();
                }
            }
            @Override
            public void onFailure(Call<ServerResponse<JsonObject>> call, Throwable t) {
                authActionData.setValue(new AuthAction(Action.LOGIN_ERROR,null));
            }
        });
    }

    public void register(HashMap<String, String> body) {
        authService.register(body).enqueue(new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                AuthAction authAction = new AuthAction();
                authAction.setUser(null);
                authAction.setAction(response.isSuccessful() ? Action.REGISTER : Action.REGISTER_ERROR);
                authActionData.setValue(authAction);
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                authActionData.setValue(new AuthAction(Action.REGISTER_ERROR,null));
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

    public LiveData<User> getUserData() {
        return userData;
    }

    public LiveData<AuthAction> getAuthActionData() {
        return authActionData;
    }


    public void updateUser(User userUpdated) {
        userData.setValue(userUpdated);
        saveUserLogged();
    }
}
