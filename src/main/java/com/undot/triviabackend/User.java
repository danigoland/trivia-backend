package com.undot.triviabackend;

/**
 * Created by 0503337710 on 27/03/2016.
 */
public class User {
    private String name;
    private String email;
    private String googleId;
    private boolean validToken;
    private String sPrivate;
    private String sPublic;
    private String cPrivate;
    private String cPublic;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public boolean isValidToken() {
        return validToken;
    }

    public void setValidToken(boolean validToken) {
        this.validToken = validToken;
    }

    public String getsPrivate() {
        return sPrivate;
    }

    public void setsPrivate(String sPrivate) {
        this.sPrivate = sPrivate;
    }

    public String getsPublic() {
        return sPublic;
    }

    public void setsPublic(String sPublic) {
        this.sPublic = sPublic;
    }

    public String getcPrivate() {
        return cPrivate;
    }

    public void setcPrivate(String cPrivate) {
        this.cPrivate = cPrivate;
    }

    public String getcPublic() {
        return cPublic;
    }

    public void setcPublic(String cPublic) {
        this.cPublic = cPublic;
    }
}
