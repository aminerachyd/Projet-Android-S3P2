package com.inpt.jibmaak.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.repository.OfferRepository;
import com.inpt.jibmaak.repository.Resource;

public class SubmitOfferViewModel extends ViewModel {
    protected OfferRepository offerRepository;
    protected MutableLiveData<Resource<String>> operationData;
    protected Offer offer;
    protected String date_depart_texte,date_arrivee_texte;
    @ViewModelInject
    public SubmitOfferViewModel(@Assisted SavedStateHandle savedStateHandle,
                                OfferRepository offerRepository){
        this.offerRepository = offerRepository;
        this.offer = new Offer();
        this.operationData = new MutableLiveData<>();
        this.offerRepository.setResultData(this.operationData);
    }

    public void createOffer(Offer offerToCreate){
        offerRepository.createOffer(offerToCreate);
    }

    public OfferRepository getOfferRepository() {
        return offerRepository;
    }

    public void setOfferRepository(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public LiveData<Resource<String>> getOperationData() {
        return operationData;
    }

    public void setOperationData(MutableLiveData<Resource<String>> operationData) {
        this.operationData = operationData;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public String getDate_depart_texte() {
        return date_depart_texte;
    }

    public void setDate_depart_texte(String date_depart_texte) {
        this.date_depart_texte = date_depart_texte;
    }

    public String getDate_arrivee_texte() {
        return date_arrivee_texte;
    }

    public void setDate_arrivee_texte(String date_arrivee_texte) {
        this.date_arrivee_texte = date_arrivee_texte;
    }


}
