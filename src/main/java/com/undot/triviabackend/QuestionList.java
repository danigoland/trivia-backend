package com.undot.triviabackend;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by 0503337710 on 26/03/2016.
 */

public class QuestionList {
    List<Question> questionList = new ArrayList<>();

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
