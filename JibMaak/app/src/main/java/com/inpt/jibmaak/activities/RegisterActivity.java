package com.inpt.jibmaak.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.repository.AuthAction;
import com.inpt.jibmaak.validators.UserValidation;

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
        boolean hasErrors = UserValidation.validate(this,zone_nom,zone_prenom,
                zone_telephone,zone_mail,zone_mdp,zone_mdp_conf);
        if (hasErrors)
            Toast.makeText(this,R.string.error_validation,Toast.LENGTH_SHORT).show();
        else if (prepareAction()){
            String nom = zone_nom.getText().toString().trim();
            String prenom = zone_prenom.getText().toString().trim();
            String telephone = zone_telephone.getText().toString();
            String mail = zone_mail.getText().toString();
            String mdp = zone_mdp.getText().toString();
            // Les champs sont corrects : on peut lancer la requete
            HashMap<String,String> body = new HashMap<>();
            body.put("nom",nom);
            body.put("prenom",prenom);
            body.put("email",mail);
            body.put("telephone",telephone);
            body.put("password",mdp);
            authManager.register(body);
        }
    }

    @Override
    public void updateUiWithUser() {
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
}