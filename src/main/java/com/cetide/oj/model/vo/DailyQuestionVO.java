package com.cetide.oj.model.vo;

import com.cetide.oj.model.entity.Question;

public class DailyQuestionVO {
    private Question question;

    private String questionImg;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getQuestionImg() {
        return questionImg;
    }

    public void setQuestionImg(String questionImg) {
        this.questionImg = questionImg;
    }
}
