package com.vosiq.edugames.game;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.*;

public class FlagsGame extends BaseGame {

    private VBox container;
    private int round = 0;
    private static final int TOTAL_ROUNDS = 10;

    // Country data: {flag emoji, country name, wrong options...}
    private static final String[][] COUNTRIES = {
        {"🇺🇿", "O'zbekiston", "Qozog'iston", "Tojikiston", "Qirg'iziston"},
        {"🇷🇺", "Rossiya", "Ukraina", "Belarus", "Polsha"},
        {"🇺🇸", "Amerika", "Kanada", "Avstraliya", "Britaniya"},
        {"🇩🇪", "Germaniya", "Avstriya", "Shveytsariya", "Niderlandiya"},
        {"🇫🇷", "Fransiya", "Belgiya", "Lyuksemburg", "Monako"},
        {"🇯🇵", "Yaponiya", "Xitoy", "Koreya", "Tayvan"},
        {"🇨🇳", "Xitoy", "Yaponiya", "Koreya", "Vetnam"},
        {"🇰🇷", "Janubiy Koreya", "Shimoliy Koreya", "Xitoy", "Yaponiya"},
        {"🇹🇷", "Turkiya", "Eron", "Suriya", "Livan"},
        {"🇮🇳", "Hindiston", "Pokiston", "Bangladesh", "Shri-Lanka"},
        {"🇬🇧", "Britaniya", "Irlandiya", "Shotlandiya", "Uels"},
        {"🇮🇹", "Italiya", "Ispaniya", "Fransiya", "Gretsiya"},
        {"🇧🇷", "Braziliya", "Argentina", "Kolumbiya", "Chili"},
        {"🇰🇿", "Qozog'iston", "O'zbekiston", "Mongol", "Rossiya"},
        {"🇸🇦", "Saudiya Arabistoni", "UAE", "Qatar", "Quvayt"},
        {"🇦🇪", "BAA", "Saudiya", "Qatar", "Bahrayn"},
        {"🇪🇬", "Misr", "Tunis", "Liviya", "Marash"},
        {"🇦🇺", "Avstraliya", "Yangi Zelandiya", "Kanada", "Britaniya"},
        {"🇨🇦", "Kanada", "Amerika", "Avstraliya", "Britaniya"},
        {"🇲🇽", "Meksika", "Ispaniya", "Argentina", "Kuba"},
    };

    public void show() {
        initRoot();
        score = 0;
        round = 0;

        root.setTop(buildTopBar("🏳️ " + LanguageManager.get("game.flags"), "#fa709a"));
        container = new VBox(25);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(30));
        root.setCenter(container);
        root.setBottom(buildBackBottomBar());

        MainApp.primaryStage.getScene().setRoot(root);
        showRound();
    }

    private void showRound() {
        container.getChildren().clear();

        if (round >= TOTAL_ROUNDS) {
            stopTimer();
            root.setCenter(buildResultScreen(TOTAL_ROUNDS));
            return;
        }

        // Pick random country
        String[] country = COUNTRIES[new Random().nextInt(COUNTRIES.length)];
        String flag = country[0];
        String correctName = country[1];

        // Build options
        List<String> options = new ArrayList<>();
        options.add(correctName);
        for (int i = 2; i < country.length && options.size() < 4; i++) {
            options.add(country[i]);
        }
        Collections.shuffle(options);

        Label progress = new Label("Savol " + (round + 1) + " / " + TOTAL_ROUNDS);
        progress.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a5568;");

        Label question = new Label("Bu qaysi mamlakat bayrog'i?");
        question.setStyle("-fx-font-size: 16px; -fx-text-fill: #a0aec0;");

        Label flagLabel = new Label(flag);
        flagLabel.setStyle("-fx-font-size: 96px;");

        // Bounce animation
        ScaleTransition st = new ScaleTransition(Duration.millis(400), flagLabel);
        st.setFromX(0.5); st.setFromY(0.5);
        st.setToX(1); st.setToY(1);
        st.play();

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);

        String[] colors = {"#667eea", "#f093fb", "#43e97b", "#f5576c"};
        int i = 0;
        for (String opt : options) {
            final String option = opt;
            final String c = colors[i % colors.length];
            Button btn = new Button(option);
            btn.setPrefWidth(230);
            btn.setPrefHeight(48);
            btn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 14px; " +
                    "-fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: " + c + "44; -fx-text-fill: white; -fx-font-size: 14px; " +
                    "-fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-border-color: " + c + "; -fx-border-radius: 12; -fx-border-width: 2;"));
            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 14px; " +
                    "-fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand;"));
            btn.setOnAction(e -> {
                stopTimer();
                grid.setDisable(true);
                boolean correct = option.equals(correctName);
                if (correct) updateScore(10);
                showFeedback(container, correct, () -> {
                    round++;
                    showRound();
                });
            });
            grid.add(btn, i % 2, i / 2);
            i++;
        }

        container.getChildren().addAll(progress, question, flagLabel, grid);

        startTimer(() -> {
            round++;
            showRound();
        });
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
