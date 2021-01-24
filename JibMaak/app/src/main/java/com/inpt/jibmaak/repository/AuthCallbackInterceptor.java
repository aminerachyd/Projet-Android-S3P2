package com.inpt.jibmaak.repository;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Callback chargé de gérer les erreurs d'authentification */
public abstract class AuthCallbackInterceptor<T> implements Callback<T> {
    protected AuthManager authManager;
    public AuthCallbackInterceptor(AuthManager authManager){
        this.authManager = authManager;
    }
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!response.isSuccessful()){
            // On vérifie si c'est une erreur d'authentification
            if (response.code() == 401)
                authManager.unauthorizedAction();
            else if (response.code() == 400){
                try {
                    String error = response.errorBody().string();
                    // Token invalide : on est deconnectés
                    if (error.contains("Token invalide, veuillez vous reconnecter"))
                        authManager.logout(true);
                } catch (IOException e) {
                    // On laisse passer
                    onGetResponse(call,response);
                }
            }
            else if (response.code() == 500){
                // Erreur serveur : on laisse passer
                onGetResponse(call,response);
            }
        }
        else{
            // On continue normalement
            onGetResponse(call,response);
        }
    }
    /** Definit l'action à faire avec la réponse */
    public abstract void onGetResponse(Call<T> call, Response<T> response);
}
