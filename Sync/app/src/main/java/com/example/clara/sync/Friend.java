package com.example.clara.sync;

/**
 * Created by Clara on 2/5/2017.
 */
public class Friend {

    public Friend(String name, String facebook, String instagram, String twitter) {
        this.name = name;
        this.facebook = facebook;
        this.instagram = instagram;
        this.twitter = twitter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getName() {
        return name;
    }

    private String name;
    private String facebook;
    private String instagram;
    private String twitter;

    public String getFacebook() {
        return facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getTwitter() {
        return twitter;
    }
}

