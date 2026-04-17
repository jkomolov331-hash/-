package uz.vosiq.edugames.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import uz.vosiq.edugames.MainApp;
import uz.vosiq.edugames.utils.LanguageManager;

public class ResultController {

    @FXML private Label titleLabel;
    @FXML private Label resultLabel;
    @FXML private Label messageLabel;
    @FXML private Button btnPlayAgain;
    @FXML private Button btnMenu;

    @FXML
    public void initialize() {
        btnPlayAgain.setText(LanguageManager.get("play_again"));
        btnMenu.setText(LanguageManager.get("back"));
    }

    public void setResult(int score, int total) {
        titleLabel.setText(LanguageManager.get("game_over"));
        resultLabel.setText(LanguageManager.get("your_score") + ": " + score + " / " + total);

        double percent = total > 0 ? (double) score / total : 0;
        if (percent >= 0.8) {
            messageLabel.setText(LanguageManager.get("congrats") + " 🎉");
            messageLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 22px; -fx-font-weight: bold;");
        } else if (percent >= 0.5) {
            messageLabel.setText("👍 " + LanguageManager.get("congrats"));
            messageLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 22px; -fx-font-weight: bold;");
        } else {
            messageLabel.setText(LanguageManager.get("try_harder") + " 💪");
            messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 22px; -fx-font-weight: bold;");
        }
    }

    @FXML private void playAgain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SubjectSelect.fxml"));
            Parent root = loader.load();
            MainApp.primaryStage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void goMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            MainApp.primaryStage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
