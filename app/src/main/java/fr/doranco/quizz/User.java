package fr.doranco.quizz;

public class User {

    private String mFirstname;
    private int mScoreQuiz;

    public User() {
    }

    public User(String mFirstname) {
        this.mFirstname = mFirstname;
    }

    public String getmFirstname() {
        return mFirstname;
    }

    public void setmFirstname(String mFirstname) {
        this.mFirstname = mFirstname;
    }

    public int getmScoreQuiz() {
        return mScoreQuiz;
    }

    public void setmScoreQuiz(int mScoreQuiz) {
        this.mScoreQuiz = mScoreQuiz;
    }

    @Override
    public String toString() {
        return "User{" +
                "mFirstname='" + mFirstname + '\'' +
                ", mScoreQuiz=" + mScoreQuiz +
                '}';
    }
}
