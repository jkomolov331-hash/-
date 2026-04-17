package com.vosiq.edugames.view;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.game.QuizGame;
import com.vosiq.edugames.game.TrueFalseGame;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class SubjectsView {

    private BorderPane root;

    public SubjectsView() { build(); }
    public BorderPane getRoot() { return root; }

    private void build() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f3460);");
        root.setTop(buildHeader());
        root.setCenter(buildSubjectGrid());
        root.setBottom(buildBackBtn());
    }

    private VBox buildHeader() {
        VBox h = new VBox(6);
        h.setAlignment(Pos.CENTER);
        h.setPadding(new Insets(25, 20, 10, 20));
        Label t = new Label("📚 " + LanguageManager.get("menu.subjects"));
        t.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #f093fb;");
        Label s = new Label("Fan tanlang, keyin o'yin rejimini belgilang");
        s.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096;");
        h.getChildren().addAll(t, s);
        return h;
    }

    private ScrollPane buildSubjectGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));

        Object[][] subjects = {
            {"➕", LanguageManager.get("subject.math"), "#667eea", "math"},
            {"🇬🇧", LanguageManager.get("subject.english"), "#f093fb", "english"},
            {"🇺🇿", LanguageManager.get("subject.uzbek"), "#43e97b", "uzbek"},
            {"🇷🇺", LanguageManager.get("subject.russian"), "#f5576c", "russian"},
            {"💻", LanguageManager.get("subject.informatics"), "#4facfe", "informatics"},
            {"🌍", LanguageManager.get("subject.geography"), "#fa709a", "geography"},
            {"🧬", LanguageManager.get("subject.biology"), "#a18cd1", "biology"},
            {"📖", LanguageManager.get("subject.literature"), "#fccb90", "literature"},
            {"📜", LanguageManager.get("subject.history"), "#96fbc4", "history"},
            {"⚗️", LanguageManager.get("subject.chemistry"), "#fbc2eb", "chemistry"},
        };

        int col = 0, row = 0;
        for (Object[] s : subjects) {
            Node card = createSubjectCard((String)s[0], (String)s[1], (String)s[2], (String)s[3]);
            grid.add(card, col, row);
            col++;
            if (col == 5) { col = 0; row++; }
        }

        int d = 0;
        for (Node c : grid.getChildren()) { animateIn(c, d); d += 70; }

        ScrollPane sp = new ScrollPane(grid);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return sp;
    }

    private Node createSubjectCard(String emoji, String name, String color, String subjectId) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(170, 130);
        card.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 16; " +
                "-fx-border-color: " + color + "33; -fx-border-radius: 16; -fx-border-width: 1.5; " +
                "-fx-cursor: hand;");

        Label emojiL = new Label(emoji);
        emojiL.setStyle("-fx-font-size: 28px;");

        Label nameL = new Label(name);
        nameL.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: white;");
        nameL.setWrapText(true);
        nameL.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(emojiL, nameL);

        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: " + color + "22; -fx-background-radius: 16; " +
                "-fx-border-color: " + color + "; -fx-border-radius: 16; -fx-border-width: 2; " +
                "-fx-cursor: hand; -fx-scale-x: 1.06; -fx-scale-y: 1.06;"));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #1e2a3a; -fx-background-radius: 16; " +
                "-fx-border-color: " + color + "33; -fx-border-radius: 16; -fx-border-width: 1.5; " +
                "-fx-cursor: hand;"));
        card.setOnMouseClicked(e -> showModeSelection(subjectId, name, color));

        return card;
    }

    // Show 2 buttons: Random OR Custom
    private void showModeSelection(String subjectId, String subjectName, String color) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(subjectName);
        dialog.setHeaderText(LanguageManager.get("game.choosemode"));

        DialogPane dp = dialog.getDialogPane();
        dp.setStyle("-fx-background-color: #1e2a3a;");

        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        Label header = new Label("📚 " + subjectName);
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label chooseLabel = new Label(LanguageManager.get("game.choosemode") + ":");
        chooseLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #a0aec0;");

        Button randomBtn = new Button("🎲 " + LanguageManager.get("game.randommode"));
        randomBtn.setPrefWidth(280);
        randomBtn.setPrefHeight(50);
        randomBtn.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                "-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; " +
                "-fx-background-radius: 12; -fx-cursor: hand;");

        Button customBtn = new Button("✏️ " + LanguageManager.get("game.custommode"));
        customBtn.setPrefWidth(280);
        customBtn.setPrefHeight(50);
        customBtn.setStyle("-fx-background-color: linear-gradient(to right, #43e97b, #38f9d7); " +
                "-fx-text-fill: #1a1a2e; -fx-font-size: 15px; -fx-font-weight: bold; " +
                "-fx-background-radius: 12; -fx-cursor: hand;");

        Label hint = new Label("Random: bank savollar | Custom: o'z savollaringiz");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: #4a5568;");

        content.getChildren().addAll(header, chooseLabel, randomBtn, customBtn, hint);
        dp.setContent(content);

        // Game type selector
        Label gameLabel = new Label("O'yin turi:");
        gameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #a0aec0;");

        ComboBox<String> gameType = new ComboBox<>();
        gameType.getItems().addAll(
            LanguageManager.get("game.quiz"),
            LanguageManager.get("game.truefalse")
        );
        gameType.setValue(LanguageManager.get("game.quiz"));
        gameType.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white;");
        content.getChildren().add(2, gameLabel);
        content.getChildren().add(3, gameType);

        dp.getButtonTypes().add(ButtonType.CANCEL);
        Button cancelBtn = (Button) dp.lookupButton(ButtonType.CANCEL);
        cancelBtn.setText("← " + LanguageManager.get("menu.back"));
        cancelBtn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-background-radius: 8;");

        randomBtn.setOnAction(e -> {
            dialog.close();
            String selected = gameType.getValue();
            if (selected.equals(LanguageManager.get("game.truefalse"))) {
                new TrueFalseGame().show(subjectId);
            } else {
                new QuizGame().show(subjectId);
            }
        });

        customBtn.setOnAction(e -> {
            dialog.close();
            new com.vosiq.edugames.game.CustomQuizGame().show();
        });

        dialog.showAndWait();
    }

    private HBox buildBackBtn() {
        HBox box = new HBox();
        box.setPadding(new Insets(10, 20, 15, 20));
        Button back = new Button("← " + LanguageManager.get("menu.back"));
        back.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-padding: 8 20; " +
                "-fx-background-radius: 10; -fx-cursor: hand;");
        back.setOnAction(e -> MainApp.primaryStage.getScene().setRoot(new MainMenuView().getRoot()));
        box.getChildren().add(back);
        return box;
    }

    private void animateIn(Node n, int d) {
        n.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(350), n);
        ft.setToValue(1); ft.setDelay(Duration.millis(d)); ft.play();
    }
}
