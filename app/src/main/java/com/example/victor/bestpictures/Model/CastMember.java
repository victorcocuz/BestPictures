package com.example.victor.bestpictures.Model;

/**
 * Created by Victor on 3/11/2018.
 */

public class CastMember {

    private String castName;
    private String castSubtitle;
    private String castProfile;

    public CastMember(String castName, String castSubtitle, String castProfile) {
        this.castName = castName;
        this.castSubtitle = castSubtitle;
        this.castProfile = castProfile;
    }

    public String getCastName() {
        return castName;
    }

    public String getCastSubtitle() {
        return castSubtitle;
    }

    public String getCastProfile() {
        return castProfile;
    }
}
