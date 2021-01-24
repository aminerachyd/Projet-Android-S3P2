package com.inpt.jibmaak.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.AuthManager;

/** Superclasse des activités qui définit une interface pour la gestion de la connexion et des
 * différents evenements
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected User user;
    protected boolean hasWaitingScreen;
    protected boolean hasConnection;
    protected ConnectivityManager.NetworkCallback networkCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthManager authManager = getAuthManager();
        // Un observateur qui surveille les actions de connexion
        // (connexion, deconnexion, etc)
        authManager.getAuthActionData().observe(this, authAction -> {
            removeWaitingScreen();
            switch (authAction.getAction()){
                case LOGOUT:
                    user = null;
                    onLogout(false);
                    break;
                case LOGIN:
                    user = authAction.getUser();
                    onLogin();
                    break;
                case UNEXPECTED_LOGOUT:
                    user = null;
                    // On affiche un message d'erreur
                    Toast.makeText(BaseActivity.this, R.string.deconnexion_surprise,
                            Toast.LENGTH_LONG).show();
                    onLogout(true);
                    break;
                case UNAUTHORIZED:
                    onUnauthorized();
                    break;
            }
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
    public abstract void onLogin();

    /**
     * Methode appelée à la deconnexion de l'utilisateur
     * @param isUnexpected est-ce que la connexion était volontaire
     */
    public abstract void onLogout(boolean isUnexpected);

    /** Methode appelée lorsqu'une erreur 401 est recu
     *
     */
    public abstract void onUnauthorized();

    /**
     * Methode qui fournit le authManager de l'activité
     * @return L'attribut authManager du viewmodel ou autre
     */
    public abstract AuthManager getAuthManager();
}
