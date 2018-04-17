package com.example.android.bestpictures.objects;

/******
 * Created by Victor on 3/11/2018.
 ******/

public class CastMember {

    private final String castName;
    private final String castSubtitle;
    private final String castProfile;

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
