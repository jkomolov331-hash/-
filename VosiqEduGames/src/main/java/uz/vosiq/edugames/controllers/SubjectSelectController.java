package uz.vosiq.edugames.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import uz.vosiq.edugames.MainApp;
import uz.vosiq.edugames.utils.LanguageManager;

public class SubjectSelectController {
    @FXML private Label titleLabel;
    @FXML private Label subjectLabel;
    @FXML private Label gameTypeLabel;
    @FXML private ComboBox<String> subjectCombo;
    @FXML private ComboBox<String> gameTypeCombo;
    @FXML private Button btnStart;
    @FXML private Button btnBack;

    private final String[] subjectKeys = {"math","english","uzbek","russian","biology","chemistry","physics","history","geography","it"};
    private final String[] gameTypes = {"quiz","memory","truefalse"};

    @FXML public void initialize() {
        titleLabel.setText(LanguageManager.get("menu.games"));
        subjectLabel.setText(LanguageManager.get("select.subject"));
        gameTypeLabel.setText(LanguageManager.get("select.game"));
        btnStart.setText(LanguageManager.get("btn.start"));
        btnBack.setText(LanguageManager.get("btn.back"));

        for(String k : subjectKeys) subjectCombo.getItems().add(LanguageManager.get("subject."+k));
        subjectCombo.getItems().set(subjectKeys.length-1, LanguageManager.get("subject.it"));

        gameTypeCombo.getItems().addAll(
            LanguageManager.get("game.quiz"),
            LanguageManager.get("game.memory"),
            LanguageManager.get("game.truefalse")
        );
        subjectCombo.getSelectionModel().selectFirst();
        gameTypeCombo.getSelectionModel().selectFirst();
    }

    @FXML private void startGame() {
        int sIdx = subjectCombo.getSelectionModel().getSelectedIndex();
        int gIdx = gameTypeCombo.getSelectionModel().getSelectedIndex();
        String subject = subjectKeys[Math.min(sIdx, subjectKeys.length-1)];
        String gameType = gameTypes[Math.min(gIdx, gameTypes.length-1)];
        try {
            String fxml = switch(gameType){
                case "memory" -> "/fxml/MemoryGame.fxml";
                case "truefalse" -> "/fxml/TrueFalseGame.fxml";
                default -> "/fxml/QuizGame.fxml";
            };
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            switch(gameType){
                case "quiz" -> ((QuizGameController)loader.getController()).setSubject(subject);
                case "memory" -> ((MemoryGameController)loader.getController()).setSubject(subject);
                case "truefalse" -> ((TrueFalseGameController)loader.getController()).setSubject(subject);
            }
            MainApp.primaryStage.getScene().setRoot(root);
        } catch(Exception e){ e.printStackTrace(); }
    }

    @FXML private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
            MainApp.primaryStage.getScene().setRoot(root);
        } catch(Exception e){ e.printStackTrace(); }
    }
}
