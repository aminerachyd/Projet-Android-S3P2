package com.inpt.jibmaak.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.validators.OfferValidation;
import com.inpt.jibmaak.viewmodels.SubmitOfferViewModel;

import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SubmitOfferActivity extends AuthenticateActivity implements ActivityManageDateDialog{

    // Activité pour proposer une offre
    // L'utilisateur (livreur) peut saisir les informations pour sa proposition d'offre
    protected SubmitOfferViewModel viewModel;
    protected EditText zone_lieu_depart;
    protected EditText zone_lieu_arrivee;
    protected TextView date_depart;
    protected TextView date_arrivee;
    protected EditText zone_poids;
    protected EditText zone_prix;
    protected Offer offer;
    protected Button bouton_recherche;
    protected DatePickerDialog dateDialog;
    protected TimePickerDialog timeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_offer);

        viewModel = new ViewModelProvider(this).get(SubmitOfferViewModel.class);
        zone_lieu_depart = findViewById(R.id.zone_lieu_depart_create);
        zone_lieu_arrivee = findViewById(R.id.zone_lieu_arrivee_create);
        date_depart = findViewById(R.id.date_depart_create);
        date_arrivee = findViewById(R.id.date_arrivee_create);
        zone_poids = findViewById(R.id.zone_poids_create);
        zone_prix = findViewById(R.id.zone_prix_create);
        bouton_recherche = findViewById(R.id.bouton_creer_offre);

        offer = viewModel.getOffer();
        String date_depart_texte = viewModel.getDate_depart_texte();
        String date_arrivee_texte = viewModel.getDate_arrivee_texte();
        if (date_depart_texte != null)
            date_depart.setText(date_depart_texte);
        if (date_arrivee_texte != null)
            date_arrivee.setText(date_arrivee_texte);

        date_depart.setOnClickListener(new DateListener(this) {
            @Override
            public Date getMinDate() {
                return new Date();
            }

            @Override
            public Date getMaxDate() {
                return offer.getDateArrivee();
            }

            @Override
            public Date getCurrentDate() {
                return offer.getDateDepart();
            }

            @Override
            public void setCurrentDate(Date date) {
                offer.setDateDepart(date);
                String texte = date == null ? getString(R.string.choisir_date)
                        : this.format.format(date);
                date_depart.setText(texte);
            }
        });
        date_arrivee.setOnClickListener(new DateListener(this) {
            @Override
            public Date getMinDate() {
                return offer.getDateDepart();
            }

            @Override
            public Date getMaxDate() {
                return null;
            }

            @Override
            public Date getCurrentDate() {
                return offer.getDateArrivee();
            }

            @Override
            public void setCurrentDate(Date date) {
                offer.setDateArrivee(date);
                String texte = date == null ? getString(R.string.choisir_date)
                        : this.format.format(date);
                date_arrivee.setText(texte);
            }
        });
        bouton_recherche.setOnClickListener(v -> creerOffre());

        viewModel.getOperationData().observe(this, stringResource -> {
            if (stringResource.isConsumed())
                return;
            Resource.Status status = stringResource.getStatus();
            stringResource.setConsumed(true);
            switch (status){
                case UNAUTHORIZED:
                    break;
                case OK:
                    Toast.makeText(SubmitOfferActivity.this,R.string.creer_offre_succes,
                            Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(SubmitOfferActivity.this,
                            R.string.error_server, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(SubmitOfferActivity.this,
                            R.string.error,Toast.LENGTH_SHORT).show();
                    break;

            }
        });

    }

    public void creerOffre() {
        if (user == null){
            askLogin();
            return;
        }
        boolean hasErrors = OfferValidation.validate(this,zone_lieu_depart,zone_lieu_arrivee,zone_poids,zone_prix);
        if (hasErrors)
            Toast.makeText(this,R.string.error_validation,Toast.LENGTH_SHORT).show();
        else if (offer.getDateDepart() == null || offer.getDateArrivee() == null){
            hasErrors = true;
            Toast.makeText(this,R.string.error_dates_vides,Toast.LENGTH_SHORT).show();
        }
        if (!hasErrors && prepareAction()){
            int valeur_poids = Integer.parseInt(zone_poids.getText().toString());
            int valeur_prix  = Integer.parseInt(zone_prix.getText().toString());
            String valeur_lieu_depart = zone_lieu_depart.getText().toString();
            String valeur_lieu_arrivee = zone_lieu_arrivee.getText().toString();
            offer.setPoidsDispo(valeur_poids);
            offer.setPrixKg(valeur_prix);
            offer.setLieuArrivee(valeur_lieu_arrivee);
            offer.setLieuDepart(valeur_lieu_depart);
            viewModel.createOffer(offer);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.setOffer(offer);
        viewModel.setDate_arrivee_texte(date_arrivee.getText().toString());
        viewModel.setDate_depart_texte(date_depart.getText().toString());
    }

    @Override
    public void onAskLoginFailed() {
        super.onAskLoginFailed();
    }

    @Override
    public void addDateDialog(DatePickerDialog dateDialog, TimePickerDialog timeDialog) {
        this.timeDialog = timeDialog;
        this.dateDialog = dateDialog;
    }

    @Override
    public void dismissDateDialog() {
        if (dateDialog != null)
            dateDialog.dismiss();
        if (timeDialog != null)
            timeDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDateDialog();
    }
}