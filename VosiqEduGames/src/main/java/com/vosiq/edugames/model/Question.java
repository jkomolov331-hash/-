package com.vosiq.edugames.model;

import java.util.List;

public class Question {
    private String question;
    private String correctAnswer;
    private List<String> wrongAnswers;
    private String subject;

    public Question(String question, String correctAnswer, List<String> wrongAnswers, String subject) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = wrongAnswers;
        this.subject = subject;
    }

    public String getQuestion() { return question; }
    public String getCorrectAnswer() { return correctAnswer; }
    public List<String> getWrongAnswers() { return wrongAnswers; }
    public String getSubject() { return subject; }
}
