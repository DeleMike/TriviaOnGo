package com.mikeinvents.triviaongo.model;

public class QuestionFormOne {

    private String mQuestionText;
    private String mOptionA;
    private String mOptionB;
    private String mOptionC;
    private String mOptionD;

    public QuestionFormOne(String questionText, String optionA, String optionB, String optionC, String optionD){
        mQuestionText = questionText;
        mOptionA = optionA;
        mOptionB = optionB;
        mOptionC = optionC;
        mOptionD = optionD;

    }

    public String getQuestionText() {
        return mQuestionText;
    }

    public String getOptionA() {
        return mOptionA;
    }

    public String getOptionB() {
        return mOptionB;
    }

    public String getOptionC() {
        return mOptionC;
    }

    public String getOptionD() {
        return mOptionD;
    }

}
