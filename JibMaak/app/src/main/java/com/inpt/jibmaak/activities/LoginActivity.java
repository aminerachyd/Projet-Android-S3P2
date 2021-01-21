package com.inpt.jibmaak.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.repository.RetrofitOfferService;

import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    // Activité de login
    // L'utilisateur doit se connecter avec son mail ou son username
    // Si l'utilisateur n'a pas de compte il doit s'inscrire

    Button seConnecter, sInscrire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Retrofit retrofit = new Retrofit.Builder().build();
        RetrofitOfferService retro = retrofit.create(RetrofitOfferService.class);

        // On map les différents boutons a la vue
        sInscrire = findViewById(R.id.s_inscrire);
        seConnecter = findViewById(R.id.se_connecter);

        sInscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);

                startActivity(intent);
            }
        });

        seConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Un système d'authentification
                Intent intent = new Intent(getBaseContext(), MenuActivity.class);

                startActivity(intent);
            }
        });
    }
}