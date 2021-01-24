package com.inpt.jibmaak.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.inpt.jibmaak.R;

public class MenuActivity extends AppCompatActivity {

    // Le menu principal
    // Devra être disponible après le login

    CardView aPropos, chercherOffre, proposerOffre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // On map les différents boutons a la vue
        aPropos = findViewById(R.id.a_propos);
        chercherOffre = findViewById(R.id.chercher_offre);
        proposerOffre = findViewById(R.id.proposer_offre);

        // Passage à l'activité de recherche d'offre
        chercherOffre.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), SearchOfferActivity.class
            );

            startActivity(intent);
        });

        // Passage à l'activité de proposition d'offre
        proposerOffre.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), SubmitOfferActivity.class
            );

            startActivity(intent);
        });

        // Passage à l'activité A propos
        aPropos.setOnClickListener(v -> {
            // TODO Doit créer une activité a propos

            Toast.makeText(getBaseContext(), "A propos pas disponbile pour l'instant", Toast.LENGTH_LONG).show();
        });
    }
}