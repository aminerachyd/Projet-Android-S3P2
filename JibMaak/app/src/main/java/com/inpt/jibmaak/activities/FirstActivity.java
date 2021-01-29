package com.inpt.jibmaak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
    public void updateUiWithUser() {
        // On passe directement au menu
        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
