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

    @SerializedName("score")
    private Integer score;

    public UserModel(){

    }

    public UserModel (String login, String avatarURL, Integer id, Integer score){
        this.login = login;
        this.avatarURL = avatarURL;
        this.id = id;
        this.score = score;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return "login " + this.login;
    }
}