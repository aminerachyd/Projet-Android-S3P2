package com.inpt.jibmaak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.inpt.jibmaak.R;

/** Activité d'accueil */
public class FirstActivity extends AuthenticateActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Button connexion = findViewById(R.id.bouton_lancer_login);
        Button continuer = findViewById(R.id.bouton_continuer);

        connexion.setOnClickListener(v -> {
            // On lance le processus de connexion
            Intent intent = new Intent(FirstActivity.this,LoginActivity.class);
            startActivityForResult(intent,REQUEST_CODE_LOGIN);
        });
        continuer.setOnClickListener(v -> {
            // On passe au menu
            Intent intent = new Intent(getBaseContext(), MenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onLogin() {
        // On passe au menu
        Toast.makeText(this,getString(R.string.salutations,user.getPrenom()),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLogout(boolean isUnexpected) {
        // Rien à faire
    }

    @Override
    public void onUnauthorized() {
        // Rien à faire
    }

    @Override
    public void onAskLoginSuccess() {
        // On passe au menu
        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAskLoginFailed() {
        // Rien
    }
}
