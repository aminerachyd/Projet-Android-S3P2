package com.inpt.jibmaak.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.model.Offer;

import java.text.DateFormat;
import java.util.ArrayList;

/** Adapter pour l'affichage des offres */
public class OfferAdapter extends BaseAdapter {
    static class ViewHolder{
        public TextView proprietaire;
        public TextView description;
    }

    protected LayoutInflater inflater;
    protected ArrayList<Offer> offers;
    protected String userId;

    public OfferAdapter(LayoutInflater inflater){
        this.inflater = inflater;
        this.offers = new ArrayList<>();
    }

    public void addOffers(ArrayList<Offer> offersToAdd){
        offers.addAll(offersToAdd);
        notifyDataSetChanged();
    }

    public void clearOffers(){
        offers.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers){
        this.offers = offers;
        notifyDataSetChanged();
    }

    public void setUserId(String userId){
        this.userId = userId;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return offers.size();
    }

    @Override
    public Object getItem(int position) {
        return offers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        Offer offer = offers.get(position);
        Context context = inflater.getContext();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_offer_item,parent,false);
            vh = new ViewHolder();
            vh.description = convertView.findViewById(R.id.label_description_offre);
            vh.proprietaire = convertView.findViewById(R.id.label_nom_proprietaire);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0){
            convertView.setBackgroundColor(context.getColor(R.color.beige));
        }
        else{
            convertView.setBackgroundColor(context.getColor(R.color.white));
        }
        String prop = context.getString(R.string.nom_prop_offre,
                offer.getUser().getNom(),offer.getUser().getPrenom());
        vh.proprietaire.setText(prop);
        if (offer.getUser().getId().equals(userId))
            vh.proprietaire.setTextColor(context.getColor(R.color.blue));
        else
            vh.proprietaire.setTextColor(context.getColor(R.color.black));

        String formatDate = DateFormat.getDateTimeInstance().format(offer.getDateDepart());
        String description = context.getString(R.string.description_offre,offer.getLieuDepart(),
                offer.getLieuArrivee(),formatDate,offer.getPrixKg(),offer.getPoidsDispo());
        vh.description.setText(description);

        return convertView;
    }
}
