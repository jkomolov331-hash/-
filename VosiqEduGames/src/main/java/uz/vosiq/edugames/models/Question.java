package uz.vosiq.edugames.models;
public class Question {
    private final String questionText;
    private final String[] options;
    private final int correctIndex;

    public Question(String questionText, String[] options, int correctIndex){
        this.questionText=questionText; this.options=options; this.correctIndex=correctIndex;
    }
    public String getQuestionText(){ return questionText; }
    public String[] getOptions(){ return options; }
    public int getCorrectIndex(){ return correctIndex; }
    public String getCorrectAnswer(){ return correctIndex < options.length ? options[correctIndex] : ""; }
    public boolean isCorrect(int idx){ return idx==correctIndex; }
}
