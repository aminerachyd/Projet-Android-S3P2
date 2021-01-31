package com.inpt.jibmaak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.repository.AuthAction;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends BaseActivity {

    // Activité de login
    // L'utilisateur doit se connecter avec son mail et son mot de passe
    // Si l'utilisateur n'a pas de compte il peut s'inscrire
    protected EditText mail;
    protected EditText mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // On recupere les vues
        Button inscrire = findViewById(R.id.bouton_inscrire);
        Button connexion = findViewById(R.id.bouton_connecter);
        mail = findViewById(R.id.zone_mail);
        mdp = findViewById(R.id.zone_mdp);

        // On definit les callback
        inscrire.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
            startActivity(intent);
        });

        connexion.setOnClickListener(v -> {
            String saisie_mail = mail.getText().toString();
            String saisie_mdp = mdp.getText().toString();
            if (saisie_mail.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(saisie_mail).matches())
                Toast.makeText(LoginActivity.this,R.string.erreur_connexion_email,
                        Toast.LENGTH_SHORT).show();
            else if (saisie_mdp.length() == 0)
                Toast.makeText(LoginActivity.this,R.string.erreur_connexion_mdp,
                        Toast.LENGTH_SHORT).show();
            else if (prepareAction()){
                authManager.login(saisie_mail,saisie_mdp);
            }
        });
    }

    @Override
    public void updateUiWithUser() {
        super.updateUiWithUser();
        Toast.makeText(this,getString(R.string.salutations,user.getPrenom()),Toast.LENGTH_SHORT).show();
        setResult(AuthenticateActivity.RESULT_LOGIN);
        finish();
    }

    @Override
    public void onAuthAction(AuthAction.Action action) {
        super.onAuthAction(action);
        switch (action){
            case LOGIN_INCORRECT:
                Toast.makeText(this,R.string.erreur_connexion_identifiants_incorrect,Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
                Toast.makeText(this,R.string.erreur,Toast.LENGTH_SHORT).show();
                break;
        }
    }
}