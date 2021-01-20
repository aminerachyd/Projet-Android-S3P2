package com.inpt.jibmaak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SubmitOfferActivity extends AppCompatActivity {

    // Activité pour proposer une offre
    // L'utilisateur (livreur) peut saisir les informations pour sa proposition d'offre

    Button proposerOffre, retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_offer);

        // On map les différents boutons a la vue
        proposerOffre = findViewById(R.id.proposer_offre);
        retour = findViewById(R.id.retour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MenuActivity.class);

                startActivity(intent);
            }
        });

        proposerOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Doit ajouter l'offre à une base de données

                Toast.makeText(getBaseContext(),"Votre offre a été ajoutée",Toast.LENGTH_LONG).show();
            }
        });
    }
}