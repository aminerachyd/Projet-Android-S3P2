package com.inpt.jibmaak.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inpt.jibmaak.R;

public class RegisterActivity extends AppCompatActivity {

    // Activité d'inscription
    // L'utilisateur peut créer un compte ici
    // Il peut le créer avec un mail, username et mot de passe
    // Inscription avec Facebook et Google à voir

    Button sInscrire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // On map les différents boutons a la vue
        sInscrire = findViewById(R.id.s_inscrire);

        sInscrire.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);

            Toast.makeText(getBaseContext(), "Votre compte a été créé", Toast.LENGTH_LONG).show();

            startActivity(intent);
        });
    }
}