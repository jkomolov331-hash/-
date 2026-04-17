package com.vosiq.edugames.game;

import com.vosiq.edugames.MainApp;
import com.vosiq.edugames.model.Question;
import com.vosiq.edugames.util.LanguageManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.*;

public class CustomQuizGame extends BaseGame {

    private List<Question> customQuestions = new ArrayList<>();
    private VBox container;

    public void show() {
        initRoot();
        showAddQuestionsScreen();
        MainApp.primaryStage.getScene().setRoot(root);
    }

    private void showAddQuestionsScreen() {
        root.setTop(buildTopBar("✏️ " + LanguageManager.get("game.custom"), "#96fbc4"));

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 40, 20, 40));
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label desc = new Label("Savollaringizni kiriting va \"O'ynash\" tugmasini bosing.");
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #a0aec0;");

        // Input form
        VBox form = new VBox(12);
        form.setMaxWidth(650);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 16; " +
                "-fx-border-color: #96fbc444; -fx-border-radius: 16; -fx-border-width: 1.5;");

        Label formTitle = new Label("➕ " + LanguageManager.get("custom.addquestion"));
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #96fbc4;");

        TextField questionField = new TextField();
        questionField.setPromptText(LanguageManager.get("custom.question") + " (masalan: O'zbekiston poytaxti?)");
        questionField.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 8; -fx-padding: 10; -fx-prompt-text-fill: #4a5568;");

        TextField answerField = new TextField();
        answerField.setPromptText(LanguageManager.get("custom.answer") + " (masalan: Toshkent)");
        answerField.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 8; -fx-padding: 10; -fx-prompt-text-fill: #4a5568;");

        TextField wrongField = new TextField();
        wrongField.setPromptText(LanguageManager.get("custom.wronganswers") + " (masalan: Samarqand,Buxoro,Namangan)");
        wrongField.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 8; -fx-padding: 10; -fx-prompt-text-fill: #4a5568;");

        Label questionLbl = new Label(LanguageManager.get("custom.question"));
        questionLbl.setStyle("-fx-text-fill: #a0aec0; -fx-font-size: 12px;");
        Label answerLbl = new Label(LanguageManager.get("custom.answer"));
        answerLbl.setStyle("-fx-text-fill: #a0aec0; -fx-font-size: 12px;");
        Label wrongLbl = new Label(LanguageManager.get("custom.wronganswers"));
        wrongLbl.setStyle("-fx-text-fill: #a0aec0; -fx-font-size: 12px;");

        Button addBtn = new Button("➕ " + LanguageManager.get("custom.addquestion"));
        addBtn.setStyle("-fx-background-color: #96fbc4; -fx-text-fill: #1a1a2e; -fx-font-size: 13px; " +
                "-fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 10; -fx-cursor: hand;");

        Label status = new Label("");
        status.setStyle("-fx-font-size: 13px; -fx-text-fill: #43e97b;");

        form.getChildren().addAll(formTitle, questionLbl, questionField, answerLbl, answerField,
                wrongLbl, wrongField, addBtn, status);

        // Questions list
        VBox qListBox = new VBox(8);
        qListBox.setMaxWidth(650);

        Label listTitle = new Label("📋 Savollar ro'yxati:");
        listTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #667eea;");
        qListBox.getChildren().add(listTitle);

        // Refresh list
        Runnable refreshList = () -> {
            qListBox.getChildren().clear();
            qListBox.getChildren().add(listTitle);
            for (int i = 0; i < customQuestions.size(); i++) {
                Question cq = customQuestions.get(i);
                final int idx = i;
                HBox qRow = new HBox(10);
                qRow.setAlignment(Pos.CENTER_LEFT);
                qRow.setPadding(new Insets(8, 12, 8, 12));
                qRow.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 10;");

                Label numL = new Label((i + 1) + ".");
                numL.setStyle("-fx-text-fill: #667eea; -fx-font-weight: bold;");

                Label qText = new Label(cq.getQuestion() + " → " + cq.getCorrectAnswer());
                qText.setStyle("-fx-text-fill: #a0aec0; -fx-font-size: 13px;");
                qText.setWrapText(true);
                qText.setMaxWidth(480);
                HBox.setHgrow(qText, Priority.ALWAYS);

                Button delBtn = new Button("✕");
                delBtn.setStyle("-fx-background-color: #f56565; -fx-text-fill: white; -fx-font-size: 11px; " +
                        "-fx-padding: 3 8; -fx-background-radius: 6; -fx-cursor: hand;");
                delBtn.setOnAction(e -> {
                    customQuestions.remove(idx);
                    // will be refreshed by the add action
                });

                qRow.getChildren().addAll(numL, qText, delBtn);
                qListBox.getChildren().add(qRow);
            }
        };

        addBtn.setOnAction(e -> {
            String q = questionField.getText().trim();
            String ans = answerField.getText().trim();
            String wrongs = wrongField.getText().trim();

            if (q.isEmpty() || ans.isEmpty()) {
                status.setText("⚠️ Savol va javob kiritilishi shart!");
                status.setStyle("-fx-font-size: 13px; -fx-text-fill: #f56565;");
                return;
            }

            List<String> wrongList = new ArrayList<>();
            if (!wrongs.isEmpty()) {
                for (String w : wrongs.split(",")) {
                    if (!w.trim().isEmpty()) wrongList.add(w.trim());
                }
            }
            // Fill with defaults if not enough
            while (wrongList.size() < 3) {
                wrongList.add("Noto'g'ri " + (wrongList.size() + 1));
            }

            customQuestions.add(new Question(q, ans, wrongList.subList(0, 3), "custom"));
            questionField.clear();
            answerField.clear();
            wrongField.clear();
            status.setText("✅ Savol qo'shildi! Jami: " + customQuestions.size());
            status.setStyle("-fx-font-size: 13px; -fx-text-fill: #43e97b;");
            refreshList.run();
        });

        // Play button
        Button playBtn = new Button("▶ " + LanguageManager.get("custom.play") + " (" + customQuestions.size() + " savol)");
        playBtn.setPrefWidth(300);
        playBtn.setPrefHeight(48);
        playBtn.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                "-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; " +
                "-fx-background-radius: 12; -fx-cursor: hand;");
        playBtn.setOnAction(e -> {
            if (customQuestions.isEmpty()) {
                status.setText("⚠️ Hech bo'lmaganda 1 ta savol kiriting!");
                status.setStyle("-fx-font-size: 13px; -fx-text-fill: #f56565;");
                return;
            }
            startCustomGame();
        });

        Button clearBtn = new Button("🗑️ Hammasini tozalash");
        clearBtn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: #f56565; -fx-font-size: 13px; " +
                "-fx-padding: 8 15; -fx-background-radius: 10; -fx-cursor: hand;");
        clearBtn.setOnAction(e -> {
            customQuestions.clear();
            refreshList.run();
            status.setText("Tozalandi.");
            status.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a5568;");
            playBtn.setText("▶ " + LanguageManager.get("custom.play") + " (0 savol)");
        });

        HBox actionBtns = new HBox(15, playBtn, clearBtn);
        actionBtns.setAlignment(Pos.CENTER);

        mainContent.getChildren().addAll(desc, form, qListBox, actionBtns);

        ScrollPane sp = new ScrollPane(mainContent);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        root.setCenter(sp);
        root.setBottom(buildBackBottomBar());

        refreshList.run();
    }

    private void startCustomGame() {
        score = 0;
        questionIndex = 0;
        List<Question> shuffled = new ArrayList<>(customQuestions);
        Collections.shuffle(shuffled);

        container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20, 40, 20, 40));

        root.setCenter(container);
        showCustomQuestion(shuffled);
    }

    private void showCustomQuestion(List<Question> qs) {
        container.getChildren().clear();

        if (questionIndex >= qs.size()) {
            stopTimer();
            root.setCenter(buildResultScreen(qs.size()));
            return;
        }

        Question q = qs.get(questionIndex);

        Label progress = new Label("Savol " + (questionIndex + 1) + " / " + qs.size());
        progress.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a5568;");

        VBox qCard = new VBox(12);
        qCard.setAlignment(Pos.CENTER);
        qCard.setPadding(new Insets(25));
        qCard.setMaxWidth(650);
        qCard.setStyle("-fx-background-color: #1e2a3a; -fx-background-radius: 18; " +
                "-fx-border-color: #96fbc444; -fx-border-radius: 18; -fx-border-width: 1.5;");

        Label qLabel = new Label(q.getQuestion());
        qLabel.setStyle("-fx-font-size: 19px; -fx-font-weight: bold; -fx-text-fill: white; -fx-wrap-text: true;");
        qLabel.setMaxWidth(600);
        qLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        qCard.getChildren().add(qLabel);

        List<String> answers = new ArrayList<>(q.getWrongAnswers());
        answers.add(q.getCorrectAnswer());
        Collections.shuffle(answers);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);

        String[] colors = {"#667eea", "#f093fb", "#43e97b", "#f5576c"};
        int i = 0;
        for (String ans : answers) {
            final String answer = ans;
            final String c = colors[i % colors.length];
            Button btn = new Button(answer);
            btn.setPrefWidth(280);
            btn.setPrefHeight(48);
            btn.setWrapText(true);
            btn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 14px; " +
                    "-fx-background-radius: 12; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: " + c + "44; -fx-text-fill: white; -fx-font-size: 14px; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; -fx-border-color: " + c + "; " +
                    "-fx-border-radius: 12; -fx-border-width: 2;"));
            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: #2d3748; -fx-text-fill: white; -fx-font-size: 14px; " +
                    "-fx-background-radius: 12; -fx-cursor: hand;"));
            btn.setOnAction(e -> {
                stopTimer();
                grid.setDisable(true);
                boolean correct = answer.equals(q.getCorrectAnswer());
                if (correct) updateScore(10);
                showFeedback(container, correct, () -> {
                    questionIndex++;
                    showCustomQuestion(qs);
                });
            });
            grid.add(btn, i % 2, i / 2);
            i++;
        }

        container.getChildren().addAll(progress, qCard, grid);

        qCard.setOpacity(0);
        new FadeTransition(Duration.millis(300), qCard) {{ setToValue(1); play(); }};

        startTimer(() -> {
            questionIndex++;
            showCustomQuestion(qs);
        });
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
