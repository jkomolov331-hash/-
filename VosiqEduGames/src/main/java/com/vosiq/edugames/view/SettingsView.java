package com.vosiq.edugames.view;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.util.LanguageManager;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SettingsView {

    private BorderPane root;

    public SettingsView() { build(); }
    public BorderPane getRoot() { return root; }

    private void build() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f3460);");

        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));

        Label title = new Label("⚙️ " + LanguageManager.get("menu.settings"));
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #43e97b;");

        // Language section
        VBox langCard = new VBox(15);
        langCard.setAlignment(Pos.CENTER);
        langCard.setPadding(new Insets(25));
        langCard.setMaxWidth(450);
        langCard.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 16; " +
                "-fx-border-color: #43e97b44; -fx-border-radius: 16; -fx-border-width: 1.5;");

        Label langTitle = new Label("🌐 " + LanguageManager.get("menu.language"));
        langTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #43e97b;");

        VBox langBtns = new VBox(10);
        langBtns.setAlignment(Pos.CENTER);

        String[][] langs = {
            {LanguageManager.get("settings.lang.uz"), "uz", "🇺🇿"},
            {LanguageManager.get("settings.lang.ru"), "ru", "🇷🇺"},
            {LanguageManager.get("settings.lang.en"), "en", "🇬🇧"},
        };

        for (String[] lang : langs) {
            Button btn = new Button(lang[2] + "  " + lang[0]);
            btn.setPrefWidth(300);
            btn.setPrefHeight(45);
            boolean active = LanguageManager.getLanguage().equals(lang[1]);
            btn.setStyle("-fx-background-color: " + (active ? "#43e97b" : "#2d3748") + "; " +
                    "-fx-text-fill: " + (active ? "#1a1a2e" : "white") + "; " +
                    "-fx-font-size: 14px; -fx-font-weight: bold; " +
                    "-fx-background-radius: 10; -fx-cursor: hand;");
            final String langCode = lang[1];
            btn.setOnAction(e -> {
                LanguageManager.setLanguage(langCode);
                MainApp.primaryStage.getScene().setRoot(new SettingsView().getRoot());
            });
            langBtns.getChildren().add(btn);
        }

        langCard.getChildren().addAll(langTitle, langBtns);

        // App info mini
        Label info = new Label("Vosiq EduGames v1.0.0  ·  2025");
        info.setStyle("-fx-font-size: 12px; -fx-text-fill: #4a5568;");

        content.getChildren().addAll(title, langCard, info);
        root.setCenter(content);
        root.setBottom(buildBackBtn());
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
}
