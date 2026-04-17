package com.vosiq.edugames.view;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class AboutView {

    private BorderPane root;

    public AboutView() { build(); }
    public BorderPane getRoot() { return root; }

    private void build() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f3460);");

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        // Logo area
        VBox logoBox = new VBox(5);
        logoBox.setAlignment(Pos.CENTER);
        Label appEmoji = new Label("🎮");
        appEmoji.setStyle("-fx-font-size: 64px;");
        Label appName = new Label(LanguageManager.get("about.appname"));
        appName.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #667eea;");
        Label version = new Label(LanguageManager.get("about.version"));
        version.setStyle("-fx-font-size: 14px; -fx-text-fill: #4a5568;");
        logoBox.getChildren().addAll(appEmoji, appName, version);

        // Separator
        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: #2d3748; -fx-max-width: 400;");

        // School info card
        VBox schoolCard = createInfoCard("🏫", LanguageManager.get("about.school"),
                "Vosiq tumani, O'zbekiston", "#667eea");

        // Author card
        VBox authorCard = createInfoCard("👨‍🎓", LanguageManager.get("about.author"),
                LanguageManager.get("about.class") + " · " + LanguageManager.get("about.year"), "#43e97b");

        // Description card
        VBox descCard = new VBox(10);
        descCard.setAlignment(Pos.CENTER);
        descCard.setPadding(new Insets(20));
        descCard.setMaxWidth(600);
        descCard.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 16; " +
                "-fx-border-color: #4facfe33; -fx-border-radius: 16; -fx-border-width: 1;");

        Label descTitle = new Label("📋 Ilova haqida");
        descTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4facfe;");

        Label descText = new Label(LanguageManager.get("about.desc"));
        descText.setStyle("-fx-font-size: 13px; -fx-text-fill: #a0aec0; -fx-wrap-text: true;");
        descText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        descText.setMaxWidth(560);

        descCard.getChildren().addAll(descTitle, descText);

        // Tech stack
        VBox techCard = new VBox(8);
        techCard.setAlignment(Pos.CENTER);
        techCard.setPadding(new Insets(15));
        techCard.setMaxWidth(600);
        techCard.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 16; " +
                "-fx-border-color: #f093fb33; -fx-border-radius: 16; -fx-border-width: 1;");

        Label techTitle = new Label("⚙️ Texnologiyalar");
        techTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #f093fb;");

        HBox techRow = new HBox(15);
        techRow.setAlignment(Pos.CENTER);
        for (String tech : new String[]{"☕ Java 17", "🎨 JavaFX 21", "📦 Maven", "🌐 3 til"}) {
            Label t = new Label(tech);
            t.setStyle("-fx-font-size: 12px; -fx-text-fill: #a0aec0; -fx-background-color: #2d3748; " +
                    "-fx-padding: 4 10; -fx-background-radius: 8;");
            techRow.getChildren().add(t);
        }
        techCard.getChildren().addAll(techTitle, techRow);

        // Games count
        Label gamesInfo = new Label("🎮 7+ o'yin turi  |  📚 10 fan  |  🌐 3 til");
        gamesInfo.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096;");

        content.getChildren().addAll(logoBox, sep1, schoolCard, authorCard, descCard, techCard, gamesInfo);

        // Animate
        content.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(600), content);
        ft.setToValue(1);
        ft.play();

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        root.setCenter(sp);
        root.setBottom(buildBackBtn());
    }

    private VBox createInfoCard(String emoji, String title, String subtitle, String color) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18));
        card.setMaxWidth(500);
        card.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 16; " +
                "-fx-border-color: " + color + "44; -fx-border-radius: 16; -fx-border-width: 1.5;");

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER);

        Label emojiL = new Label(emoji);
        emojiL.setStyle("-fx-font-size: 24px;");

        VBox texts = new VBox(3);
        Label titleL = new Label(title);
        titleL.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label subL = new Label(subtitle);
        subL.setStyle("-fx-font-size: 12px; -fx-text-fill: #718096;");
        texts.getChildren().addAll(titleL, subL);

        row.getChildren().addAll(emojiL, texts);
        card.getChildren().add(row);
        return card;
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
