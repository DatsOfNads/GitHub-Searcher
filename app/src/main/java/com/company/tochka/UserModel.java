package com.company.tochka;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("login")
    private String login;

    @SerializedName("avatar_url")
    private String avatarURL;

    @SerializedName("id")
    private Integer id;

    @SerializedName("type")
    private String type;

    public UserModel(){

    }

    public UserModel (String login, String avatarURL, Integer id){
        this.login = login;
        this.avatarURL = avatarURL;
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "login " + this.login;
    }
}