package com.inpt.jibmaak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    // Activité d'inscription
    // L'utilisateur peut créer un compte ici
    // Il peut le créer avec un mail, username et mot de passe
    // Inscription avec Facebook et Google à voir

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}