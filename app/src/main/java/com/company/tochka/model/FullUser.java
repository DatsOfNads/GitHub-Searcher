package com.company.tochka.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FullUser implements Parcelable {

    @SerializedName("login")
    private String login;

    @SerializedName("avatar_url")
    private String avatarURL;

    @SerializedName("name")
    private String name;

    @SerializedName("company")
    private String company;

    @SerializedName("blog")
    private String blog;

    @SerializedName("location")
    private String location;

    @SerializedName("public_repos")
    private String publicRepos;

    @SerializedName("public_gists")
    private String publicGists;

    @SerializedName("followers")
    private String followers;

    @SerializedName("bio")
    private String bio;

    public FullUser(Parcel in) {
        login = in.readString();
        avatarURL = in.readString();
        name = in.readString();
        company = in.readString();
        blog = in.readString();
        location = in.readString();
        publicRepos = in.readString();
        publicGists = in.readString();
        followers = in.readString();
        bio = in.readString();
    }

    public static final Creator<FullUser> CREATOR = new Creator<FullUser>() {
        @Override
        public FullUser createFromParcel(Parcel in) {
            return new FullUser(in);
        }

        @Override
        public FullUser[] newArray(int size) {
            return new FullUser[size];
        }
    };

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPublicRepos() {
        return publicRepos;
    }

    public void setPublicRepos(String publicRepos) {
        this.publicRepos = publicRepos;
    }

    public String getPublicGists() {
        return publicGists;
    }

    public void setPublicGists(String publicGists) {
        this.publicGists = publicGists;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(avatarURL);
        dest.writeString(name);
        dest.writeString(company);
        dest.writeString(blog);
        dest.writeString(location);
        dest.writeString(publicRepos);
        dest.writeString(publicGists);
        dest.writeString(followers);
        dest.writeString(bio);
    }
}