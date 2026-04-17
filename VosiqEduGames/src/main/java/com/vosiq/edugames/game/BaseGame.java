package com.vosiq.edugames.game;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.util.LanguageManager;
import com.vosiq.edugames.view.GamesView;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public abstract class BaseGame {

    protected BorderPane root;
    protected int score = 0;
    protected int questionIndex = 0;
    protected Label scoreLabel;
    protected Label timerLabel;
    protected Timeline timer;
    protected int timeLeft = 30;

    protected void initRoot() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f3460);");
    }

    protected HBox buildTopBar(String title, String color) {
        HBox bar = new HBox(20);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(15, 25, 10, 25));
        bar.setStyle("-fx-background-color: #1e2a3a88;");

        Button back = new Button("←");
        back.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 16px; " +
                "-fx-padding: 6 14; -fx-background-radius: 8; -fx-cursor: hand;");
        back.setOnAction(e -> backToGames());

        Label titleL = new Label(title);
        titleL.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        scoreLabel = new Label(LanguageManager.get("game.score") + ": 0");
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #43e97b; " +
                "-fx-background-color: #1e2a3a; -fx-padding: 6 15; -fx-background-radius: 10;");

        timerLabel = new Label("⏱ 30");
        timerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f093fb; " +
                "-fx-background-color: #1e2a3a; -fx-padding: 6 15; -fx-background-radius: 10;");

        bar.getChildren().addAll(back, titleL, spacer, scoreLabel, timerLabel);
        return bar;
    }

    protected void startTimer(Runnable onTimeout) {
        timeLeft = 30;
        if (timer != null) timer.stop();
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("⏱ " + timeLeft);
            if (timeLeft <= 10) {
                timerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f56565; " +
                        "-fx-background-color: #1e2a3a; -fx-padding: 6 15; -fx-background-radius: 10;");
            }
            if (timeLeft <= 0) {
                timer.stop();
                onTimeout.run();
            }
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    protected void stopTimer() {
        if (timer != null) timer.stop();
    }

    protected void updateScore(int points) {
        score += points;
        scoreLabel.setText(LanguageManager.get("game.score") + ": " + score);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), scoreLabel);
        st.setFromX(1.3); st.setFromY(1.3);
        st.setToX(1); st.setToY(1);
        st.play();
    }

    protected void showFeedback(VBox container, boolean correct, Runnable onNext) {
        String msg = correct ? "✅ " + LanguageManager.get("game.correct")
                             : "❌ " + LanguageManager.get("game.wrong");
        String color = correct ? "#43e97b" : "#f56565";

        Label feedback = new Label(msg);
        feedback.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        feedback.setAlignment(Pos.CENTER);

        container.getChildren().add(feedback);

        PauseTransition pause = new PauseTransition(Duration.millis(correct ? 1000 : 1500));
        pause.setOnFinished(e -> {
            container.getChildren().remove(feedback);
            onNext.run();
        });
        pause.play();
    }

    protected VBox buildResultScreen(int total) {
        VBox result = new VBox(20);
        result.setAlignment(Pos.CENTER);
        result.setPadding(new Insets(40));

        String emoji = score >= total * 8 ? "🏆" : score >= total * 5 ? "👍" : "💪";
        Label emojilabel = new Label(emoji);
        emojilabel.setStyle("-fx-font-size: 64px;");

        Label resultTitle = new Label(LanguageManager.get("game.result"));
        resultTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #667eea;");

        Label scoreResult = new Label(LanguageManager.get("game.score") + ": " + score + " / " + (total * 10));
        scoreResult.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #43e97b;");

        String msg = score >= total * 8 ? "Ajoyib natija! 🎉" :
                     score >= total * 5 ? "Yaxshi urinish! 👏" : "Ko'proq mashq kerak! 💪";
        Label msgLabel = new Label(msg);
        msgLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #a0aec0;");

        Button playAgain = new Button("🔄 " + LanguageManager.get("game.playagain"));
        playAgain.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                "-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; " +
                "-fx-padding: 12 30; -fx-background-radius: 12; -fx-cursor: hand;");
        playAgain.setOnAction(e -> restart());

        Button menuBtn = new Button("🏠 Bosh menyu");
        menuBtn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 15px; " +
                "-fx-padding: 12 30; -fx-background-radius: 12; -fx-cursor: hand;");
        menuBtn.setOnAction(e -> backToGames());

        HBox btns = new HBox(15, playAgain, menuBtn);
        btns.setAlignment(Pos.CENTER);

        result.getChildren().addAll(emojilabel, resultTitle, scoreResult, msgLabel, btns);

        FadeTransition ft = new FadeTransition(Duration.millis(800), result);
        ft.setFromValue(0); ft.setToValue(1); ft.play();

        return result;
    }

    protected void backToGames() {
        stopTimer();
        MainApp.primaryStage.getScene().setRoot(new GamesView().getRoot());
    }

    protected abstract void restart();
}
