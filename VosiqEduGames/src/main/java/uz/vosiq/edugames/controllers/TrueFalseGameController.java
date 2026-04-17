package uz.vosiq.edugames.controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Duration;
import uz.vosiq.edugames.MainApp;
import uz.vosiq.edugames.data.QuestionBank;
import uz.vosiq.edugames.utils.LanguageManager;
import java.util.List;

public class TrueFalseGameController {

    @FXML private Label titleLabel;
    @FXML private Label questionLabel;
    @FXML private Label questionNumLabel;
    @FXML private Label scoreLabel;
    @FXML private Label timerLabel;
    @FXML private Label feedbackLabel;
    @FXML private Button btnTrue;
    @FXML private Button btnFalse;
    @FXML private Button btnBack;
    @FXML private ProgressBar progressBar;

    private List<String[]> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String subject = "math";
    private Timeline timer;
    private int timeLeft = 10;

    public void setSubject(String subject) {
        this.subject = subject;
        loadQuestions();
    }

    @FXML
    public void initialize() {
        titleLabel.setText(LanguageManager.get("truefalse_title"));
        btnTrue.setText(LanguageManager.get("true_btn"));
        btnFalse.setText(LanguageManager.get("false_btn"));
        btnBack.setText(LanguageManager.get("back"));
        feedbackLabel.setText("");
    }

    private void loadQuestions() {
        questions = QuestionBank.getTrueFalseQuestions(LanguageManager.getLang());
        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) { showResult(); return; }
        String[] q = questions.get(currentIndex);
        questionLabel.setText(q[0]);
        questionNumLabel.setText(LanguageManager.get("question") + " " + (currentIndex + 1) + "/" + questions.size());
        scoreLabel.setText(LanguageManager.get("score") + ": " + score);
        feedbackLabel.setText("");
        progressBar.setProgress((double) currentIndex / questions.size());
        btnTrue.setDisable(false); btnFalse.setDisable(false);
        btnTrue.setStyle(""); btnFalse.setStyle("");
        startTimer();
    }

    private void startTimer() {
        if (timer != null) timer.stop();
        timeLeft = 10;
        timerLabel.setText(LanguageManager.get("time") + ": " + timeLeft);
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText(LanguageManager.get("time") + ": " + timeLeft);
            if (timeLeft <= 0) { timer.stop(); checkAnswer(null); }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML private void answerTrue() { checkAnswer("true"); }
    @FXML private void answerFalse() { checkAnswer("false"); }

    private void checkAnswer(String answer) {
        if (timer != null) timer.stop();
        btnTrue.setDisable(true); btnFalse.setDisable(true);
        String correct = questions.get(currentIndex)[1];
        boolean isCorrect = correct.equals(answer);

        if (answer != null) {
            if (isCorrect) {
                score++;
                feedbackLabel.setText(LanguageManager.get("correct"));
                feedbackLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 18px; -fx-font-weight: bold;");
                if (answer.equals("true")) btnTrue.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                else btnFalse.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
            } else {
                feedbackLabel.setText(LanguageManager.get("wrong"));
                feedbackLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 18px; -fx-font-weight: bold;");
                if (answer.equals("true")) btnTrue.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                else btnFalse.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            }
        } else {
            feedbackLabel.setText(LanguageManager.get("wrong") + " (timeout)");
            feedbackLabel.setStyle("-fx-text-fill: #e74c3c;");
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> { currentIndex++; showQuestion(); });
        pause.play();
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
