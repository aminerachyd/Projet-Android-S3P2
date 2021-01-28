package com.inpt.jibmaak.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;

import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchOfferActivity extends BaseActivity implements  ActivityManageDateDialog{

    // Activité de recherche d'offre
    // L'utilisateur saisi les infos pour trouver l'offre qui le convient
    public static final String EXTRA_CRITERIA = "com.inpt.jibmaak.EXTRA_CRITERIA";
    public static final String EXTRA_PAGINATION = "com.inpt.jibmaak.EXTRA_PAGINATION";
    
    protected OfferSearchCriteria criteria;
    protected Pagination page;

    protected SeekBar slider_poids;
    protected SeekBar slider_prix_min;
    protected SeekBar slider_prix_max;
    protected TextView label_poids;
    protected TextView label_prix_min;
    protected TextView label_prix_max;
    protected TextView date_depart_avant;
    protected TextView date_depart_apres;
    protected TextView date_arrivee_avant;
    protected TextView date_arrivee_apres;
    protected Button bouton_recherche;
    protected DatePickerDialog dateDialog;
    protected TimePickerDialog timeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_offer);
        
        // On recupere les vues
        slider_poids = findViewById(R.id.slider_poids);
        slider_prix_min = findViewById(R.id.slider_prix_min);
        slider_prix_max = findViewById(R.id.slider_prix_max);
        label_poids = findViewById(R.id.label_poids);
        label_prix_min = findViewById(R.id.label_prix_min);
        label_prix_max = findViewById(R.id.label_prix_max);
        date_depart_avant = findViewById(R.id.date_depart_avant);
        date_depart_apres = findViewById(R.id.date_depart_apres);
        date_arrivee_avant = findViewById(R.id.date_arrive_avant);
        date_arrivee_apres = findViewById(R.id.date_arrive_apres);
        bouton_recherche = findViewById(R.id.card_chercher_offre);

        if (savedInstanceState == null){
            criteria = new OfferSearchCriteria();
            page = new Pagination(0,10);
        }
        else{
            criteria = savedInstanceState.getParcelable("CRITERIA");
            page = savedInstanceState.getParcelable("PAGE");
            date_depart_avant.setText(savedInstanceState.getString("DEPART_AVANT"));
            date_depart_apres.setText(savedInstanceState.getString("DEPART_APRES"));
            date_arrivee_avant.setText(savedInstanceState.getString("ARRIVEE_AVANT"));
            date_arrivee_apres.setText(savedInstanceState.getString("ARRIVEE_APRES"));
        }

        // On met en place les listeners sur les sliders
        slider_poids.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                label_poids.setText(getString(R.string.poids_disponible_minimum,progress));
                criteria.setMinPoidsDisponible(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        slider_poids.setProgress(0,false);

        slider_prix_min.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = slider_prix_max.getProgress();
                if (fromUser && progress > max){
                    slider_prix_max.setProgress(progress,true);
                }
                label_prix_min.setText(getString(R.string.prix_minimum,progress));
                criteria.setMinPrixKg(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        slider_prix_min.setProgress(2);

        slider_prix_max.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = slider_prix_min.getProgress();
                if (fromUser && progress < min){
                    slider_prix_min.setProgress(progress,true);
                }
                label_prix_max.setText(getString(R.string.prix_maximum,progress));
                criteria.setMaxPrixKg(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        slider_prix_max.setProgress(10);

        // On met en place ceux pour le choix des dates
        date_depart_avant.setOnClickListener(new DateListener(this) {
            @Override
            public Date getMinDate() {
                return criteria.getDepartApres();
            }

            @Override
            public Date getMaxDate() {
                return null;
            }

            @Override
            public Date getCurrentDate() {
                return criteria.getDepartAvant();
            }

            @Override
            public void setCurrentDate(Date date) {
                criteria.setDepartAvant(date);
                String texte = date == null ? getString(R.string.choisir_date) : this.format.format(date);
                date_depart_avant.setText(texte);
            }
        });
        date_depart_apres.setOnClickListener(new DateListener(this) {
            @Override
            public Date getMinDate() {
                return null;
            }

            @Override
            public Date getMaxDate() {
                return criteria.getDepartAvant();
            }

            @Override
            public Date getCurrentDate() {
                return criteria.getDepartApres();
            }

            @Override
            public void setCurrentDate(Date date) {
                criteria.setDepartApres(date);
                String texte = date == null ? getString(R.string.choisir_date) : this.format.format(date);
                date_depart_apres.setText(texte);
            }
        });

        date_arrivee_avant.setOnClickListener(new DateListener(this) {
            @Override
            public Date getMinDate() {
                return criteria.getArriveApres();
            }

            @Override
            public Date getMaxDate() {
                return null;
            }

            @Override
            public Date getCurrentDate() {
                return criteria.getArriveAvant();
            }

            @Override
            public void setCurrentDate(Date date) {
                criteria.setArriveAvant(date);
                String texte = date == null ? getString(R.string.choisir_date) : this.format.format(date);
                date_arrivee_avant.setText(texte);
            }
        });
        date_arrivee_apres.setOnClickListener(new DateListener(this) {
            @Override
            public Date getMinDate() {
                return null;
            }

            @Override
            public Date getMaxDate() {
                return criteria.getArriveAvant();
            }

            @Override
            public Date getCurrentDate() {
                return criteria.getArriveApres();
            }

            @Override
            public void setCurrentDate(Date date) {
                criteria.setArriveApres(date);
                String texte = date == null ? getString(R.string.choisir_date) : this.format.format(date);
                date_arrivee_apres.setText(texte);
            }
        });

        bouton_recherche.setOnClickListener(v -> {
            Intent intent = new Intent(SearchOfferActivity.this,
                    SearchOfferResultActivity.class);
            intent.putExtra(EXTRA_CRITERIA, criteria);
            intent.putExtra(EXTRA_PAGINATION, page);
            startActivity(intent);
        });
        
    }

    @Override
    public String getConsommateurName() {
        return "SearchOfferActivity";
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("CRITERIA",criteria);
        outState.putParcelable("PAGE",page);
        outState.putString("DEPART_AVANT",date_depart_avant.getText().toString());
        outState.putString("DEPART_APRES",date_depart_apres.getText().toString());
        outState.putString("ARRIVEE_AVANT", date_arrivee_avant.getText().toString());
        outState.putString("ARRIVEE_APRES", date_arrivee_apres.getText().toString());
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