package com.inpt.jibmaak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.inpt.jibmaak.R;

public class SubmitOfferActivity extends AppCompatActivity {

    // Activité pour proposer une offre
    // L'utilisateur (livreur) peut saisir les informations pour sa proposition d'offre

    Button proposerOffre, retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_offer);

        // On map les différents boutons a la vue
        proposerOffre = findViewById(R.id.card_gerer_offre);
        retour = findViewById(R.id.retour);

        retour.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), MenuActivity.class);

            startActivity(intent);
        });

        proposerOffre.setOnClickListener(v -> {
            // TODO Doit ajouter l'offre à une base de données

            Toast.makeText(getBaseContext(),"Votre offre a été ajoutée",Toast.LENGTH_LONG).show();
        });
    }
}