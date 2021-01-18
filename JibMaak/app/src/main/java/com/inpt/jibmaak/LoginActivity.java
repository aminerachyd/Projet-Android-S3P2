package com.inpt.jibmaak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    // Activité de login
    // L'utilisateur doit se connecter avec son mail ou son username
    // Si l'utilisateur n'a pas de compte il doit s'inscrire

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}