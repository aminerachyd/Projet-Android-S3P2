package com.inpt.jibmaak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.inpt.jibmaak.R;

public class MenuActivity extends AuthenticateActivity {

    // Le menu principal

    protected CardView card_compte;
    protected CardView card_gerer_offre;
    protected CardView card_chercher_offre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // On recupere les vues
        card_compte = findViewById(R.id.card_compte);
        card_chercher_offre= findViewById(R.id.card_chercher_offre);
        card_gerer_offre = findViewById(R.id.card_gerer_offre);

        card_chercher_offre.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this,SearchOfferActivity.class);
            startActivity(intent);
        });

        card_gerer_offre.setOnClickListener(v -> {
            if (user == null){
                askLogin();
            }
            else{
                // TODO : activité gerer offre
            }
        });

        card_compte.setOnClickListener(v -> {
            if (user == null){
                askLogin();
            }
            else{
                Intent intent = new Intent(MenuActivity.this,ManageAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Si l'utilisateur n'est pas connecté on affiche la premiere activité
        if (user == null){
            Intent intent = new Intent(this,FirstActivity.class);
            startActivity(intent);
        }
        // On termine l'activité
        finish();
    }

    @Override
    public void onLogin() {
        // On affiche les cartes gerer compte et gerer offres
        card_compte.setVisibility(View.VISIBLE);
        card_gerer_offre.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLogout(boolean isUnexpected) {
        // On masque les cartes gerer compte et gerer offres
        card_compte.setVisibility(View.GONE);
        card_gerer_offre.setVisibility(View.GONE);
    }

    @Override
    public String getConsommateurName() {
        return "MenuActivity";
    }
}