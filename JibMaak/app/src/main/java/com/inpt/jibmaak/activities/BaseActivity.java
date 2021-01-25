package com.inpt.jibmaak.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.AuthAction;
import com.inpt.jibmaak.repository.AuthManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/** Superclasse des activités qui définit une interface pour la gestion de la connexion et des
 * différents evenements
 */
@AndroidEntryPoint
public abstract class BaseActivity extends AppCompatActivity {
    protected User user;
    protected boolean hasWaitingScreen;
    protected boolean hasConnection;
    protected ConnectivityManager.NetworkCallback networkCallback;
    @Inject
    protected AuthManager authManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authManager.getUserData().observe(this, user -> {
            boolean isUnexpected = hasWaitingScreen;
            removeWaitingScreen();
            if (user == null){
                BaseActivity.this.user = null;
                onLogout(isUnexpected);
            }
            else{
                BaseActivity.this.user = user;
                onLogin();
            }
        });
        // Un observateur qui surveille les actions de connexion
        // (connexion, deconnexion, etc)
        authManager.getAuthActionData().observe(this, authAction -> {
            removeWaitingScreen();
            String consommateur = getConsommateurName();
            AuthAction.Action action = authAction.getAction(consommateur);
            if (action != null)
                onAuthAction(action);
        });
        // Callback qui surveille l'etat de la connexion internet
        ConnectivityManager connectivityManager =  (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkCallback = new ConnectivityManager.NetworkCallback(){

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                hasConnection = networkCapabilities
                        .hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }

            @Override
            public void onLost(Network network) {
                hasConnection = false;
            }
        };
        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }


    /**
     * Methode appelée quand un évènement de connexion se produit
     * @param action l'action
     */
    public void onAuthAction(AuthAction.Action action){ }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // On retire le callback pour éviter les fuites de memoires
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    /** Crée un ecran d'attente
     *
     */
    public void makeWaitingScreen(){
        if (!hasWaitingScreen){
            // TODO : code
            hasWaitingScreen = true;
        }
    }

    /** Retire l'ecran d'attente
     *
     */
    public void removeWaitingScreen(){
        if (hasWaitingScreen){
            //TODO : code
            hasWaitingScreen = false;
        }
    }

    /**
     * Methode appelée à la connexion de l'utilisateur
     */
    public void onLogin(){}

    /**
     * Methode appelée à la deconnexion de l'utilisateur
     * @param isUnexpected est-ce que la connexion était volontaire
     */
    public void onLogout(boolean isUnexpected){}

    /** Methode appelée lorsqu'une erreur 401 est recu
     *
     */
    public void onUnauthorized(){}

    /**
     * Renvoie le nom de l'activité qui souhaite consommer l'evenement
     * Ceci permet d'éviter la consommation multiple d'un evenement
     * @return Le nom de l'activité consommatrice
     */
    public abstract String getConsommateurName();
}
