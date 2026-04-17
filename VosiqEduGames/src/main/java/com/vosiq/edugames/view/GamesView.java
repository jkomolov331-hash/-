package com.vosiq.edugames.view;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.game.*;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class GamesView {

    private BorderPane root;

    public GamesView() { build(); }
    public BorderPane getRoot() { return root; }

    private void build() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f3460);");

        root.setTop(buildHeader());
        root.setCenter(buildGrid());
        root.setBottom(buildBackBtn());
    }

    private VBox buildHeader() {
        VBox h = new VBox(6);
        h.setAlignment(Pos.CENTER);
        h.setPadding(new Insets(25, 20, 10, 20));

        Label title = new Label("🎮 " + LanguageManager.get("menu.games"));
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #667eea;");

        Label sub = new Label("O'yin turini tanlang");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096;");

        h.getChildren().addAll(title, sub);
        return h;
    }

    private ScrollPane buildGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 40, 20, 40));

        Object[][] games = {
            {"🧠", LanguageManager.get("game.quiz"), "Savollar va javoblar", "#667eea", "#764ba2", "quiz"},
            {"✅", LanguageManager.get("game.truefalse"), "To'g'ri yoki noto'g'ri?", "#f093fb", "#f5576c", "truefalse"},
            {"🃏", LanguageManager.get("game.memory"), "Xotirangizni sinang", "#4facfe", "#00f2fe", "memory"},
            {"📝", LanguageManager.get("game.crossword"), "So'zlarni toping", "#43e97b", "#38f9d7", "crossword"},
            {"🏳️", LanguageManager.get("game.flags"), "Bayroqlarni bil", "#fa709a", "#fee140", "flags"},
            {"🤼", LanguageManager.get("game.tugofwar"), "Jamoaviy raqobat", "#a18cd1", "#fbc2eb", "tugofwar"},
            {"🎨", LanguageManager.get("game.colors"), "Farqli rangni top", "#fccb90", "#d57eeb", "colors"},
            {"✏️", LanguageManager.get("game.custom"), "O'z savollaringiz", "#96fbc4", "#f9f586", "custom"},
        };

        int col = 0, row = 0;
        for (Object[] g : games) {
            Node card = createGameCard(
                (String)g[0], (String)g[1], (String)g[2],
                (String)g[3], (String)g[4], (String)g[5]);
            grid.add(card, col, row);
            col++;
            if (col == 4) { col = 0; row++; }
        }

        // Animate
        int delay = 0;
        for (Node c : grid.getChildren()) {
            animateIn(c, delay);
            delay += 80;
        }

        ScrollPane sp = new ScrollPane(grid);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return sp;
    }

    private Node createGameCard(String emoji, String title, String desc,
                                 String c1, String c2, String gameId) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(200, 150);
        card.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 18; " +
                "-fx-border-color: " + c1 + "33; -fx-border-radius: 18; -fx-border-width: 1.5; " +
                "-fx-cursor: hand;");

        Label emojiL = new Label(emoji);
        emojiL.setStyle("-fx-font-size: 30px;");

        Label titleL = new Label(title);
        titleL.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleL.setWrapText(true);
        titleL.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label descL = new Label(desc);
        descL.setStyle("-fx-font-size: 10px; -fx-text-fill: #718096;");

        card.getChildren().addAll(emojiL, titleL, descL);

        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: " + c1 + "22; -fx-background-radius: 18; " +
                "-fx-border-color: " + c1 + "; -fx-border-radius: 18; -fx-border-width: 2; " +
                "-fx-cursor: hand; -fx-scale-x: 1.05; -fx-scale-y: 1.05; " +
                "-fx-effect: dropshadow(gaussian, " + c1 + "88, 15, 0, 0, 0);"));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #1e2a3a; -fx-background-radius: 18; " +
                "-fx-border-color: " + c1 + "33; -fx-border-radius: 18; -fx-border-width: 1.5; " +
                "-fx-cursor: hand;"));

        card.setOnMouseClicked(e -> launchGame(gameId));
        return card;
    }

    private void launchGame(String gameId) {
        switch (gameId) {
            case "quiz" -> new QuizGame().show(null);
            case "truefalse" -> new TrueFalseGame().show(null);
            case "memory" -> new MemoryGame().show();
            case "crossword" -> new CrosswordGame().show();
            case "flags" -> new FlagsGame().show();
            case "tugofwar" -> new TugOfWarGame().show();
            case "colors" -> new ColorsGame().show();
            case "custom" -> new CustomQuizGame().show();
        }
    }

    private HBox buildBackBtn() {
        HBox box = new HBox();
        box.setPadding(new Insets(10, 20, 15, 20));
        Button back = new Button("← " + LanguageManager.get("menu.back"));
        back.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-padding: 8 20; " +
                "-fx-background-radius: 10; -fx-cursor: hand;");
        back.setOnAction(e -> {
            MainApp.primaryStage.getScene().setRoot(new MainMenuView().getRoot());
        });
        box.getChildren().add(back);
        return box;
    }

    private void animateIn(Node n, int delayMs) {
        n.setOpacity(0);
        n.setTranslateY(20);
        FadeTransition ft = new FadeTransition(Duration.millis(400), n);
        ft.setToValue(1); ft.setDelay(Duration.millis(delayMs)); ft.play();
        TranslateTransition tt = new TranslateTransition(Duration.millis(400), n);
        tt.setToY(0); tt.setDelay(Duration.millis(delayMs)); tt.play();
    }
}
