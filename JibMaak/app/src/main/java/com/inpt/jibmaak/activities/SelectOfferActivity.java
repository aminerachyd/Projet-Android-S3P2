package com.inpt.jibmaak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.inpt.jibmaak.R;

public class SelectOfferActivity extends AppCompatActivity {

    // Activité de selection de l'offre
    // L'utilisateur affiche les informations d'une offre et décide ou pas de la choisir

    Button selectionnerOffre, retourListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_offer);

        // On map les différents boutons a la vue
        selectionnerOffre = findViewById(R.id.selectionner_offre);
        retourListe = findViewById(R.id.retour_liste);

        retourListe.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), SearchOfferResultActivity.class);

            startActivity(intent);
        });

        selectionnerOffre.setOnClickListener(v -> {
            // TODO Une activité pour confirmer la selection ?
            Toast.makeText(getBaseContext(), "Vous avez selectionné cette offre", Toast.LENGTH_LONG).show();
        });

    }
}