package com.inpt.jibmaak.validators;

import android.content.Context;
import android.widget.EditText;

import com.inpt.jibmaak.R;

public class OfferValidation {
    public static boolean validate(Context context, EditText zone_lieu_depart,EditText zone_lieu_arrivee,
                                   EditText zone_poids, EditText zone_prix) {
        boolean hasErrors = false;
        try {
            Integer.parseInt(zone_poids.getText().toString());
        } catch (NumberFormatException e){
            zone_poids.setError(context.getString(R.string.error_poids_incorrect));
            hasErrors = true;
        }
        try {
            Integer.parseInt(zone_prix.getText().toString());
        } catch (NumberFormatException e){
            zone_prix.setError(context.getString(R.string.error_prix_incorrect));
            hasErrors = true;
        }
        String valeur_lieu_depart = zone_lieu_depart.getText().toString();
        String valeur_lieu_arrivee = zone_lieu_arrivee.getText().toString();
        if (valeur_lieu_depart.length() < 2){
            zone_lieu_depart.setError(context.getString(R.string.error_lieu_depart_court));
            hasErrors = true;
        }
        if (valeur_lieu_arrivee.length() < 2){
            zone_lieu_arrivee.setError(context.getString(R.string.error_lieu_arrivee_court));
            hasErrors = true;
        }
        return hasErrors;
    }
}
