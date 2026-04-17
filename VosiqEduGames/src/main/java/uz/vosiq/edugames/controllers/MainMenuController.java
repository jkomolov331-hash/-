package uz.vosiq.edugames.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uz.vosiq.edugames.MainApp;
import uz.vosiq.edugames.utils.LanguageManager;

public class MainMenuController {

    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Button btnGames;
    @FXML private Button btnAbout;
    @FXML private Button btnExit;
    @FXML private Button btnLangUz;
    @FXML private Button btnLangRu;
    @FXML private Button btnLangEn;
    @FXML private ImageView logoView;

    @FXML
    public void initialize() {
        updateTexts();
        loadLogo();
    }

    private void loadLogo() {
        try {
            Image img = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoView.setImage(img);
        } catch (Exception e) {
            System.out.println("Logo load failed: " + e.getMessage());
        }
    }

    private void updateTexts() {
        titleLabel.setText(LanguageManager.get("app.title"));
        subtitleLabel.setText(LanguageManager.get("app.subtitle"));
        btnGames.setText(LanguageManager.get("menu.games"));
        btnAbout.setText(LanguageManager.get("menu.about"));
        btnExit.setText(LanguageManager.get("menu_exit"));
        btnLangUz.setText(LanguageManager.get("lang_uz"));
        btnLangRu.setText(LanguageManager.get("lang_ru"));
        btnLangEn.setText(LanguageManager.get("lang_en"));
        highlightActiveLang();
    }

    private void highlightActiveLang() {
        String normal = "-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: white;";
        String active = "-fx-background-color: #FFD700; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;";
        btnLangUz.setStyle(normal);
        btnLangRu.setStyle(normal);
        btnLangEn.setStyle(normal);
        switch (LanguageManager.getLang()) {
            case "uz": btnLangUz.setStyle(active); break;
            case "ru": btnLangRu.setStyle(active); break;
            case "en": btnLangEn.setStyle(active); break;
        }
    }

    @FXML private void openGames() {
        loadScene("/fxml/SubjectSelect.fxml");
    }

    @FXML private void openAbout() {
        loadScene("/fxml/About.fxml");
    }

    @FXML private void setLangUz() { LanguageManager.setLanguage("uz"); reload(); }
    @FXML private void setLangRu() { LanguageManager.setLanguage("ru"); reload(); }
    @FXML private void setLangEn() { LanguageManager.setLanguage("en"); reload(); }

    private void reload() { loadScene("/fxml/MainMenu.fxml"); }

    private void loadScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            MainApp.primaryStage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void exitApp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageManager.get("menu_exit"));
        alert.setHeaderText(null);
        alert.setContentText("Ilovadan chiqmoqchimisiz? / Выйти? / Exit the app?");
        alert.showAndWait().ifPresent(r -> { if (r == ButtonType.OK) System.exit(0); });
    }
}
