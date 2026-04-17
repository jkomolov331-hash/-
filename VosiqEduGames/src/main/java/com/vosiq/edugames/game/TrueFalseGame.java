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

public class TrueFalseGame extends BaseGame {

    private List<Question[]> tfQuestions;
    private VBox container;
    private String subjectId;
    private static final int TOTAL = 10;

    public void show(String subjectId) {
        this.subjectId = subjectId;
        initRoot();
        score = 0;
        questionIndex = 0;

        // Build true/false pairs
        List<Question> raw = subjectId != null ?
            new ArrayList<>(QuestionBank.getQuestions(subjectId)) :
            QuestionBank.getRandomQuestions(TOTAL + 5);
        Collections.shuffle(raw);

        tfQuestions = new ArrayList<>();
        for (Question q : raw) {
            if (tfQuestions.size() >= TOTAL) break;
            tfQuestions.add(new Question[]{q});
        }

        root.setTop(buildTopBar("✅ " + LanguageManager.get("game.truefalse"), "#f5576c"));
        container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(30, 60, 20, 60));

        root.setCenter(container);
        MainApp.primaryStage.getScene().setRoot(root);
        showQuestion();
    }

    private void showQuestion() {
        container.getChildren().clear();

        if (questionIndex >= tfQuestions.size()) {
            stopTimer();
            root.setCenter(buildResultScreen(tfQuestions.size()));
            return;
        }

        Question q = tfQuestions.get(questionIndex)[0];

        // Decide: show correct or wrong statement
        boolean showCorrect = new Random().nextBoolean();
        String statement;
        if (showCorrect) {
            statement = q.getQuestion() + " → " + q.getCorrectAnswer();
        } else {
            List<String> wrong = q.getWrongAnswers();
            String wrongAns = wrong.get(new Random().nextInt(wrong.size()));
            statement = q.getQuestion() + " → " + wrongAns;
        }

        final boolean isTrue = showCorrect;

        Label progress = new Label("Savol " + (questionIndex + 1) + " / " + tfQuestions.size());
        progress.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a5568;");

        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setMaxWidth(650);
        card.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 20; " +
                "-fx-border-color: #f5576c44; -fx-border-radius: 20; -fx-border-width: 1.5;");

        Label questionLabel = new Label("❓ Bu ifoda to'g'rimi?");
        questionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #718096;");

        Label stateLabel = new Label(statement);
        stateLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-wrap-text: true;");
        stateLabel.setMaxWidth(590);
        stateLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(questionLabel, stateLabel);

        HBox btns = new HBox(30);
        btns.setAlignment(Pos.CENTER);

        Button trueBtn = new Button("✅ To'g'ri");
        trueBtn.setPrefWidth(180);
        trueBtn.setPrefHeight(55);
        trueBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: #1a1a2e; " +
                "-fx-font-size: 17px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-cursor: hand;");
        trueBtn.setOnAction(e -> checkAnswer(true, isTrue));

        Button falseBtn = new Button("❌ Noto'g'ri");
        falseBtn.setPrefWidth(180);
        falseBtn.setPrefHeight(55);
        falseBtn.setStyle("-fx-background-color: #f56565; -fx-text-fill: white; " +
                "-fx-font-size: 17px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-cursor: hand;");
        falseBtn.setOnAction(e -> checkAnswer(false, isTrue));

        btns.getChildren().addAll(trueBtn, falseBtn);

        container.getChildren().addAll(progress, card, btns);

        FadeTransition ft = new FadeTransition(Duration.millis(400), card);
        ft.setFromValue(0); ft.setToValue(1); ft.play();

        startTimer(() -> {
            questionIndex++;
            showQuestion();
        });
    }

    private void checkAnswer(boolean userSaid, boolean actuallyTrue) {
        stopTimer();
        boolean correct = userSaid == actuallyTrue;
        if (correct) updateScore(10);
        showFeedback(container, correct, () -> {
            questionIndex++;
            showQuestion();
        });
    }

    @Override
    protected void restart() { show(subjectId); }
}
