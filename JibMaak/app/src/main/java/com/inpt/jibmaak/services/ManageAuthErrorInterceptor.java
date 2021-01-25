package com.inpt.jibmaak.services;

import com.inpt.jibmaak.repository.AuthManager;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;

/** Intercepteur chargé de notifier les erreurs d'authentification */
public class ManageAuthErrorInterceptor implements Interceptor {
    protected AuthManager authManager;
    @Inject
    public ManageAuthErrorInterceptor(AuthManager authManager){
        this.authManager = authManager;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (!response.isSuccessful()) {
            try {
                // On vérifie si c'est une erreur d'authentification
                if (response.code() == 401)
                    authManager.unauthorizedAction();
                else if (response.code() == 400) {
                    String error = response.peekBody(1024).string();
                    // Token invalide : on est deconnectés
                    if (error.contains("Token invalide, veuillez vous reconnecter"))
                        authManager.logout();
                }
            } catch (Throwable ignored) {}
        }
        return response;
    }
}
