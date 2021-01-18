package com.inpt.jibmaak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchOfferActivity extends AppCompatActivity {

    // Activité de recherche d'offre
    // L'utilisateur saisi les infos pour trouver l'offre qui le convient

    Button chercherOffre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_offer);

        // On map les différents boutons a la vue
        chercherOffre = findViewById(R.id.chercher_offre);

        chercherOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Doit effectuer la recherche
                Intent intent = new Intent(getBaseContext(), SearchOfferResultActivity.class
                );

                startActivity(intent);
            }
        });
    }
}