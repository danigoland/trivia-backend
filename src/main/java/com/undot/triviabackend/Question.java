/**
 * Created by 0503337710 on 26/03/2016.
 */
package com.undot.triviabackend;

public class Question {

    /**
     * Created by oshriamir on 3/12/16.
     */



        private String mQuestion; // this field contains the question that will asked...

        //those fields contain the various options for the answer...
        private String mOptionToAns_1;
        private String mOptionToAns_2;
        private String mOptionToAns_3;
        private String mOptionToAns_4;

        private String tCorrectAns; //this field contains the correct answer fot the question...
        private int rank;

        /**
         * //this field represents the question kind.
         * field value :
         * (tQuestionKind <- 0)  =>  this question is a trivia question kind.
         * (tQuestionKind <- 1)  =>  this question is a word's completion question kind.
         */
        private Boolean tQuestionKind;

        // this var will consist the
        private String[] arrayChars;

    public Question() {
    }

    public Question (int _rank, String _tQuestion, String _mOptionToAns_1, String _mOptionToAns_2, String _mOptionToAns_3,
                     String _mOptionToAns_4, String _tCorrectAns, Boolean _tKind) {

            this.mQuestion = _tQuestion;
            this.mOptionToAns_1 = _mOptionToAns_1;
            this.mOptionToAns_2 = _mOptionToAns_2;
            this.mOptionToAns_3 = _mOptionToAns_3;
            this.mOptionToAns_4 = _mOptionToAns_4;
            this.tCorrectAns = _tCorrectAns;
            this.tQuestionKind = _tKind;
            this.rank=_rank;

        }

    public String getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getmOptionToAns_1() {
        return mOptionToAns_1;
    }

    public void setmOptionToAns_1(String mOptionToAns_1) {
        this.mOptionToAns_1 = mOptionToAns_1;
    }

    public String getmOptionToAns_2() {
        return mOptionToAns_2;
    }

    public void setmOptionToAns_2(String mOptionToAns_2) {
        this.mOptionToAns_2 = mOptionToAns_2;
    }

    public String getmOptionToAns_3() {
        return mOptionToAns_3;
    }

    public void setmOptionToAns_3(String mOptionToAns_3) {
        this.mOptionToAns_3 = mOptionToAns_3;
    }

    public String getmOptionToAns_4() {
        return mOptionToAns_4;
    }

    public void setmOptionToAns_4(String mOptionToAns_4) {
        this.mOptionToAns_4 = mOptionToAns_4;
    }

    public String gettCorrectAns() {
        return tCorrectAns;
    }

    public void settCorrectAns(String tCorrectAns) {
        this.tCorrectAns = tCorrectAns;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Boolean gettQuestionKind() {
        return tQuestionKind;
    }

    public void settQuestionKind(Boolean tQuestionKind) {
        this.tQuestionKind = tQuestionKind;
    }

    public String[] getArrayChars() {
        return arrayChars;
    }

    public void setArrayChars(String[] arrayChars) {
        this.arrayChars = arrayChars;
    }
}





