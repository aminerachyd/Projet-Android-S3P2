package com.inpt.jibmaak.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.adapters.OfferAdapter;
import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.viewmodels.SearchOfferResultViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    protected SwipeRefreshLayout refreshLayout;
    protected AlertDialog offerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_offer_result);

        viewModel = new ViewModelProvider(this).get(SearchOfferResultViewModel.class);
        // On recupere les vues
        label_resultats = findViewById(R.id.label_resultats_recherche);
        listView = findViewById(R.id.liste_resultats_recherche);
        refreshLayout = findViewById(R.id.refresh_result_search);
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
        }

        adapter = new OfferAdapter(getLayoutInflater());
        ArrayList<Offer> savedOffers = viewModel.getSavedOffers();
        if (savedOffers != null && !savedOffers.isEmpty()){
            adapter.setOffers(savedOffers);
            label_resultats.setText(R.string.yes_resuts_search);
        }
        listView.setAdapter(adapter);

        // On met en place un listener sur la listView
        // Quand on arrive à la fin on continue la recherche
        listView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (!v.canScrollVertically(1) && !viewModel.isSearchFinished()
                    && prepareAction()){
                viewModel.continueSearch();
            }

        });

        viewModel.getOffersData().observe(this, offers -> {
            removeWaitingScreen();
            // On extrait les informations
            if (!offers.isConsumed()){
                Resource.Status status = offers.getStatus();
                switch (status){
                    case OK:
                        // On affiche les données
                        ArrayList<Offer> offerArrayList = offers.getResource();
                        Pagination pagination = viewModel.getPage();
                        if (offerArrayList.size() < pagination.getLimit())
                            viewModel.setSearchFinished(true);
                        if (!offerArrayList.isEmpty())
                            label_resultats.setText(R.string.yes_resuts_search);
                        adapter.addOffers(offerArrayList);
                        break;
                    case UNAUTHORIZED:
                        askLogin();
                        break;
                    case SERVER_ERROR:
                        viewModel.setSearchFinished(true);
                        Toast.makeText(SearchOfferResultActivity.this,
                                R.string.error_server,Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        viewModel.setSearchFinished(true);
                        Toast.makeText(SearchOfferResultActivity.this,
                                R.string.error,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Offer offer = (Offer) adapter.getItem(position);
            showDetails(offer);
        });

        refreshLayout.setOnRefreshListener(this::refresh);
    }

    public void refresh(){
        if (prepareAction()){
            adapter.clearOffers();
            viewModel.refresh();
        }
        refreshLayout.setRefreshing(false);
    }

    public void showDetails(Offer offer){
        offerDetails = new AlertDialog.Builder(SearchOfferResultActivity.this)
                .setView(R.layout.dialog_selected_offer).create();
        offerDetails.show();

        User livreur = offer.getUser();

        TextView label_nom = offerDetails.findViewById(R.id.label_detail_nom_livreur);
        TextView label_date_depart = offerDetails.findViewById(R.id.label_detail_date_depart);
        TextView label_heure_depart = offerDetails.findViewById(R.id.label_detail_heure_depart);
        TextView label_lieu_arrivee = offerDetails.findViewById(R.id.label_detail_lieu_arrivee);
        TextView label_lieu_depart = offerDetails.findViewById(R.id.label_detail_lieu_depart);
        TextView label_poids_disponible = offerDetails.findViewById(R.id.label_detail_poids_disponible);
        TextView label_prix_kg = offerDetails.findViewById(R.id.label_detail_prix_kg);
        TextView label_tel_livreur = offerDetails.findViewById(R.id.label_tel_livreur);

        DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL);
        DateFormat heureFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);

        label_nom.append(livreur.getNom());
        label_nom.append(" ");
        label_nom.append(livreur.getPrenom());
        label_date_depart.append(dateFormat.format(offer.getDateDepart()));
        label_heure_depart.append(heureFormat.format(offer.getDateDepart()));
        label_lieu_arrivee.append(offer.getLieuArrivee());
        label_lieu_depart.append(offer.getLieuDepart());
        label_poids_disponible.append(String.valueOf(offer.getPoidsDispo()));
        label_prix_kg.append(String.valueOf(offer.getPrixKg()));
        label_tel_livreur.append(livreur.getTelephone());

        if (user != null && user.getId().equals(livreur.getId())){
            Button bouton_modifier = offerDetails.findViewById(R.id.bouton_modifier_offre);
            bouton_modifier.setVisibility(View.VISIBLE);
            bouton_modifier.setOnClickListener(v -> {
                // TODO : modifier offer activity
            });
        }
    }

    @Override
    public void onLogin() {
        super.onLogin();
        if (offerDetails != null)
            offerDetails.dismiss();
        adapter.setUserId(user.getId());
    }

    @Override
    public void onLogout(boolean isUnexpected) {
        super.onLogout(isUnexpected);
        if (offerDetails != null)
            offerDetails.dismiss();
        adapter.setUserId(null);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.setSavedOffers(adapter.getOffers());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (offerDetails != null)
            offerDetails.dismiss();
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