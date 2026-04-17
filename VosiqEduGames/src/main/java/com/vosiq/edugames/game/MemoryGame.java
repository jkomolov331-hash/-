package com.vosiq.edugames.game;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.*;

public class MemoryGame extends BaseGame {

    private List<String> cards;
    private Button[] cardButtons;
    private String[] cardValues;
    private boolean[] revealed;
    private int firstIdx = -1;
    private int secondIdx = -1;
    private int pairs = 0;
    private int moves = 0;
    private Label movesLabel;
    private Label pairsLabel;

    private static final String[] EMOJIS = {
        "🐶","🐱","🐭","🐹","🐰","🦊","🐻","🐼",
        "🐨","🐯","🦁","🐮","🐷","🐸","🐵","🐔"
    };

    public void show() {
        initRoot();
        score = 0;
        pairs = 0;
        moves = 0;
        firstIdx = -1;
        secondIdx = -1;

        root.setTop(buildTopBar("🃏 " + LanguageManager.get("game.memory"), "#4facfe"));

        // Prepare cards (8 pairs)
        List<String> emojiList = Arrays.asList(Arrays.copyOf(EMOJIS, 8));
        cards = new ArrayList<>();
        cards.addAll(emojiList);
        cards.addAll(emojiList);
        Collections.shuffle(cards);

        cardValues = cards.toArray(new String[0]);
        revealed = new boolean[cards.size()];
        cardButtons = new Button[cards.size()];

        // Stats bar
        HBox stats = new HBox(30);
        stats.setAlignment(Pos.CENTER);
        stats.setPadding(new Insets(10));

        movesLabel = new Label("Harakatlar: 0");
        movesLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #a0aec0; -fx-font-weight: bold;");

        pairsLabel = new Label("Juftlar: 0 / 8");
        pairsLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #43e97b; -fx-font-weight: bold;");

        stats.getChildren().addAll(movesLabel, pairsLabel);

        // Grid of cards
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));

        for (int i = 0; i < cards.size(); i++) {
            final int idx = i;
            Button btn = new Button("?");
            btn.setPrefSize(80, 80);
            btn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 28px; " +
                    "-fx-background-radius: 14; -fx-cursor: hand; -fx-border-color: #4facfe33; " +
                    "-fx-border-radius: 14; -fx-border-width: 1;");
            btn.setOnAction(e -> flipCard(idx));
            cardButtons[i] = btn;

            // Animate in
            btn.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.millis(300), btn);
            ft.setToValue(1);
            ft.setDelay(Duration.millis(i * 40));
            ft.play();

            grid.add(btn, i % 4, i / 4);
        }

        VBox center = new VBox(15, stats, grid);
        center.setAlignment(Pos.CENTER);

        root.setCenter(center);
        root.setBottom(buildBackBottomBar());

        MainApp.primaryStage.getScene().setRoot(root);
    }

    private void flipCard(int idx) {
        if (revealed[idx]) return;
        if (secondIdx != -1) return;

        cardButtons[idx].setText(cardValues[idx]);
        cardButtons[idx].setStyle("-fx-background-color: #4facfe22; -fx-text-fill: white; -fx-font-size: 28px; " +
                "-fx-background-radius: 14; -fx-cursor: hand; -fx-border-color: #4facfe; " +
                "-fx-border-radius: 14; -fx-border-width: 2;");

        if (firstIdx == -1) {
            firstIdx = idx;
        } else if (idx != firstIdx) {
            secondIdx = idx;
            moves++;
            movesLabel.setText("Harakatlar: " + moves);

            PauseTransition pause = new PauseTransition(Duration.millis(600));
            pause.setOnFinished(e -> checkMatch());
            pause.play();
        }
    }

    private void checkMatch() {
        if (cardValues[firstIdx].equals(cardValues[secondIdx])) {
            // Match!
            revealed[firstIdx] = true;
            revealed[secondIdx] = true;
            cardButtons[firstIdx].setStyle("-fx-background-color: #43e97b33; -fx-text-fill: white; -fx-font-size: 28px; " +
                    "-fx-background-radius: 14; -fx-border-color: #43e97b; -fx-border-radius: 14; -fx-border-width: 2;");
            cardButtons[secondIdx].setStyle("-fx-background-color: #43e97b33; -fx-text-fill: white; -fx-font-size: 28px; " +
                    "-fx-background-radius: 14; -fx-border-color: #43e97b; -fx-border-radius: 14; -fx-border-width: 2;");
            pairs++;
            score += 15;
            scoreLabel.setText(LanguageManager.get("game.score") + ": " + score);
            pairsLabel.setText("Juftlar: " + pairs + " / 8");

            if (pairs == 8) {
                PauseTransition win = new PauseTransition(Duration.millis(500));
                win.setOnFinished(e -> root.setCenter(buildResultScreen(8)));
                win.play();
            }
        } else {
            // No match
            cardButtons[firstIdx].setText("?");
            cardButtons[firstIdx].setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 28px; " +
                    "-fx-background-radius: 14; -fx-cursor: hand; -fx-border-color: #4facfe33; " +
                    "-fx-border-radius: 14; -fx-border-width: 1;");
            cardButtons[secondIdx].setText("?");
            cardButtons[secondIdx].setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 28px; " +
                    "-fx-background-radius: 14; -fx-cursor: hand; -fx-border-color: #4facfe33; " +
                    "-fx-border-radius: 14; -fx-border-width: 1;");
        }

        firstIdx = -1;
        secondIdx = -1;
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
