package com.inpt.jibmaak.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.inpt.jibmaak.R;

public class SearchOfferResultActivity extends AppCompatActivity {

    // Activité de résultat de la recherche
    // Est affiché le résutlat de la recherche effectuée par l'utilisateur
    // La liste de résultat doit correspondre au mieux aux besoins de l'utilisateur
    // L'ordre est important (plus haut dans la liste = plus correspondant)

    LinearLayout offre1;
    Button retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_offer_result);

        // On map les différents boutons a la vue
        offre1 = findViewById(R.id.offre1);
        retour = findViewById(R.id.retour);


        retour.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), SearchOfferActivity.class
            );

            startActivity(intent);
        });

        offre1.setOnClickListener(v -> {
            // TODO Doit passer les informations de l'offre
            Intent intent = new Intent(getBaseContext(), SelectOfferActivity.class
            );

            startActivity(intent);
        });
    }
}