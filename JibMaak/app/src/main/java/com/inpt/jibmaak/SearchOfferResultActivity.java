package com.inpt.jibmaak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SearchOfferResultActivity extends AppCompatActivity {

    // Activité de résultat de la recherche
    // Est affiché le résutlat de la recherche effectuée par l'utilisateur
    // La liste de résultat doit correspondre au mieux aux besoins de l'utilisateur
    // L'ordre est important (plus haut dans la liste = plus correspondant)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_offer_result);
    }
}