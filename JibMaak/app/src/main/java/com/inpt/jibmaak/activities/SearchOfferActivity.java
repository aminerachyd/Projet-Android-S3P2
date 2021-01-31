package com.inpt.jibmaak.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.model.SearchOfferCriteria;
import com.inpt.jibmaak.validators.VilleValidator;

import java.util.Arrays;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchOfferActivity extends BaseActivity implements ManageDateDialogActivity {

    protected SearchOfferCriteria criteria;
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
    protected AutoCompleteTextView zone_depart;
    protected AutoCompleteTextView zone_arrivee;
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
        zone_arrivee = findViewById(R.id.saisie_lieu_arrive);
        zone_depart = findViewById(R.id.saisie_lieu_depart);
        bouton_recherche = findViewById(R.id.card_chercher_offre);

        if (savedInstanceState == null){
            criteria = new SearchOfferCriteria();
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
        slider_prix_min.setProgress(0);

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
        slider_prix_max.setProgress(50);

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

        String[] villes = getResources().getStringArray(R.array.villes);
        String[] villesPropositions = Arrays.copyOf(villes,villes.length+1);
        villesPropositions[villes.length] = "";
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, villes);
        VilleValidator validator1 = new VilleValidator(villesPropositions,zone_depart);
        VilleValidator validator2 = new VilleValidator(villesPropositions,zone_arrivee);
        zone_depart.setAdapter(adapter);
        zone_arrivee.setAdapter(adapter);
        zone_depart.setValidator(validator1);
        zone_arrivee.setValidator(validator2);

        bouton_recherche.setOnClickListener(v -> {
            String depart = zone_depart.getText().toString();
            String arrivee = zone_depart.getText().toString();
            if (Arrays.stream(villesPropositions).noneMatch(depart::equals)){
                Toast.makeText(SearchOfferActivity.this,R.string.erreur_validation,
                        Toast.LENGTH_LONG).show();
                zone_depart.setError(getString(R.string.erreur_offre_ville_inconnu));
                return;
            }
            if (Arrays.stream(villesPropositions).noneMatch(arrivee::equals)){
                Toast.makeText(SearchOfferActivity.this,R.string.erreur_validation,
                        Toast.LENGTH_LONG).show();
                zone_depart.setError(getString(R.string.erreur_offre_ville_inconnu));
                return;
            }
            if (depart.length() > 0)
                criteria.setDepart(depart);
            if (arrivee.length() > 0)
                criteria.setDestination(arrivee);
            Intent intent = new Intent(SearchOfferActivity.this,
                    SearchOfferResultActivity.class);
            intent.putExtra(SearchOfferResultActivity.EXTRA_CRITERIA, criteria);
            intent.putExtra(SearchOfferResultActivity.EXTRA_PAGINATION, page);
            startActivity(intent);
        });
        
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