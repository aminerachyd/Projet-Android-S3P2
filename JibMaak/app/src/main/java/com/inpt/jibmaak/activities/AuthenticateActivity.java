package com.inpt.jibmaak.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.inpt.jibmaak.R;

/** Superclasse des activités pouvant demander l'authentification */
public abstract class AuthenticateActivity extends BaseActivity {
    /**
     * L'utilisateur s'est correctement connecté
     */
    public static final int RESULT_LOGIN = 1;
    /**
     * L'utilisateur ne s'est pas connecté (abandon ou erreur)
     */
    public static final int RESULT_NO_LOGIN = 2;

    /**
     * Code pour la demande d'authentification
     */
    public static final int REQUEST_CODE_LOGIN = 1;

    /**
     * Code pour la demande d'inscription
     */
    public static final int MENU_LOGIN_LOGOUT_GROUP = 0;
    public static final int MENU_LOGIN_LOGOUT_ITEM = 0;

    protected AlertDialog loginDialog;

    /** Demarre le processus de connexion */
    public void askLogin(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        loginDialog = adb.setMessage(R.string.demande_connexion)
                .setPositiveButton(R.string.se_connecter, (dialog, which) -> {
                    Intent intent = new Intent(AuthenticateActivity.this,LoginActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_LOGIN);
                })
                .setNegativeButton(R.string.annuler, (dialog, which) -> onAskLoginFailed())
                .setOnCancelListener(dialog -> onAskLoginFailed()).create();
        loginDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN){
            if (resultCode == RESULT_LOGIN) {// L'utilisateur s'est connecté
                onAskLoginSuccess();
            } else {// On considère que l'utilisateur ne s'est pas connecté
                onAskLoginFailed();
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        String titre_item = user == null ? getString(R.string.se_connecter)
                : getString(R.string.se_deconnecter);
        menu.add(MENU_LOGIN_LOGOUT_GROUP,MENU_LOGIN_LOGOUT_ITEM, Menu.NONE,titre_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == MENU_LOGIN_LOGOUT_ITEM){
            if (user != null){
                authManager.logout();
                Toast.makeText(this,R.string.deconnexion_volontaire,Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent(AuthenticateActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }
        return true;
    }

    /**
     * Définit l'action à accomplir si la demande de connexion a reussie
     */
    public void onAskLoginSuccess(){}

    /**
     * Définit l'action à accomplir si la demande de connexion a échouée
     */
    public void onAskLoginFailed(){}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loginDialog != null)
            loginDialog.dismiss();
    }
}
