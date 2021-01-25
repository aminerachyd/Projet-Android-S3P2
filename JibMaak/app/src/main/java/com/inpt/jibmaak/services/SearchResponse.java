package com.inpt.jibmaak.services;

import com.inpt.jibmaak.model.Offer;

import java.util.ArrayList;

public class SearchResponse {
    protected boolean hasMore;
    protected ArrayList<Offer> offers;
    protected int page;
    protected int limit;

    public SearchResponse(){}

    public SearchResponse(boolean hasMore, ArrayList<Offer> offers, int page, int limit) {
        this.hasMore = hasMore;
        this.offers = offers;
        this.page = page;
        this.limit = limit;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
