package com.vosiq.edugames.game;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.util.*;

public class CrosswordGame extends BaseGame {

    // Simple crossword: words + clues
    private static final String[][] WORDS = {
        {"ALMA", "Olma", "1"},         // Across
        {"NOK", "Nok", "2"},           // Down
        {"UZUM", "Uzum", "3"},         // Across
        {"OT", "Ot (hayvon)", "4"},    // Down
        {"GUL", "Gul (o'simlik)", "5"},// Across
        {"SUV", "H₂O", "6"},           // Down
        {"KITOB", "O'qiladigan narsa", "7"}, // Across
        {"MEVA", "Daraxtda o'sadigan", "8"}, // Down
    };

    private Map<Integer, TextField[]> wordFields = new HashMap<>();
    private VBox container;

    public void show() {
        initRoot();
        score = 0;

        root.setTop(buildTopBar("📝 " + LanguageManager.get("game.crossword"), "#43e97b"));

        container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20, 40, 20, 40));

        Label instructions = new Label("📝 So'z izohini ko'rib, to'g'ri so'zni yozing va TEKSHIR tugmasini bosing.");
        instructions.setStyle("-fx-font-size: 13px; -fx-text-fill: #a0aec0; -fx-wrap-text: true;");
        instructions.setMaxWidth(700);

        VBox wordList = buildWordList();

        Button checkBtn = new Button("✅ Tekshirish");
        checkBtn.setPrefWidth(200);
        checkBtn.setPrefHeight(45);
        checkBtn.setStyle("-fx-background-color: linear-gradient(to right, #43e97b, #38f9d7); " +
                "-fx-text-fill: #1a1a2e; -fx-font-size: 15px; -fx-font-weight: bold; " +
                "-fx-background-radius: 12; -fx-cursor: hand;");
        checkBtn.setOnAction(e -> checkAnswers());

        Button clearBtn = new Button("🗑️ Tozalash");
        clearBtn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 13px; " +
                "-fx-padding: 8 20; -fx-background-radius: 10; -fx-cursor: hand;");
        clearBtn.setOnAction(e -> clearAllFields());

        HBox btnRow = new HBox(15, checkBtn, clearBtn);
        btnRow.setAlignment(Pos.CENTER);

        container.getChildren().addAll(instructions, wordList, btnRow);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        root.setCenter(sp);
        root.setBottom(buildBackBottomBar());

        MainApp.primaryStage.getScene().setRoot(root);
    }

    private VBox buildWordList() {
        VBox list = new VBox(10);
        list.setMaxWidth(700);
        wordFields.clear();

        for (int i = 0; i < WORDS.length; i++) {
            String word = WORDS[i][0];
            String clue = WORDS[i][1];
            String num = WORDS[i][2];
            final int idx = i;

            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 15, 10, 15));
            row.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 12; " +
                    "-fx-border-color: #43e97b22; -fx-border-radius: 12; -fx-border-width: 1;");

            Label numLabel = new Label(num + ".");
            numLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #43e97b; -fx-min-width: 30;");

            Label clueLabel = new Label(clue);
            clueLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #a0aec0; -fx-min-width: 200;");

            // Letter boxes
            HBox letterBoxes = new HBox(5);
            letterBoxes.setAlignment(Pos.CENTER_LEFT);
            TextField[] fields = new TextField[word.length()];

            for (int j = 0; j < word.length(); j++) {
                TextField tf = new TextField();
                tf.setPrefSize(36, 36);
                tf.setMaxSize(36, 36);
                tf.setAlignment(Pos.CENTER);
                tf.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 16px; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6; -fx-border-color: #4a5568; " +
                        "-fx-border-radius: 6; -fx-border-width: 1;");

                // Auto-uppercase and max 1 char
                final int jFinal = j;
                tf.textProperty().addListener((obs, old, newVal) -> {
                    if (newVal.length() > 1) {
                        tf.setText(newVal.substring(0, 1).toUpperCase());
                        // Move to next field
                        if (jFinal + 1 < fields.length) fields[jFinal + 1].requestFocus();
                    } else {
                        tf.setText(newVal.toUpperCase());
                        if (!newVal.isEmpty() && jFinal + 1 < fields.length) {
                            fields[jFinal + 1].requestFocus();
                        }
                    }
                });

                fields[j] = tf;
                letterBoxes.getChildren().add(tf);
            }

            wordFields.put(idx, fields);
            row.getChildren().addAll(numLabel, clueLabel, letterBoxes);
            list.getChildren().add(row);
        }

        return list;
    }

    private void checkAnswers() {
        int correct = 0;
        StringBuilder results = new StringBuilder();

        for (int i = 0; i < WORDS.length; i++) {
            String word = WORDS[i][0];
            TextField[] fields = wordFields.get(i);
            StringBuilder entered = new StringBuilder();
            for (TextField f : fields) entered.append(f.getText());

            boolean isCorrect = entered.toString().equals(word);
            if (isCorrect) {
                correct++;
                // Green styling
                for (TextField f : fields) {
                    f.setStyle("-fx-background-color: #43e97b33; -fx-text-fill: #43e97b; -fx-font-size: 16px; " +
                            "-fx-font-weight: bold; -fx-background-radius: 6; -fx-border-color: #43e97b; " +
                            "-fx-border-radius: 6; -fx-border-width: 2;");
                }
            } else {
                // Red styling
                for (TextField f : fields) {
                    f.setStyle("-fx-background-color: #f5656533; -fx-text-fill: #f56565; -fx-font-size: 16px; " +
                            "-fx-font-weight: bold; -fx-background-radius: 6; -fx-border-color: #f56565; " +
                            "-fx-border-radius: 6; -fx-border-width: 2;");
                }
            }
        }

        score = correct * 10;
        scoreLabel.setText(LanguageManager.get("game.score") + ": " + score);

        // Show result popup
        String msg = correct + " / " + WORDS.length + " to'g'ri!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(LanguageManager.get("game.result"));
        alert.setHeaderText(msg);
        alert.setContentText(correct == WORDS.length ? "🏆 Barcha so'zlar to'g'ri!" :
                             correct >= WORDS.length / 2 ? "👍 Yaxshi urinish!" : "💪 Ko'proq mashq kerak!");
        alert.getDialogPane().setStyle("-fx-background-color: #1e2a3a;");
        Label header = (Label) alert.getDialogPane().lookup(".header-panel .label");
        if (header != null) header.setStyle("-fx-text-fill: #43e97b; -fx-font-size: 18px;");
        alert.showAndWait();
    }

    private void clearAllFields() {
        for (TextField[] fields : wordFields.values()) {
            for (TextField f : fields) {
                f.setText("");
                f.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 16px; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6; -fx-border-color: #4a5568; " +
                        "-fx-border-radius: 6; -fx-border-width: 1;");
            }
        }
        score = 0;
        scoreLabel.setText(LanguageManager.get("game.score") + ": 0");
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
