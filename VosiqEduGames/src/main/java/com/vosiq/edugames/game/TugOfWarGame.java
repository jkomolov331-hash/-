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

public class TugOfWarGame extends BaseGame {

    private double ropePosition = 0.5; // 0=blue wins, 1=red wins, 0.5=center
    private Label ropeLabel;
    private Label blueScore;
    private Label redScore;
    private int bluePoints = 0;
    private int redPoints = 0;
    private int currentTeam = 0; // 0=blue, 1=red
    private VBox container;
    private List<Question> questions;
    private int qIndex = 0;
    private static final int MAX_QUESTIONS = 10;

    public void show() {
        initRoot();
        questions = QuestionBank.getRandomQuestions(MAX_QUESTIONS + 5);
        Collections.shuffle(questions);
        if (questions.size() > MAX_QUESTIONS) questions = questions.subList(0, MAX_QUESTIONS);
        qIndex = 0;
        ropePosition = 0.5;
        bluePoints = 0;
        redPoints = 0;

        root.setTop(buildTopBar("🤼 " + LanguageManager.get("game.tugofwar"), "#a18cd1"));

        // Rope visualization
        VBox ropeArea = buildRopeArea();

        // Teams info
        HBox teamsBar = buildTeamsBar();

        container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10, 40, 10, 40));

        VBox mainContent = new VBox(15, ropeArea, teamsBar, container);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(10));

        ScrollPane sp = new ScrollPane(mainContent);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        root.setCenter(sp);
        root.setBottom(buildBackBottomBar());

        MainApp.primaryStage.getScene().setRoot(root);
        showQuestion();
    }

    private VBox buildRopeArea() {
        VBox area = new VBox(8);
        area.setAlignment(Pos.CENTER);
        area.setPadding(new Insets(15, 20, 10, 20));
        area.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 16;");
        area.setMaxWidth(700);

        Label title = new Label("🤼 Arqon Tortish");
        title.setStyle("-fx-font-size: 14px; -fx-text-fill: #718096;");

        HBox ropeBar = new HBox();
        ropeBar.setAlignment(Pos.CENTER_LEFT);
        ropeBar.setPrefHeight(30);
        ropeBar.setPrefWidth(600);
        ropeBar.setMaxWidth(600);
        ropeBar.setStyle("-fx-background-color: #2d3748; -fx-background-radius: 15;");

        ropeLabel = new Label("😤");
        ropeLabel.setStyle("-fx-font-size: 24px;");
        ropeLabel.setTranslateX(300 - 15); // center initially

        StackPane ropeStack = new StackPane(ropeBar, ropeLabel);
        StackPane.setAlignment(ropeLabel, Pos.CENTER_LEFT);

        area.getChildren().addAll(title,
            buildTeamRopeLabel(),
            ropeStack);
        return area;
    }

    private HBox buildTeamRopeLabel() {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER);
        Label blue = new Label("🔵 Ko'k jamoa");
        blue.setStyle("-fx-font-size: 13px; -fx-text-fill: #4facfe; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label red = new Label("Qizil jamoa 🔴");
        red.setStyle("-fx-font-size: 13px; -fx-text-fill: #f56565; -fx-font-weight: bold;");
        row.getChildren().addAll(blue, spacer, red);
        row.setMaxWidth(600);
        return row;
    }

    private HBox buildTeamsBar() {
        HBox bar = new HBox(30);
        bar.setAlignment(Pos.CENTER);

        blueScore = new Label("🔵 Ko'k: 0");
        blueScore.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #4facfe; " +
                "-fx-background-color: #1e2a3a; -fx-padding: 8 20; -fx-background-radius: 10;");

        Label vs = new Label("⚡ VS ⚡");
        vs.setStyle("-fx-font-size: 16px; -fx-text-fill: #718096;");

        redScore = new Label("Qizil: 0 🔴");
        redScore.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #f56565; " +
                "-fx-background-color: #1e2a3a; -fx-padding: 8 20; -fx-background-radius: 10;");

        bar.getChildren().addAll(blueScore, vs, redScore);
        return bar;
    }

    private void showQuestion() {
        container.getChildren().clear();

        if (qIndex >= questions.size()) {
            stopTimer();
            showFinalResult();
            return;
        }

        Question q = questions.get(qIndex);
        String teamName = currentTeam == 0 ? "🔵 Ko'k jamoaning navbati" : "🔴 Qizil jamoaning navbati";
        String teamColor = currentTeam == 0 ? "#4facfe" : "#f56565";

        Label turnLabel = new Label(teamName);
        turnLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + teamColor + "; " +
                "-fx-background-color: " + teamColor + "22; -fx-padding: 8 20; -fx-background-radius: 10;");

        VBox qCard = new VBox(10);
        qCard.setAlignment(Pos.CENTER);
        qCard.setPadding(new Insets(20, 25, 20, 25));
        qCard.setMaxWidth(650);
        qCard.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 16; " +
                "-fx-border-color: " + teamColor + "44; -fx-border-radius: 16; -fx-border-width: 1.5;");

        Label qLabel = new Label(q.getQuestion());
        qLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-wrap-text: true;");
        qLabel.setMaxWidth(600);
        qLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        qCard.getChildren().add(qLabel);

        List<String> answers = new ArrayList<>(q.getWrongAnswers());
        answers.add(q.getCorrectAnswer());
        Collections.shuffle(answers);

        GridPane ansGrid = new GridPane();
        ansGrid.setHgap(10);
        ansGrid.setVgap(10);
        ansGrid.setAlignment(Pos.CENTER);

        String[] colors = {"#4facfe", "#f56565", "#43e97b", "#f093fb"};
        int i = 0;
        for (String ans : answers) {
            final String answer = ans;
            final String c = colors[i % colors.length];
            Button btn = new Button(answer);
            btn.setPrefWidth(270);
            btn.setPrefHeight(45);
            btn.setWrapText(true);
            btn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 13px; " +
                    "-fx-background-radius: 10; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: " + c + "44; -fx-text-fill: white; -fx-font-size: 13px; " +
                    "-fx-background-radius: 10; -fx-cursor: hand; -fx-border-color: " + c + "; " +
                    "-fx-border-radius: 10; -fx-border-width: 2;"));
            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 13px; " +
                    "-fx-background-radius: 10; -fx-cursor: hand;"));
            btn.setOnAction(e -> checkTugAnswer(answer, q.getCorrectAnswer(), ansGrid));
            ansGrid.add(btn, i % 2, i / 2);
            i++;
        }

        container.getChildren().addAll(turnLabel, qCard, ansGrid);

        FadeTransition ft = new FadeTransition(Duration.millis(400), qCard);
        ft.setFromValue(0); ft.setToValue(1); ft.play();

        startTimer(() -> {
            currentTeam = 1 - currentTeam;
            qIndex++;
            showQuestion();
        });
    }

    private void checkTugAnswer(String selected, String correct, GridPane grid) {
        stopTimer();
        grid.setDisable(true);
        boolean isCorrect = selected.equals(correct);

        if (isCorrect) {
            if (currentTeam == 0) bluePoints++;
            else redPoints++;
            moveRope(currentTeam == 0 ? -0.08 : 0.08);
        }

        updateScoreLabels();
        showFeedback(container, isCorrect, () -> {
            currentTeam = 1 - currentTeam;
            qIndex++;
            showQuestion();
        });
    }

    private void moveRope(double delta) {
        ropePosition = Math.max(0.1, Math.min(0.9, ropePosition + delta));
        double x = ropePosition * 560 + 20;
        ropeLabel.setTranslateX(x);
    }

    private void updateScoreLabels() {
        blueScore.setText("🔵 Ko'k: " + bluePoints);
        redScore.setText("Qizil: " + redPoints + " 🔴");
    }

    private void showFinalResult() {
        container.getChildren().clear();

        VBox result = new VBox(20);
        result.setAlignment(Pos.CENTER);
        result.setPadding(new Insets(30));

        String winner;
        String color;
        if (bluePoints > redPoints) {
            winner = "🔵 Ko'k jamoa g'alaba qozondi! 🏆";
            color = "#4facfe";
        } else if (redPoints > bluePoints) {
            winner = "🔴 Qizil jamoa g'alaba qozondi! 🏆";
            color = "#f56565";
        } else {
            winner = "🤝 Durrang! Teng natija!";
            color = "#a0aec0";
        }

        Label winnerLabel = new Label(winner);
        winnerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label scores = new Label("Ko'k: " + bluePoints + "  |  Qizil: " + redPoints);
        scores.setStyle("-fx-font-size: 18px; -fx-text-fill: #a0aec0;");

        Button again = new Button("🔄 " + LanguageManager.get("game.playagain"));
        again.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-size: 15px; " +
                "-fx-padding: 10 25; -fx-background-radius: 10; -fx-cursor: hand;");
        again.setOnAction(e -> restart());

        Button menu = new Button("🏠 Menyu");
        menu.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 15px; " +
                "-fx-padding: 10 25; -fx-background-radius: 10; -fx-cursor: hand;");
        menu.setOnAction(e -> backToGames());

        HBox btns = new HBox(15, again, menu);
        btns.setAlignment(Pos.CENTER);

        result.getChildren().addAll(winnerLabel, scores, btns);
        container.getChildren().add(result);
    }

    private HBox buildBackBottomBar() {
        HBox box = new HBox();
        box.setPadding(new Insets(10, 20, 15, 20));
        Button back = new Button("← " + LanguageManager.get("menu.back"));
        back.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-padding: 8 20; " +
                "-fx-background-radius: 10; -fx-cursor: hand;");
        back.setOnAction(e -> backToGames());
        box.getChildren().add(back);
        return box;
    }

    @Override
    protected void restart() { show(); }
}
