package com.undot.triviabackend;

/**
 * Created by 0503337710 on 27/03/2016.
 */
public class Score {
    private String userToken;
    private String score;
    private String category;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
