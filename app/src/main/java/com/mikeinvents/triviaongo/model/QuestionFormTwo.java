package com.mikeinvents.triviaongo.model;

public class QuestionFormTwo {

    private String mQuestionText;
    private String mAnswerOne;
    private String mAnswerTwo;

    public QuestionFormTwo(String questionText, String answerOne, String answerTwo){
        mQuestionText = questionText;
        mAnswerOne = answerOne;
        mAnswerTwo = answerTwo;
    }

    public String getQuestionText() {
        return mQuestionText;
    }

    public String getAnswerOne() {
        return mAnswerOne;
    }

    public String getAnswerTwo() {
        return mAnswerTwo;
    }


}
