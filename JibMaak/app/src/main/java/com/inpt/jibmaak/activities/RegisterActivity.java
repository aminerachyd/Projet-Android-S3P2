package com.inpt.jibmaak.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.repository.AuthAction;

import java.util.HashMap;

import static com.inpt.jibmaak.repository.AuthAction.Action.REGISTER;
import static com.inpt.jibmaak.repository.AuthAction.Action.REGISTER_ERROR;

public class RegisterActivity extends BaseActivity {

    // Activité d'inscription
    // L'utilisateur peut créer un compte ici
    // Il peut le créer avec un mail et ses informations
    // Inscription avec Facebook et Google à voir
    protected EditText zone_nom;
    protected EditText zone_prenom;
    protected EditText zone_mail;
    protected EditText zone_telephone;
    protected EditText zone_mdp;
    protected EditText zone_mdp_conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // On recupere les vues
        zone_nom = findViewById(R.id.zone_nom);
        zone_prenom = findViewById(R.id.zone_prenom);
        zone_mail = findViewById(R.id.zone_mail_register);
        zone_telephone = findViewById(R.id.zone_phone);
        zone_mdp = findViewById(R.id.zone_mdp_register);
        zone_mdp_conf = findViewById(R.id.zone_mdp_conf);
        Button inscrire = findViewById(R.id.bouton_inscrire);

        inscrire.setOnClickListener(v -> register());
    }

    public void register(){
        String nom = zone_nom.getText().toString();
        if (nom.length() < 1){
            Toast.makeText(this,R.string.error_register_nom,Toast.LENGTH_SHORT).show();
            return;
        }
        String prenom = zone_prenom.getText().toString();
        if (prenom.length() < 1){
            Toast.makeText(this,R.string.error_register_prenom,Toast.LENGTH_SHORT).show();
            return;
        }
        String telephone = zone_telephone.getText().toString();
        if (!telephone.matches("^0[0-9]{9}$")){
            Toast.makeText(this,R.string.error_register_telephone,Toast.LENGTH_SHORT).show();
            return;
        }
        String email = zone_mail.getText().toString();
        if (email.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,R.string.error_register_email,Toast.LENGTH_SHORT).show();
            return;
        }
        String mdp = zone_mdp.getText().toString();
        String mdp_conf = zone_mdp_conf.getText().toString();

        if (mdp.length() < 6){
            Toast.makeText(this,R.string.error_register_mdp,Toast.LENGTH_SHORT).show();
        }
        else if (!mdp.equals(mdp_conf)){
            Toast.makeText(this,R.string.error_register_mdp_conf,Toast.LENGTH_SHORT).show();
        }
        else if (prepareAction()){
            // Les champs sont corrects : on peut lancer la requete
            HashMap<String,String> body = new HashMap<>();
            body.put("nom",nom);
            body.put("prenom",prenom);
            body.put("email",email);
            body.put("telephone",telephone);
            body.put("password",mdp);
            makeWaitingScreen();
            authManager.register(body);
        }
    }

    @Override
    public void onLogin() {
        // On ferme l'activité
        finish();
    }

    @Override
    public void onAuthAction(AuthAction.Action action) {
        super.onAuthAction(action);
        if (action == REGISTER){
            Toast.makeText(RegisterActivity.this,
                    R.string.register_success,Toast.LENGTH_LONG).show();
            finish();
        }
        else if (action == REGISTER_ERROR){
            Toast.makeText(RegisterActivity.this,
                    R.string.register_error,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String getConsommateurName() {
        return "RegisterActivity";
    }
}