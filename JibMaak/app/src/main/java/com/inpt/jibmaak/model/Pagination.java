package com.inpt.jibmaak.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pagination implements Parcelable {
    protected int page;
    protected int limit;

    public Pagination(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;
    }

    protected Pagination(Parcel in) {
        page = in.readInt();
        limit = in.readInt();
    }

    public static final Creator<Pagination> CREATOR = new Creator<Pagination>() {
        @Override
        public Pagination createFromParcel(Parcel in) {
            return new Pagination(in);
        }

        @Override
        public Pagination[] newArray(int size) {
            return new Pagination[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(limit);
    }
}
