package com.inpt.jibmaak.validators;

import android.widget.AutoCompleteTextView;

import com.inpt.jibmaak.R;

import java.util.Arrays;

public class VilleValidator implements AutoCompleteTextView.Validator {
    protected String[] villes;
    protected AutoCompleteTextView view;
    public VilleValidator(String[] villes,AutoCompleteTextView view){
        this.villes = villes;
        this.view = view;
    }
    @Override
    public boolean isValid(CharSequence text) {
        String saisie = text.toString();
        return Arrays.asList(villes).contains(saisie);
    }

    @Override
    public CharSequence fixText(CharSequence invalidText) {
        String saisie = invalidText.toString().trim().toUpperCase();
        boolean isUpperCorrect = Arrays.asList(villes).contains(saisie);
        if (!isUpperCorrect)
            view.setError(view.getContext().getString(R.string.erreur_offre_ville_inconnu));
        return isUpperCorrect ? saisie : invalidText;
    }
}
