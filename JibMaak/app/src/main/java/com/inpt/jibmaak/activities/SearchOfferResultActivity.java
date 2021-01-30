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
import androidx.annotation.Nullable;
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
    // Activité de recherche d'offre
    // L'utilisateur saisi les infos pour trouver l'offre qui le convient
    public static final String EXTRA_CRITERIA = "com.inpt.jibmaak.EXTRA_CRITERIA";
    public static final String EXTRA_PAGINATION = "com.inpt.jibmaak.EXTRA_PAGINATION";

    /**
     * L'utilisateur a modifié l'offre
     */
    public static final int RESULT_MODIFY_OFFER = 2;

    /**
     * Code pour la demande de modification d'offre
     */
    public static final int REQUEST_MODIFY_OFFER = 2;
    // Activité de résultat de la recherche
    // Est affiché le résutlat de la recherche effectuée par l'utilisateur

    protected ListView listView;
    protected TextView label_resultats;
    protected OfferAdapter adapter;
    protected SearchOfferResultViewModel viewModel;
    protected SwipeRefreshLayout refreshLayout;
    protected AlertDialog detailsOfferDialog;

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
                    EXTRA_CRITERIA);
            Pagination page = starterIntent.getParcelableExtra(
                    EXTRA_PAGINATION);
            if (criteria == null || page == null){
                // On arrete l'activité : c'est une erreur
                finish();
            }
            viewModel.setCriteria(criteria);
            viewModel.setPage(page);
            viewModel.continueSearch();
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
                offers.setConsumed(true);
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
        detailsOfferDialog = new AlertDialog.Builder(SearchOfferResultActivity.this)
                .setView(R.layout.dialog_selected_offer).create();
        detailsOfferDialog.show();

        User livreur = offer.getUser();

        TextView label_nom = detailsOfferDialog.findViewById(R.id.label_detail_nom_livreur);
        TextView label_lieu_depart = detailsOfferDialog.findViewById(R.id.label_detail_lieu_depart);
        TextView label_date_depart = detailsOfferDialog.findViewById(R.id.label_detail_date_depart);
        TextView label_heure_depart = detailsOfferDialog.findViewById(R.id.label_detail_heure_depart);
        TextView label_lieu_arrivee = detailsOfferDialog.findViewById(R.id.label_detail_lieu_arrivee);
        TextView label_date_arrivee = detailsOfferDialog.findViewById(R.id.label_detail_date_arrivee);
        TextView label_heure_arrivee = detailsOfferDialog.findViewById(R.id.label_detail_heure_arrivee);
        TextView label_poids_disponible = detailsOfferDialog.findViewById(R.id.label_detail_poids_disponible);
        TextView label_prix_kg = detailsOfferDialog.findViewById(R.id.label_detail_prix_kg);
        TextView label_tel_livreur = detailsOfferDialog.findViewById(R.id.label_tel_livreur);

        DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL);
        DateFormat heureFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
        label_nom.append(livreur.getNom());
        label_nom.append(" ");
        label_nom.append(livreur.getPrenom());
        label_lieu_depart.append(offer.getLieuDepart());
        label_date_depart.append(dateFormat.format(offer.getDateDepart()));
        label_heure_depart.append(heureFormat.format(offer.getDateDepart()));
        label_lieu_arrivee.append(offer.getLieuArrivee());
        label_date_arrivee.append(dateFormat.format(offer.getDateArrivee()));
        label_heure_arrivee.append(heureFormat.format(offer.getDateArrivee()));
        label_poids_disponible.append(String.valueOf(offer.getPoidsDispo()));
        label_prix_kg.append(String.valueOf(offer.getPrixKg()));
        label_tel_livreur.append(livreur.getTelephone());

        if (user != null && user.getId().equals(livreur.getId())){
            Button bouton_modifier = detailsOfferDialog.findViewById(R.id.bouton_modifier_offre);
            bouton_modifier.setVisibility(View.VISIBLE);
            bouton_modifier.setOnClickListener(v -> {
                if (user == null) {
                    askLogin();
                    return;
                }
                Intent intent = new Intent(SearchOfferResultActivity.this,
                        UpdateOfferActivity.class);
                intent.putExtra(UpdateOfferActivity.EXTRA_OFFER,offer);
                startActivityForResult(intent,REQUEST_MODIFY_OFFER);
                detailsOfferDialog.dismiss();
            });
        }
    }

    @Override
    public void updateUiWithUser() {
        super.updateUiWithUser();
        if (detailsOfferDialog != null)
            detailsOfferDialog.dismiss();
        adapter.setUserId(user.getId());
    }

    @Override
    public void updateUiNoUser() {
        if (detailsOfferDialog != null)
            detailsOfferDialog.dismiss();
        adapter.setUserId(null);
        OfferSearchCriteria criteria = viewModel.getCriteria();
        if (criteria.getUser() != null){
            criteria.setUser(null);
            viewModel.setCriteria(criteria);
            refresh();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.setSavedOffers(adapter.getOffers());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (detailsOfferDialog != null)
            detailsOfferDialog.dismiss();
    }

    @Override
    public void onAskLoginFailed() {
        super.onAskLoginFailed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MODIFY_OFFER && resultCode == RESULT_MODIFY_OFFER)
            refresh();
    }
}