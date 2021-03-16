package fr.doranco.quizz;

import java.util.List;

public class Question {

    private String mQuestion;
    private List<String> mChoiceList;
    private int mIndexReponse;

    public Question(String mQuestion, List<String> mChoiceList, int mIndexReponse) {
        this.mQuestion = mQuestion;
        this.mChoiceList = mChoiceList;
        this.mIndexReponse = mIndexReponse;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public List<String> getChoiceList() {
        return mChoiceList;
    }

    public int getIndexReponse() {
        return mIndexReponse;
    }
}
