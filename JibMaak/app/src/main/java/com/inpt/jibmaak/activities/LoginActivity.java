package com.inpt.jibmaak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.repository.AuthManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends BaseActivity {

    // Activité de login
    // L'utilisateur doit se connecter avec son mail ou son username
    // Si l'utilisateur n'a pas de compte il doit s'inscrire
    protected EditText mail;
    protected EditText mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // On recupere les vues
        Button inscrire = findViewById(R.id.bouton_inscrire);
        Button connexion = findViewById(R.id.bouton_connecter);
        Button continuer = findViewById(R.id.bouton_continuer);
        mail = findViewById(R.id.zone_mail);
        mdp = findViewById(R.id.zone_mdp);

        // On definit les callback
        inscrire.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
            startActivity(intent);
        });

        connexion.setOnClickListener(v -> {
            if (!hasConnection){
                Toast.makeText(LoginActivity.this,R.string.no_connection,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String saisie_mail = mail.getText().toString();
            String saisie_mdp = mdp.getText().toString();
            if (saisie_mail.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(saisie_mail).matches())
                Toast.makeText(LoginActivity.this,R.string.email_incorrect,
                        Toast.LENGTH_SHORT).show();
            else if (saisie_mdp.length() == 0)
                Toast.makeText(LoginActivity.this,R.string.mdp_vide,
                        Toast.LENGTH_SHORT).show();
            else{
                authManager.login(saisie_mail,saisie_mdp);
            }
        });

        continuer.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), MenuActivity.class);
            startActivity(intent);
            finish();
        });
        authManager.getAuthActionData().observe(this, authAction -> {
            switch (authAction.getAction()){
                case LOGIN_INCORRECT:
                    Toast.makeText(LoginActivity.this,R.string.error_credentials,Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_ERROR:
                    Toast.makeText(LoginActivity.this,R.string.error,Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    public void onLogin() {
        Toast.makeText(LoginActivity.this,getString(R.string.salutations,user.getPrenom()),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLogout(boolean isUnexpected) {
        // Rien à faire ici
    }

    @Override
    public void onUnauthorized() {
        // Rien à faire ici
    }
}