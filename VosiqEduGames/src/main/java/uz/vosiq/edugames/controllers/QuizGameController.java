package uz.vosiq.edugames.controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import uz.vosiq.edugames.MainApp;
import uz.vosiq.edugames.data.QuestionBank;
import uz.vosiq.edugames.models.Question;
import uz.vosiq.edugames.utils.LanguageManager;
import java.util.List;

public class QuizGameController {

    @FXML private Label titleLabel;
    @FXML private Label questionNumLabel;
    @FXML private Label questionLabel;
    @FXML private Label scoreLabel;
    @FXML private Label timerLabel;
    @FXML private Button[] optionButtons;
    @FXML private Button btn0, btn1, btn2, btn3;
    @FXML private Button btnBack;
    @FXML private Label feedbackLabel;
    @FXML private ProgressBar progressBar;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String subject = "math";
    private Timeline timer;
    private int timeLeft = 15;

    public void setSubject(String subject) {
        this.subject = subject;
        loadQuestions();
    }

    @FXML
    public void initialize() {
        optionButtons = new Button[]{btn0, btn1, btn2, btn3};
        titleLabel.setText(LanguageManager.get("game.quiz"));
        btnBack.setText(LanguageManager.get("btn.back"));
        feedbackLabel.setText("");
    }

    private void loadQuestions() {
        questions = QuestionBank.getQuestions(subject, LanguageManager.getLang());
        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            showResult();
            return;
        }
        Question q = questions.get(currentIndex);
        questionNumLabel.setText(LanguageManager.get("quiz.question") + " " + (currentIndex + 1) + "/" + questions.size());
        questionLabel.setText(q.getQuestionText());
        scoreLabel.setText(LanguageManager.get("quiz.score") + ": " + score);
        feedbackLabel.setText("");
        progressBar.setProgress((double) currentIndex / questions.size());

        String[] opts = q.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            String label = (char)('A' + i) + ") " + (i < opts.length ? opts[i] : "");
            optionButtons[i].setText(label);
            optionButtons[i].setDisable(i >= opts.length);
            optionButtons[i].setStyle("");
        }

        startTimer();
    }

    private void startTimer() {
        if (timer != null) timer.stop();
        timeLeft = 15;
        timerLabel.setText(LanguageManager.get("quiz.time") + ": " + timeLeft);
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText(LanguageManager.get("quiz.time") + ": " + timeLeft);
            if (timeLeft <= 5) timerLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            else timerLabel.setStyle("-fx-text-fill: white;");
            if (timeLeft <= 0) {
                timer.stop();
                showCorrectAnswer(-1);
                nextQuestion();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void showCorrectAnswer(int selected) {
        Question q = questions.get(currentIndex);
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setDisable(true);
            if (i == q.getCorrectIndex()) {
                optionButtons[i].setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
            } else if (i == selected && selected != q.getCorrectIndex()) {
                optionButtons[i].setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            }
        }
    }

    @FXML private void selectOption0() { checkAnswer(0); }
    @FXML private void selectOption1() { checkAnswer(1); }
    @FXML private void selectOption2() { checkAnswer(2); }
    @FXML private void selectOption3() { checkAnswer(3); }

    private void checkAnswer(int selected) {
        if (timer != null) timer.stop();
        Question q = questions.get(currentIndex);
        showCorrectAnswer(selected);

        if (selected == q.getCorrectIndex()) {
            score++;
            feedbackLabel.setText(LanguageManager.get("quiz.correct"));
            feedbackLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 16px; -fx-font-weight: bold;");
        } else {
            feedbackLabel.setText(LanguageManager.get("quiz.wrong") + " → " + q.getCorrectAnswer());
            feedbackLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> nextQuestion());
        pause.play();
    }

    private void nextQuestion() {
        currentIndex++;
        showQuestion();
    }

    private void showResult() {
        if (timer != null) timer.stop();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Result.fxml"));
            Parent root = loader.load();
            ResultController c = loader.getController();
            c.setResult(score, questions.size());
            MainApp.primaryStage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void goBack() {
        if (timer != null) timer.stop();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SubjectSelect.fxml"));
            Parent root = loader.load();
            MainApp.primaryStage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
