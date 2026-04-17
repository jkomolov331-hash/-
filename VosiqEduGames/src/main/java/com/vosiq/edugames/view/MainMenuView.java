package com.vosiq.edugames.view;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.util.Duration;

public class MainMenuView {

    private BorderPane root;

    public MainMenuView() {
        build();
    }

    public BorderPane getRoot() { return root; }

    private void build() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f3460);");

        // Header
        VBox header = buildHeader();
        root.setTop(header);

        // Center menu
        VBox center = buildCenterMenu();
        root.setCenter(center);

        // Footer
        HBox footer = buildFooter();
        root.setBottom(footer);

        // Decorative floating circles
        addDecorativeCircles();
    }

    private VBox buildHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(30, 20, 10, 20));

        // School name
        Label schoolLabel = new Label("VOSIQ INTERNATIONAL SCHOOL");
        schoolLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #a0aec0; -fx-font-weight: bold; -fx-letter-spacing: 3;");

        // App title
        Label titleLabel = new Label("🎮 EduGames");
        titleLabel.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; " +
                "-fx-text-fill: linear-gradient(to right, #667eea, #764ba2);");

        // Apply gradient via effect
        titleLabel.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-text-fill: #667eea;");

        // Subtitle
        Label subtitle = new Label(LanguageManager.get("menu.games") + " · " +
                LanguageManager.get("menu.subjects") + " · " + "Fun Learning!");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #718096;");

        // Language selector
        HBox langBox = buildLanguageSelector();

        header.getChildren().addAll(schoolLabel, titleLabel, subtitle, langBox);

        // Animate title
        FadeTransition fade = new FadeTransition(Duration.millis(1000), titleLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        return header;
    }

    private HBox buildLanguageSelector() {
        HBox langBox = new HBox(8);
        langBox.setAlignment(Pos.CENTER);
        langBox.setPadding(new Insets(10, 0, 0, 0));

        Label langLabel = new Label("🌐");
        langLabel.setStyle("-fx-text-fill: #a0aec0; -fx-font-size: 16px;");

        Button btnUz = createLangButton("O'ZB", "uz");
        Button btnRu = createLangButton("РУС", "ru");
        Button btnEn = createLangButton("ENG", "en");

        langBox.getChildren().addAll(langLabel, btnUz, btnRu, btnEn);
        return langBox;
    }

    private Button createLangButton(String text, String lang) {
        Button btn = new Button(text);
        boolean active = LanguageManager.getLanguage().equals(lang);
        btn.setStyle("-fx-background-color: " + (active ? "#667eea" : "#2d3748") + "; " +
                "-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; " +
                "-fx-padding: 6 14; -fx-background-radius: 20; -fx-cursor: hand;");
        btn.setOnAction(e -> {
            LanguageManager.setLanguage(lang);
            refreshScene();
        });
        return btn;
    }

    private VBox buildCenterMenu() {
        VBox center = new VBox(20);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20, 40, 20, 40));

        // Menu cards grid
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Main menu items
        Node gamesCard = createMenuCard("🎮", LanguageManager.get("menu.games"),
                "Barcha o'yinlar", "#667eea", "#764ba2", () -> goToGames());
        Node subjectsCard = createMenuCard("📚", LanguageManager.get("menu.subjects"),
                "Fan bo'yicha o'ynash", "#f093fb", "#f5576c", () -> goToSubjects());
        Node aboutCard = createMenuCard("ℹ️", LanguageManager.get("menu.about"),
                "Ilova haqida ma'lumot", "#4facfe", "#00f2fe", () -> goToAbout());
        Node settingsCard = createMenuCard("⚙️", LanguageManager.get("menu.settings"),
                "Sozlamalar", "#43e97b", "#38f9d7", () -> goToSettings());

        grid.add(gamesCard, 0, 0);
        grid.add(subjectsCard, 1, 0);
        grid.add(aboutCard, 0, 1);
        grid.add(settingsCard, 1, 1);

        center.getChildren().add(grid);

        // Animate cards
        int delay = 0;
        for (Node card : grid.getChildren()) {
            animateCard(card, delay);
            delay += 150;
        }

        return center;
    }

    private Node createMenuCard(String emoji, String title, String subtitle,
                                 String color1, String color2, Runnable action) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(220, 160);
        card.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 20; " +
                "-fx-border-color: " + color1 + "33; -fx-border-radius: 20; -fx-border-width: 1.5; " +
                "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 4);");

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 36px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subLabel = new Label(subtitle);
        subLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #718096; -fx-wrap-text: true;");
        subLabel.setTextAlignment(TextAlignment.CENTER);

        card.getChildren().addAll(emojiLabel, titleLabel, subLabel);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, " + color1 + "33, " + color2 + "22); " +
                "-fx-background-radius: 20; -fx-border-color: " + color1 + "; -fx-border-radius: 20; " +
                "-fx-border-width: 2; -fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, " + color1 + "88, 20, 0, 0, 0); " +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #1e2a3a; -fx-background-radius: 20; " +
                "-fx-border-color: " + color1 + "33; -fx-border-radius: 20; -fx-border-width: 1.5; " +
                "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 4);"));
        card.setOnMouseClicked(e -> action.run());

        return card;
    }

    private HBox buildFooter() {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(10, 20, 15, 20));

        Button exitBtn = new Button("🚪 " + LanguageManager.get("menu.exit"));
        exitBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4a5568; " +
                "-fx-font-size: 13px; -fx-cursor: hand; -fx-underline: true;");
        exitBtn.setOnAction(e -> System.exit(0));
        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #f56565; " +
                "-fx-font-size: 13px; -fx-cursor: hand; -fx-underline: true;"));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #4a5568; " +
                "-fx-font-size: 13px; -fx-cursor: hand; -fx-underline: true;"));

        footer.getChildren().add(exitBtn);
        return footer;
    }

    private void addDecorativeCircles() {
        // Background decorative circles (subtle)
        StackPane stack = new StackPane();
        stack.setMouseTransparent(true);

        for (int i = 0; i < 5; i++) {
            Circle c = new Circle(30 + i * 20);
            c.setFill(Color.TRANSPARENT);
            c.setStroke(Color.web("#667eea", 0.1 - i * 0.015));
            c.setStrokeWidth(1);
            stack.getChildren().add(c);

            RotateTransition rt = new RotateTransition(Duration.seconds(10 + i * 3), c);
            rt.setByAngle(360);
            rt.setCycleCount(Animation.INDEFINITE);
            rt.play();
        }

        root.setStyle(root.getStyle()); // keep background
    }

    private void animateCard(Node card, int delayMs) {
        card.setOpacity(0);
        card.setTranslateY(30);

        TranslateTransition tt = new TranslateTransition(Duration.millis(500), card);
        tt.setToY(0);
        tt.setDelay(Duration.millis(delayMs));

        FadeTransition ft = new FadeTransition(Duration.millis(500), card);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delayMs));

        ft.play();
        tt.play();
    }

    private void goToGames() {
        GamesView view = new GamesView();
        MainApp.primaryStage.getScene().setRoot(view.getRoot());
    }

    private void goToSubjects() {
        SubjectsView view = new SubjectsView();
        MainApp.primaryStage.getScene().setRoot(view.getRoot());
    }

    private void goToAbout() {
        AboutView view = new AboutView();
        MainApp.primaryStage.getScene().setRoot(view.getRoot());
    }

    private void goToSettings() {
        SettingsView view = new SettingsView();
        MainApp.primaryStage.getScene().setRoot(view.getRoot());
    }

    private void refreshScene() {
        MainMenuView fresh = new MainMenuView();
        MainApp.primaryStage.getScene().setRoot(fresh.getRoot());
    }
}
