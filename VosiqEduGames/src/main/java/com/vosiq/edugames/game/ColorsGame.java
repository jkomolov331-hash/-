package com.vosiq.edugames.game;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

public class ColorsGame extends BaseGame {

    private VBox container;
    private int round = 0;
    private int gridSize = 3;
    private static final int TOTAL_ROUNDS = 15;
    private Label roundLabel;

    public void show() {
        initRoot();
        score = 0;
        round = 0;
        gridSize = 3;

        root.setTop(buildTopBar("🎨 " + LanguageManager.get("game.colors"), "#fccb90"));

        container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));

        Label instructions = new Label("Farqli rangni toping! 🎨");
        instructions.setStyle("-fx-font-size: 14px; -fx-text-fill: #a0aec0;");

        roundLabel = new Label("Daraja 1");
        roundLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a5568;");

        container.getChildren().addAll(instructions, roundLabel);

        root.setCenter(container);
        root.setBottom(buildBackBottomBar());

        MainApp.primaryStage.getScene().setRoot(root);
        showRound();
    }

    private void showRound() {
        // Remove old grid
        container.getChildren().removeIf(n -> n instanceof GridPane);
        container.getChildren().removeIf(n -> n instanceof Label && ((Label)n).getText().startsWith("✅"));
        container.getChildren().removeIf(n -> n instanceof Label && ((Label)n).getText().startsWith("❌"));

        if (round >= TOTAL_ROUNDS) {
            stopTimer();
            root.setCenter(buildResultScreen(TOTAL_ROUNDS));
            return;
        }

        // Increase difficulty every 5 rounds
        if (round == 5) gridSize = 4;
        if (round == 10) gridSize = 5;

        roundLabel.setText("Daraja " + (round / 5 + 1) + "  |  Savol " + (round + 1) + "/" + TOTAL_ROUNDS);

        // Generate base color and slightly different one
        Random rng = new Random();
        float hue = rng.nextFloat() * 360f;
        float sat = 0.6f + rng.nextFloat() * 0.3f;
        float bright = 0.7f + rng.nextFloat() * 0.2f;

        // Difficulty: difference decreases with rounds
        float diff = Math.max(0.04f, 0.18f - round * 0.01f);
        float diffHue = hue + (diff * 30);

        Color baseColor = Color.hsb(hue, sat, bright);
        Color diffColor = Color.hsb(diffHue, sat, bright);

        int total = gridSize * gridSize;
        int diffIdx = rng.nextInt(total);

        GridPane grid = new GridPane();
        grid.setHgap(6);
        grid.setVgap(6);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < total; i++) {
            final int idx = i;
            Color cellColor = (i == diffIdx) ? diffColor : baseColor;
            String hex = toHex(cellColor);

            Button btn = new Button();
            btn.setPrefSize(70, 70);
            btn.setStyle("-fx-background-color: " + hex + "; -fx-background-radius: 10; " +
                    "-fx-cursor: hand; -fx-border-color: #1e2a3a; -fx-border-radius: 10; -fx-border-width: 2;");
            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: " + hex + "; -fx-background-radius: 10; " +
                    "-fx-cursor: hand; -fx-border-color: white; -fx-border-radius: 10; -fx-border-width: 2; " +
                    "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"));
            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: " + hex + "; -fx-background-radius: 10; " +
                    "-fx-cursor: hand; -fx-border-color: #1e2a3a; -fx-border-radius: 10; -fx-border-width: 2;"));

            btn.setOnAction(e -> {
                stopTimer();
                grid.setDisable(true);
                boolean correct = idx == diffIdx;
                if (correct) updateScore(10);
                showFeedback(container, correct, () -> {
                    round++;
                    showRound();
                });
            });

            grid.add(btn, i % gridSize, i / gridSize);
        }

        container.getChildren().add(grid);

        // Animate
        grid.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(300), grid);
        ft.setToValue(1); ft.play();

        startTimer(() -> {
            round++;
            showRound();
        });
    }

    private String toHex(Color c) {
        return String.format("#%02x%02x%02x",
            (int)(c.getRed() * 255),
            (int)(c.getGreen() * 255),
            (int)(c.getBlue() * 255));
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
