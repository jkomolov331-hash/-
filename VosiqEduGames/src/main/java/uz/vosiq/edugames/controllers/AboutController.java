package uz.vosiq.edugames.controllers;
import uz.vosiq.edugames.MainApp;
import uz.vosiq.edugames.utils.LanguageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.util.Objects;

public class AboutController {
    @FXML private ImageView logoView;
    @FXML private Label titleLabel, appNameLabel, versionLabel, schoolLabel, creatorLabel, classLabel, yearLabel, descLabel;
    @FXML private Button btnBack;

    @FXML public void initialize() {
        try { logoView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")))); } catch(Exception ignored){}
        String lang = LanguageManager.getLang();
        titleLabel.setText(LanguageManager.get("about.title"));
        appNameLabel.setText("Vosiq EduGames");
        versionLabel.setText(LanguageManager.get("about.version.label") + ": 1.0.0");
        schoolLabel.setText("🏫 Vosiq International School");
        if("ru".equals(lang)) {
            creatorLabel.setText("👨‍💻 Создатель: Наджмиддинов Сирожиддин");
            classLabel.setText("📚 Класс: 9A");
            yearLabel.setText("📅 Год: 2025-2026");
            descLabel.setText("Образовательная игровая платформа для учеников школы Vosiq International School");
        } else if("en".equals(lang)) {
            creatorLabel.setText("👨‍💻 Developer: Najmiddinov Sirojiddin");
            classLabel.setText("📚 Class: 9A");
            yearLabel.setText("📅 Year: 2025-2026");
            descLabel.setText("Educational gaming platform for Vosiq International School students");
        } else {
            creatorLabel.setText("👨‍💻 Yaratuvchi: Najmiddinov Sirojiddin");
            classLabel.setText("📚 Sinf: 9A");
            yearLabel.setText("📅 Yil: 2025-2026");
            descLabel.setText("Vosiq International School o'quvchilari uchun ta'limiy o'yinlar platformasi");
        }
    }

    @FXML private void goBack() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainMenu.fxml")));
            MainApp.primaryStage.getScene().setRoot(root);
        } catch(Exception e){ e.printStackTrace(); }
    }
}
