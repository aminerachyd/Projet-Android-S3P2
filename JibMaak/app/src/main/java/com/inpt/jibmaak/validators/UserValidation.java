package com.inpt.jibmaak.validators;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;

import com.inpt.jibmaak.R;

public class UserValidation {
    public static boolean validate(Context context, EditText zone_nom, EditText zone_prenom, EditText zone_telephone,
                                   EditText zone_mail, EditText zone_mdp, EditText zone_mdp_conf) {
        String nom = zone_nom == null ? null :  zone_nom.getText().toString();
        String prenom = zone_prenom == null ? null : zone_prenom.getText().toString();
        String telephone = zone_telephone == null ? null : zone_telephone.getText().toString();
        String mail = zone_telephone == null ? null : zone_mail.getText().toString();
        String mdp = zone_mdp == null ? null : zone_mdp.getText().toString();
        String mdp_conf = zone_mdp_conf == null ? null : zone_mdp_conf.getText().toString();
        boolean hasErrors = false;
        if (nom != null && nom.length() < 1){
            zone_nom.setError(context.getString(R.string.error_user_validate_nom));
            hasErrors = true;
        }
        if (prenom != null && prenom.length() < 1){
            zone_prenom.setError(context.getString(R.string.error_user_validate_prenom));
            hasErrors = true;
        }

        if (telephone != null && !telephone.matches("^0[0-9]{9}$")){
            zone_telephone.setError(context.getString(R.string.error_user_validate_telephone));
            hasErrors = true;
        }

        if (mail != null && mail.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            zone_mail.setError(context.getString(R.string.error_user_validate_email));
            hasErrors = true;
        }
        if (mdp != null){
            if (mdp.length() < 6){
                zone_mdp.setError(context.getString(R.string.error_user_validate_mdp));
                hasErrors = true;
            }
            else if (mdp_conf != null && !mdp.equals(mdp_conf)){
                zone_mdp_conf.setError(context.getString(R.string.error_user_validate_mdp_conf));
                hasErrors = true;
            }
        }
        return hasErrors;
    }
}
