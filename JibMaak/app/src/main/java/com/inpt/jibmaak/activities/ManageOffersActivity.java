package com.inpt.jibmaak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;

public class ManageOffersActivity extends AuthenticateActivity {
    protected Button bouton_voir_offres;
    protected Button bouton_ajouter_offres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_offers);
        bouton_ajouter_offres = findViewById(R.id.bouton_ajouter_offre);
        bouton_voir_offres = findViewById(R.id.bouton_voir_offres);

        bouton_voir_offres.setOnClickListener(v -> {
            if (user == null){
                askLogin();
                return;
            }
            // TODO : filtre utilisateur
            OfferSearchCriteria criteria = new OfferSearchCriteria();
            Pagination page = new Pagination(0,3);

            Intent intent = new Intent(ManageOffersActivity.this,
                    SearchOfferResultActivity.class);
            intent.putExtra(SearchOfferResultActivity.EXTRA_CRITERIA, criteria);
            intent.putExtra(SearchOfferResultActivity.EXTRA_PAGINATION, page);
            startActivity(intent);
        });

        bouton_ajouter_offres.setOnClickListener(v -> {
            if (user == null){
                askLogin();
                return;
            }
            Intent intent = new Intent(ManageOffersActivity.this,
                    SubmitOfferActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onLogout(boolean isUnexpected) {
        super.onLogout(isUnexpected);
        if (!isUnexpected)
            finish();
    }

    @Override
    public void onAskLoginFailed() {
        super.onAskLoginFailed();
        finish();
    }
}