package com.inpt.jibmaak.activities;

import android.app.AlertDialog;
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
import com.inpt.jibmaak.viewmodels.UpdateOfferViewModel;

import java.text.DateFormat;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

import static com.inpt.jibmaak.repository.Resource.Operation.DELETE;
import static com.inpt.jibmaak.repository.Resource.Operation.UPDATE;
import static com.inpt.jibmaak.repository.Resource.Status.OK;
import static com.inpt.jibmaak.repository.Resource.Status.UNAUTHORIZED;

@AndroidEntryPoint
public class UpdateOfferActivity extends AuthenticateActivity implements ActivityManageDateDialog{
    protected UpdateOfferViewModel viewModel;
    protected EditText zone_lieu_depart;
    protected EditText zone_lieu_arrivee;
    protected TextView date_depart;
    protected TextView date_arrivee;
    protected EditText zone_poids;
    protected EditText zone_prix;
    protected Button bouton_maj;
    protected Button bouton_delete;
    protected Offer offer;
    protected DatePickerDialog dateDialog;
    protected TimePickerDialog timeDialog;
    protected AlertDialog deleteDialog;

    public static final String EXTRA_OFFER = "com.inpt.jibmaak.EXTRA_OFFER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_offer);

        viewModel = new ViewModelProvider(this).get(UpdateOfferViewModel.class);
        zone_lieu_depart = findViewById(R.id.zone_lieu_depart_update);
        zone_lieu_arrivee = findViewById(R.id.zone_lieu_arrivee_update);
        date_depart = findViewById(R.id.date_depart_update);
        date_arrivee = findViewById(R.id.date_arrivee_update);
        zone_poids = findViewById(R.id.zone_poids_update);
        zone_prix = findViewById(R.id.zone_prix_update);
        bouton_maj = findViewById(R.id.bouton_maj_offre);
        bouton_delete = findViewById(R.id.bouton_supprimer_offre);

        if (savedInstanceState == null){
            // On recupere les informations depuis l'intent
            // viewModel.setOffer
            Offer offer = getIntent().getParcelableExtra(EXTRA_OFFER);
            // Si l'offre est null on arrete
            if (offer == null)
                finish();
            // On recupere les champs
            zone_lieu_depart.setText(offer.getLieuDepart());
            zone_lieu_arrivee.setText(offer.getLieuArrivee());
            zone_poids.setText(String.valueOf(offer.getPoidsDispo()));
            zone_prix.setText(String.valueOf(offer.getPrixKg()));
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.SHORT);
            date_depart.setText(format.format(offer.getDateDepart()));
            date_arrivee.setText(format.format(offer.getDateArrivee()));
            viewModel.setOffer(offer);
        }
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

        bouton_maj.setOnClickListener(v -> majOffre());
        bouton_delete.setOnClickListener(v -> {
            if (user == null) {
                askLogin();
                return;
            }
            AlertDialog.Builder adb = new AlertDialog.Builder(UpdateOfferActivity.this);
            deleteDialog = adb.setMessage(R.string.delete_compte_confirm)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        if (prepareAction())
                            viewModel.deleteOffer(offer.getId());
                    })
                    .setNegativeButton(R.string.annuler, (dialog, which) -> dialog.dismiss())
                    .create();
            deleteDialog.show();
        });

        viewModel.getOperationData().observe(this, stringResource -> {
            if (stringResource.isConsumed())
                return;
            Resource.Status status = stringResource.getStatus();
            Resource.Operation operation = stringResource.getOperation();
            stringResource.setConsumed(true);
            if (status.equals(OK)){
                if (operation.equals(UPDATE))
                    Toast.makeText(UpdateOfferActivity.this,R.string.update_offer_succes,
                            Toast.LENGTH_LONG).show();
                else if (operation.equals(DELETE))
                    Toast.makeText(UpdateOfferActivity.this,R.string.delete_offer_success,
                            Toast.LENGTH_LONG).show();
                finish();
            }
            else if (status.equals(UNAUTHORIZED)){
                Toast.makeText(UpdateOfferActivity.this,R.string.error_unauthorized,
                        Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(UpdateOfferActivity.this,R.string.error,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void majOffre() {
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
        else if (new Date().after(offer.getDateDepart())) {
            hasErrors = true;
            Toast.makeText(this, R.string.error_dates_deja_passee, Toast.LENGTH_SHORT).show();
        }
        else if (offer.getDateDepart().after(offer.getDateArrivee())){
            hasErrors = true;
            Toast.makeText(this,R.string.error_dates_invalides,Toast.LENGTH_SHORT).show();
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
            viewModel.updateOffer(offer);
        }
    }

    @Override
    public void onAskLoginFailed() {
        super.onAskLoginFailed();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDateDialog();
        if (deleteDialog != null)
            deleteDialog.dismiss();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.setOffer(offer);
        viewModel.setDate_arrivee_texte(date_arrivee.getText().toString());
        viewModel.setDate_depart_texte(date_depart.getText().toString());
    }

    @Override
    public void addDateDialog(DatePickerDialog dateDialog, TimePickerDialog timeDialog) {
        this.dateDialog = dateDialog;
        this.timeDialog = timeDialog;
    }

    @Override
    public void dismissDateDialog() {
        if (dateDialog != null)
            dateDialog.dismiss();
        if (timeDialog != null)
            timeDialog.dismiss();
    }
}
