package com.inpt.jibmaak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.adapters.OfferAdapter;
import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.viewmodels.SearchOfferResultViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchOfferResultActivity extends AuthenticateActivity {

    // Activité de résultat de la recherche
    // Est affiché le résutlat de la recherche effectuée par l'utilisateur

    protected ListView listView;
    protected TextView label_resultats;
    protected OfferAdapter adapter;
    protected SearchOfferResultViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_offer_result);

        viewModel = new ViewModelProvider(this).get(SearchOfferResultViewModel.class);
        // On recupere les vues
        label_resultats = findViewById(R.id.label_resultats_recherche);
        listView = findViewById(R.id.liste_resultats_recherche);

        // On recupere les informations depuis l'intent si c'est la premiere qu'on lance
        // l'activité
        if (savedInstanceState == null){
            Intent starterIntent = getIntent();
            OfferSearchCriteria criteria = starterIntent.getParcelableExtra(
                    SearchOfferActivity.EXTRA_CRITERIA);
            Pagination page = starterIntent.getParcelableExtra(
                    SearchOfferActivity.EXTRA_PAGINATION);
            ArrayList<Offer> offers = starterIntent.getParcelableArrayListExtra(
                    SearchOfferActivity.EXTRA_LIST_OFFERS);
            if (criteria == null || page == null || offers == null){
                // On arrete l'activité : c'est une erreur
                finish();
            }
            viewModel.setCriteria(criteria);
            viewModel.setPage(page);
            Resource<ArrayList<Offer>> offersResource = new Resource<>();
            offersResource.setStatus(Resource.Status.OK);
            offersResource.setOperation(Resource.Operation.READ);
            offersResource.setResource(offers);
            viewModel.getOffersData().setValue(offersResource);
            if (!offers.isEmpty())
                label_resultats.setText(R.string.yes_resuts_search);
        }

        adapter = viewModel.getOfferAdapter();
        if (adapter == null)
            adapter = new OfferAdapter(getLayoutInflater());
        if (!adapter.isEmpty()){
            label_resultats.setText(R.string.yes_resuts_search);
        }
        listView.setAdapter(adapter);


        viewModel.getOffersData().observe(this, offers -> {
            // On extrait les informations
            if (!offers.isConsumed()){
                Resource.Status status = offers.getStatus();
                switch (status){
                    case OK:
                        // On affiche les données
                        ArrayList<Offer> offerArrayList = offers.getResource();
                        adapter.addOffers(offers.getResource());
                        break;
                    case UNAUTHORIZED:
                        askLogin();
                        break;
                    case SERVER_ERROR:
                        Toast.makeText(SearchOfferResultActivity.this,
                                R.string.error_server,Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(SearchOfferResultActivity.this,
                                R.string.error,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.setOfferAdapter(adapter);
    }

    @Override
    public void onAskLoginFailed() {
        super.onAskLoginFailed();
        finish();
    }

    @Override
    public String getConsommateurName() {
        return "SearchOfferResultActivity";
    }
}