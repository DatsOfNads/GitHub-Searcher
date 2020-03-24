package com.company.tochka.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ItemModel {

    @SerializedName("items")
    @Expose
    private ArrayList<UserModel> items;

    @SerializedName("total_count")
    private Integer totalCount;

    public ArrayList<UserModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<UserModel> items) {
        this.items = items;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}