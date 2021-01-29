package com.inpt.jibmaak.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.inpt.jibmaak.R;
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
    protected AlertDialog logoutDialog;
    protected AlertDialog waitingDialog;
    @Inject
    protected AuthManager authManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Un observateur qui surveille l'utilisateur actuellement connecté afin de changer
        // l'interface graphique
        authManager.getUserData().observe(this, user -> {
            removeWaitingScreen();
            if (user == null){
                BaseActivity.this.user = null;
                updateUiNoUser();
            }
            else{
                BaseActivity.this.user = user;
                updateUiWithUser();
            }
        });
        // Un observateur qui surveille les actions de connexion
        // (connexion, deconnexion, etc)
        authManager.getAuthActionData().observe(this, authAction -> {
            removeWaitingScreen();
            AuthAction.Action action = authAction.getAction();
            if (action != null){
                if (action.equals(AuthAction.Action.LOGOUT))
                    onLogout(false);
                else if (action.equals(AuthAction.Action.LOGOUT_UNEXPECTED))
                    onLogout(true);
                else
                    onAuthAction(action);
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
        if (logoutDialog != null)
            logoutDialog.dismiss();
        if (waitingDialog != null)
            waitingDialog.dismiss();
    }

    /** Crée un ecran d'attente
     *
     */
    public void makeWaitingScreen(){
        if (!hasWaitingScreen){
            ProgressBar bar = new ProgressBar(this);
            bar.setIndeterminate(true);
            waitingDialog = new AlertDialog.Builder(this).setView(bar)
                    .setCancelable(false).create();
            waitingDialog.show();
            hasWaitingScreen = true;
        }
    }

    /** Retire l'ecran d'attente
     *
     */
    public void removeWaitingScreen(){
        if (hasWaitingScreen){
            waitingDialog.setCancelable(true);
            waitingDialog.dismiss();
            hasWaitingScreen = false;
        }
    }

    /**
     * Methode appelée quand un évènement de connexion autre que la deconnexion se produit
     * @param action l'action
     */
    public void onAuthAction(AuthAction.Action action){ }

    /** Vérifie que la connexion est disponible et dans le cas écheant affiche l'ecran d'attente
     * @return true si la connexion est disponible
     */
    public boolean prepareAction(){
        if (!hasConnection)
            Toast.makeText(this, R.string.no_connection,
                    Toast.LENGTH_SHORT).show();
        else
            makeWaitingScreen();
        return hasConnection;
    }

    /**
     * Methode appelée pour changer l'interface graphique afin qu'elle correspond à celle du
     * mode avec utilisateur
     */
    public void updateUiWithUser(){}

    /**
     * Methode appelée pour changer l'interface graphique afin qu'elle correspond à celle du
     * mode visiteur
     */
    public void updateUiNoUser(){}

    /** Methode appelée lorsqu'une erreur 401 est recu
     *
     */
    public void onUnauthorized(){}

    /**
     * Methode appelée lorsque l'utilisateur est deconnecté
     * @param isUnexpected Si la déconnexion était involontaire
     */
    public void onLogout(boolean isUnexpected){
        if (!isUnexpected){
            Toast.makeText(this,R.string.deconnexion_volontaire,
                    Toast.LENGTH_LONG).show();
        }
        else{
            logoutDialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.deconnexion_surprise)
                    .setNeutralButton(R.string.ok, (dialog, which) -> { })
                    .create();
            logoutDialog.show();
        }
    }
}
