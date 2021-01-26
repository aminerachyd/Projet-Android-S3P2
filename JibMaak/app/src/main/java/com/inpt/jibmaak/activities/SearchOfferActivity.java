package com.inpt.jibmaak.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.viewmodels.SearchOfferViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchOfferActivity extends BaseActivity {

    // Activité de recherche d'offre
    // L'utilisateur saisi les infos pour trouver l'offre qui le convient
    public static final String EXTRA_LIST_OFFERS = "com.inpt.jibmaak.EXTRA_LIST_OFFERS";
    public static final String EXTRA_CRITERIA = "com.inpt.jibmaak.EXTRA_CRITERIA";
    public static final String EXTRA_PAGINATION = "com.inpt.jibmaak.EXTRA_PAGINATION";
    
    protected OfferSearchCriteria criteria;
    protected Pagination page;
    protected SearchOfferViewModel viewModel;

    protected SeekBar slider_poids;
    protected SeekBar slider_prix_min;
    protected SeekBar slider_prix_max;
    protected TextView label_poids;
    protected TextView label_prix_min;
    protected TextView label_prix_max;
    protected TextView date_depart_avant;
    protected TextView date_depart_apres;
    protected TextView date_arrive_avant;
    protected TextView date_arrive_apres;
    protected Button bouton_recherche;
    
    /** Classe abstraite pour définir un callback en cas de changement de date */
    abstract static class DateListener implements View.OnClickListener{
        protected DateFormat format = SimpleDateFormat.getDateInstance();
        /**
         * Methode qui renvoie la date minimale possible
         * @return La date minimale possible selectionnable, null si aucune limite
         */
        public abstract Date getMinDate();

        /**
         * Methode qui renvoie la date maximale possible
         * @return La date maximale possible selectionnable, null si aucune limite
         */
        public abstract Date getMaxDate();

        /**
         * Methode qui renvoie la valeur actuelle
         * @return La date actuelle, null si aucune valeur actuellement
         */
        public abstract Date getCurrentDate();

        /**
         * Methode qui change la valeur actuelle de la date
         * @param date La nouvelle valeur de la date, null si aucune
         */
        public abstract void setCurrentDate(Date date);
        @Override
        public void onClick(View v) {
            Date min_date = getMinDate();
            Date max_date = getMaxDate();
            Date current_date = getCurrentDate();
            DatePickerDialog dpd = new DatePickerDialog(v.getContext());
            Calendar calendar = Calendar.getInstance();
            if (current_date != null){
                calendar.setTime(current_date);
                dpd.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH));
            }
            if (min_date != null)
                dpd.getDatePicker().setMinDate(min_date.getTime());
            if (max_date != null)
                dpd.getDatePicker().setMaxDate(max_date.getTime());
            dpd.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                Calendar new_calendar = Calendar.getInstance();
                new_calendar.set(year,month,dayOfMonth);
                setCurrentDate(new_calendar.getTime());
            });

            dpd.setOnCancelListener(dialog -> setCurrentDate(null));
            dpd.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_offer);
        // On recupere le view model et les données qu'il contient
        viewModel = new ViewModelProvider(this)
                .get(SearchOfferViewModel.class);
        criteria = viewModel.getCriteria();
        page = viewModel.getPage();

        viewModel.getSearchOffersData().observe(this, listResource -> {
            removeWaitingScreen();
            Resource.Status status = listResource.getStatus();
            switch (status){
                case ERROR:
                    if (!listResource.isConsumed()){
                        Toast.makeText(SearchOfferActivity.this,R.string.error_search,
                                Toast.LENGTH_SHORT).show();
                        listResource.setConsumed(true);
                    }
                    break;
                case UNAUTHORIZED:
                    break;
                case OK:
                    if (!listResource.isConsumed()) {
                        Intent intent = new Intent(SearchOfferActivity.this,
                                SearchOfferResultActivity.class);
                        intent.putParcelableArrayListExtra(EXTRA_LIST_OFFERS, listResource.getResource());
                        intent.putExtra(EXTRA_CRITERIA, criteria);
                        intent.putExtra(EXTRA_PAGINATION, page);
                        startActivity(intent);
                    }
                    break;
            }
        });
        
        // On recupere les vues
        slider_poids = findViewById(R.id.slider_poids);
        slider_prix_min = findViewById(R.id.slider_prix_min);
        slider_prix_max = findViewById(R.id.slider_prix_max);
        label_poids = findViewById(R.id.label_poids);
        label_prix_min = findViewById(R.id.label_prix_min);
        label_prix_max = findViewById(R.id.label_prix_max);

        date_depart_avant = findViewById(R.id.date_depart_avant);
        date_depart_apres = findViewById(R.id.date_depart_apres);
        date_arrive_avant = findViewById(R.id.date_arrive_avant);
        date_arrive_apres = findViewById(R.id.date_arrive_apres);

        bouton_recherche = findViewById(R.id.card_chercher_offre);

        // On met en place les listeners sur les sliders
        slider_poids.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                label_poids.setText(getString(R.string.poids_disponible,progress));
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
        date_depart_avant.setOnClickListener(new DateListener() {
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
        date_depart_apres.setOnClickListener(new DateListener() {
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

        date_arrive_avant.setOnClickListener(new DateListener() {
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
                date_arrive_avant.setText(texte);
            }
        });
        date_arrive_apres.setOnClickListener(new DateListener() {
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
                date_arrive_apres.setText(texte);
            }
        });

        bouton_recherche.setOnClickListener(v -> lancerRecherche());
        
    }

    @Override
    public String getConsommateurName() {
        return "SearchOfferActivity";
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        viewModel.setCriteria(criteria);
        viewModel.setPage(page);
    }

    public void lancerRecherche(){
        if (!hasConnection){
            Toast.makeText(this,R.string.no_connection,Toast.LENGTH_SHORT).show();
            return;
        }
        makeWaitingScreen();
        viewModel.chercherOffres(criteria,page);
    }
}