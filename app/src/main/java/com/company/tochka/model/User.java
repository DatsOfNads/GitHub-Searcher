package com.company.tochka.model;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

public class User {

    private String numberInList;

    private boolean isLastInList = false;

    @SerializedName("login")
    private String login;

    @SerializedName("avatar_url")
    private String avatarURL;

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    public User(){

    }

    @BindingAdapter("android:src")
    public static void loadImage(ImageView view, String url) {
        Picasso.get().load(url).into(view);
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.INVISIBLE : View.VISIBLE);
    }

    public String getNumberInList() {
        return numberInList;
    }

    public void setNumberInList(String numberInList) {
        this.numberInList = numberInList;
    }

    public boolean isLastInList() {
        return isLastInList;
    }

    public void setLastInList(boolean lastInList) {
        isLastInList = lastInList;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}