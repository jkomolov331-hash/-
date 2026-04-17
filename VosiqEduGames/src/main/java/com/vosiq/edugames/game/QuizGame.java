package com.vosiq.edugames.game;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.data.QuestionBank;
import com.vosiq.edugames.model.Question;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.*;

public class QuizGame extends BaseGame {

    private List<Question> questions;
    private VBox questionContainer;
    private ProgressBar progressBar;
    private String subjectId;
    private static final int TOTAL_QUESTIONS = 10;

    public void show(String subjectId) {
        this.subjectId = subjectId;
        initRoot();
        score = 0;
        questionIndex = 0;

        if (subjectId != null) {
            questions = new ArrayList<>(QuestionBank.getQuestions(subjectId));
        } else {
            questions = QuestionBank.getRandomQuestions(TOTAL_QUESTIONS + 5);
        }
        Collections.shuffle(questions);
        if (questions.size() > TOTAL_QUESTIONS) questions = questions.subList(0, TOTAL_QUESTIONS);

        root.setTop(buildTopBar("🧠 " + LanguageManager.get("game.quiz"), "#667eea"));

        questionContainer = new VBox(20);
        questionContainer.setAlignment(Pos.TOP_CENTER);
        questionContainer.setPadding(new Insets(20, 40, 20, 40));

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(600);
        progressBar.setPrefHeight(8);
        progressBar.setStyle("-fx-accent: #667eea;");

        VBox wrapper = new VBox(15, progressBar, questionContainer);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(20));

        ScrollPane sp = new ScrollPane(wrapper);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        root.setCenter(sp);

        MainApp.primaryStage.getScene().setRoot(root);
        showQuestion();
    }

    private void showQuestion() {
        questionContainer.getChildren().clear();

        if (questionIndex >= questions.size()) {
            stopTimer();
            root.setCenter(buildResultScreen(questions.size()));
            return;
        }

        progressBar.setProgress((double) questionIndex / questions.size());

        Question q = questions.get(questionIndex);

        // Progress label
        Label progress = new Label("Savol " + (questionIndex + 1) + " / " + questions.size());
        progress.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a5568;");

        // Question card
        VBox qCard = new VBox(12);
        qCard.setAlignment(Pos.CENTER);
        qCard.setPadding(new Insets(25, 30, 25, 30));
        qCard.setMaxWidth(700);
        qCard.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 18; " +
                "-fx-border-color: #667eea44; -fx-border-radius: 18; -fx-border-width: 1.5;");

        Label qLabel = new Label(q.getQuestion());
        qLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; -fx-wrap-text: true;");
        qLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        qLabel.setMaxWidth(640);
        qCard.getChildren().add(qLabel);

        // Answers
        List<String> answers = new ArrayList<>(q.getWrongAnswers());
        answers.add(q.getCorrectAnswer());
        Collections.shuffle(answers);

        GridPane answerGrid = new GridPane();
        answerGrid.setHgap(12);
        answerGrid.setVgap(12);
        answerGrid.setAlignment(Pos.CENTER);

        String[] colors = {"#667eea", "#f093fb", "#43e97b", "#f5576c"};
        int i = 0;
        for (String ans : answers) {
            final String answer = ans;
            final String color = colors[i % colors.length];
            Button btn = new Button(answer);
            btn.setPrefWidth(300);
            btn.setPrefHeight(50);
            btn.setWrapText(true);
            btn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 14px; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; -fx-alignment: center;");
            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: " + color + "44; -fx-text-fill: white; -fx-font-size: 14px; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; -fx-border-color: " + color + "; " +
                    "-fx-border-radius: 12; -fx-border-width: 2;"));
            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 14px; " +
                    "-fx-background-radius: 12; -fx-cursor: hand;"));
            btn.setOnAction(e -> checkAnswer(answer, q.getCorrectAnswer(), answerGrid));
            answerGrid.add(btn, i % 2, i / 2);
            i++;
        }

        questionContainer.getChildren().addAll(progress, qCard, answerGrid);

        // Animate in
        qCard.setOpacity(0);
        answerGrid.setOpacity(0);
        FadeTransition ft1 = new FadeTransition(Duration.millis(300), qCard);
        ft1.setToValue(1); ft1.play();
        FadeTransition ft2 = new FadeTransition(Duration.millis(300), answerGrid);
        ft2.setToValue(1); ft2.setDelay(Duration.millis(150)); ft2.play();

        startTimer(() -> {
            questionIndex++;
            showQuestion();
        });
    }

    private void checkAnswer(String selected, String correct, GridPane grid) {
        stopTimer();
        grid.setDisable(true);

        boolean isCorrect = selected.equals(correct);
        if (isCorrect) updateScore(10);

        showFeedback(questionContainer, isCorrect, () -> {
            questionIndex++;
            showQuestion();
        });
    }

    @Override
    protected void restart() {
        show(subjectId);
    }
}
