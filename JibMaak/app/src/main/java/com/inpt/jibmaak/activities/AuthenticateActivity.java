package com.inpt.jibmaak.activities;

import android.app.AlertDialog;
import android.content.Intent;

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
    public static final int REQUEST_CODE_REGISTER = 2;

    /** Demarre le processus de connexion */
    public void askLogin(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage(R.string.demande_connexion)
                .setPositiveButton(R.string.se_connecter, (dialog, which) -> {
                    Intent intent = new Intent(AuthenticateActivity.this,LoginActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_LOGIN);
                })
                .setNegativeButton(R.string.annuler, (dialog, which) -> onAskLoginFailed())
                .setOnCancelListener(dialog -> onAskLoginFailed()).create().show();
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

    /**
     * Définit l'action à accomplir si la demande de connexion a reussie
     */
    public abstract void onAskLoginSuccess();

    /**
     * Définit l'action à accomplir si la demande de connexion a échouée
     */
    public abstract void onAskLoginFailed();
}
