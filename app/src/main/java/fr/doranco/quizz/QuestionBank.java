package fr.doranco.quizz;

import java.util.Collections;
import java.util.List;

import fr.doranco.quizz.Question;

public class QuestionBank {

    private List<Question> mQuestionList;
    private int mNextQuestionIndex;


    public QuestionBank(List<Question> mQuestionList) {
        this.mQuestionList = mQuestionList;

        // Mélanger la liste des questions (==> Shuffle)
        Collections.shuffle(mQuestionList);
        mNextQuestionIndex = 0;
    }

    public Question getQuestion() {

        // On s'assure d'abord qu'on boucle sur la liste des questions au cas ou on atteint la fin de la liste
        if (mNextQuestionIndex == mQuestionList.size()) {
            mNextQuestionIndex = 0;
        }
        return mQuestionList.get(mNextQuestionIndex++);
    }
}
