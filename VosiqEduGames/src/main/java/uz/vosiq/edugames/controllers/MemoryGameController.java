package uz.vosiq.edugames.controllers;
import uz.vosiq.edugames.utils.LanguageManager;
import uz.vosiq.edugames.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import java.util.*;

public class MemoryGameController {
    @FXML private GridPane cardGrid;
    @FXML private Label scoreLabel;
    @FXML private Label timerLabel;
    @FXML private Label pairsLabel;
    @FXML private Button backBtn;
    @FXML private VBox resultPane;
    @FXML private Label resultLabel;

    private MainController mainController;
    private final List<Button> cards = new ArrayList<>();
    private Button firstFlipped = null;
    private int firstIdx = -1;
    private int pairs = 0;
    private int totalPairs = 8;
    private int moves = 0;
    private boolean canClick = true;
    private Timeline gameTimer;
    private int timeElapsed = 0;
    private List<String> cardValues;

    public void setMainController(MainController mc){ this.mainController = mc; }
    public void setSubject(String subject){ startGame(subject); }

    public void startGame(String subject){
        backBtn.setText(LanguageManager.get("btn.back"));
        String[] emojis = {"🐶","🐱","🐭","🐹","🐰","🦊","🐻","🐼","🐨","🐯","🦁","🐮","🐷","🐸","🐵","🦋"};
        cardValues = new ArrayList<>();
        for(int i=0;i<totalPairs;i++){ cardValues.add(emojis[i]); cardValues.add(emojis[i]); }
        Collections.shuffle(cardValues);
        buildGrid();
        startTimer();
        updateLabels();
    }

    private void buildGrid(){
        cardGrid.getChildren().clear();
        cards.clear();
        int cols = 4;
        for(int i=0;i<cardValues.size();i++){
            final int idx = i;
            Button btn = new Button("?");
            btn.setPrefSize(100, 80);
            btn.getStyleClass().add("card-btn");
            btn.setUserData(cardValues.get(i));
            btn.setOnAction(e -> flipCard(btn, idx));
            cards.add(btn);
            cardGrid.add(btn, i%cols, i/cols);
        }
    }

    private void flipCard(Button btn, int idx){
        if(!canClick || btn.getStyleClass().contains("matched") || btn == firstFlipped) return;
        btn.setText((String)btn.getUserData());
        btn.getStyleClass().add("flipped");
        moves++;
        if(firstFlipped == null){ firstFlipped=btn; firstIdx=idx; }
        else {
            canClick = false;
            Button second = btn;
            if(firstFlipped.getUserData().equals(second.getUserData())){
                firstFlipped.getStyleClass().addAll("matched");
                second.getStyleClass().addAll("matched");
                pairs++;
                updateLabels();
                firstFlipped=null; firstIdx=-1; canClick=true;
                if(pairs >= totalPairs) showResult();
            } else {
                Button f = firstFlipped;
                PauseTransition pt = new PauseTransition(Duration.millis(800));
                pt.setOnFinished(e -> {
                    f.setText("?"); f.getStyleClass().remove("flipped");
                    second.setText("?"); second.getStyleClass().remove("flipped");
                    firstFlipped=null; firstIdx=-1; canClick=true;
                });
                pt.play();
            }
        }
    }

    private void updateLabels(){
        pairsLabel.setText("🃏 " + pairs + "/" + totalPairs);
    }

    private void startTimer(){
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeElapsed++;
            int m = timeElapsed/60, s = timeElapsed%60;
            timerLabel.setText(String.format("⏱ %02d:%02d", m, s));
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    private void showResult(){
        if(gameTimer!=null) gameTimer.stop();
        resultPane.setVisible(true);
        int m = timeElapsed/60, s = timeElapsed%60;
        resultLabel.setText("🎉 " + String.format("%02d:%02d", m, s) + " | " + moves + " moves");
    }

    @FXML private void onBack(){ 
        if(gameTimer!=null) gameTimer.stop(); 
        if(mainController!=null){ mainController.goHome(); return; }
        try { Parent root = FXMLLoader.load(getClass().getResource("/fxml/SubjectSelect.fxml")); MainApp.primaryStage.getScene().setRoot(root); } catch(Exception e){ e.printStackTrace(); }
    }
    @FXML private void onRestart(){ 
        resultPane.setVisible(false); pairs=0; moves=0; timeElapsed=0; firstFlipped=null; canClick=true;
        Collections.shuffle(cardValues);
        buildGrid(); startTimer(); updateLabels();
    }
}
