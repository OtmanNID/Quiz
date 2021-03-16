package fr.doranco.quizz;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
public class PageQuizz extends AppCompatActivity implements IQuizConstants, View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    TextView mTextViewQuestion;
    Button mButtonReponse1, mButtonReponse2, mButtonReponse3, mButtonReponse4;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;
    private int mScore;
    private int mMaxNumberOfQuestions;
    private int mNumberOfQuestions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_quizz);
        String firstname = "";
        firstname = getIntent().getStringExtra("firstname");

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(STATE_SCORE_BEFORE_ROTATION);
            mNumberOfQuestions = savedInstanceState.getInt(STATE_QUESTION_BEFORE_ROTATION);
        } else {
            mScore = 0;
            mNumberOfQuestions = mMaxNumberOfQuestions = 4;
        }


        /*------------Récupérer les éléments XML sous forme JAVA---------------*/
        mTextViewQuestion = findViewById(R.id.textViewQuestion);
        mButtonReponse1 = findViewById(R.id.buttonReponse1);
        mButtonReponse2 = findViewById(R.id.buttonReponse2);
        mButtonReponse3 = findViewById(R.id.buttonReponse3);
        mButtonReponse4 = findViewById(R.id.buttonReponse4);

        mQuestionBank = generateQuestions();




        // Utiliser la propriété TAG pour nommer les boutons
        // Pour savoir quel bouton a été appuyé
        mButtonReponse1.setTag(0);
        mButtonReponse2.setTag(1);
        mButtonReponse3.setTag(2);
        mButtonReponse4.setTag(3);

        // Pour cela il faudra obligatoirement que cette classe implemente
        // ==> l'Interface View.OnClickListener
        // Dans ce cas, c'est la méthode onClick() lorsqu'on clique sur n'importe
        // quel bouton parmi les 4
        mButtonReponse1.setOnClickListener(this);
        mButtonReponse2.setOnClickListener(this);
        mButtonReponse3.setOnClickListener(this);
        mButtonReponse4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayCurrentQuestion(mCurrentQuestion);

    }
    // Ceci équivaut à un seul listener dans toute la page (donc tous les boutons de la page)
    // Pour se faire, il faudra que notre classe implemente l'interface View.OnClickListener
    @Override
    public void onClick(View view) {
        int responseIndex = (int) view.getTag();
        if (responseIndex == mCurrentQuestion.getIndexReponse()) {
            mScore++;
            Toast.makeText(PageQuizz.this, "Bonne réponse ! Bien joué :)", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PageQuizz.this, "Mauvaise réponse ! Désolé :(", Toast.LENGTH_SHORT).show();
        }

        // On souhaite laisser le temps à l'utilisateur de voir la notifications de bonne ou mauvaise réponse
        // Avant de passer à la question suivante
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Si c'est la dernière question, alors arrêter le quizz
                // Sinon afficher la prochaine question
                if(--mNumberOfQuestions == 0){
                    endQuiz();
                } else
                {
                    mCurrentQuestion=mQuestionBank.getQuestion();
                    displayCurrentQuestion(mCurrentQuestion);
                }
            }
        }, 2000);





    }

    private void displayCurrentQuestion(Question question) {
        mTextViewQuestion.setText(question.getQuestion());
        mButtonReponse1.setText(question.getChoiceList().get(0));
        mButtonReponse2.setText(question.getChoiceList().get(1));
        mButtonReponse3.setText(question.getChoiceList().get(2));
        mButtonReponse4.setText(question.getChoiceList().get(3));
    }

    private void  endQuiz() {

        // On arrête la musique
        Intent intent = new Intent(PageQuizz.this, MusicQuizz.class);
        stopService(intent);

        // On récupère la valeur du firstname pour pusher dans le BDD en même temps le nom et le score
        String firstname = "";
        firstname = getIntent().getStringExtra("firstname");


        // On affiche une boite de dialogue pour indiquer le score final et la fin du quiz
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quizz terminé !");
        builder.setMessage("Votre score est de : " + mScore + " / " + mMaxNumberOfQuestions).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Quand on appuie sur le bouton Ok, il appelle la page principal pour lui renvoyer le score !
                Intent  intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        })
                .create()
                .show();
        try {
            ContentValues values = new ContentValues();
            values.put("firstname", firstname);
            values.put("score", mScore);
            Uri uriAjout = getContentResolver().insert(UserProvider.CONTENT_USERS_URI, values);
            Toast.makeText(getBaseContext(), "Les valeurs ont bien été ajoutée à la BDD", Toast.LENGTH_SHORT).show();

        } catch (SQLiteException e) {
            Toast.makeText(getBaseContext(), "Erreur lors de l'ajout du score de l'utilisateur", Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
        }

    }

    // Pour régler le problème de destruction de l'activité courante lors de la rotation du téléphone
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_SCORE_BEFORE_ROTATION, mScore);
        outState.putInt(STATE_QUESTION_BEFORE_ROTATION, mNumberOfQuestions);
        super.onSaveInstanceState(outState);
    }

    private QuestionBank generateQuestions() {
        Question question1 = new Question("Quel est le pays le plus peuplé du monde ?",
                Arrays.asList("USA", "Chine", "Inde", "Indonésie"), 1);
        Question question2 = new Question("Quelle pièce est absolument à protéger dans un jeu d’échec ?",
                Arrays.asList("Roi", "Dame", "Tour", "Cavalier"), 0);
        Question question3 = new Question("Quel est l'impératif du verbe peindre à la 2e personne du pluriel ?",
                Arrays.asList("Peigniez", "Peints", "Peignez" , "Peigner"), 2);
        Question question4 = new Question("Avec la laine de quel animal fait-on du cachemire ?",
                Arrays.asList("Mouton", "Chèvre", "Lapin", "Vison"), 1);
        Question question5 = new Question("Quel ville est surnommé « big Apple » aux USA ?",
                Arrays.asList("New-York", "Washington", "Denver", "Los Angeles"), 0);
        Question question6 = new Question("Qui a écrit les misérables ?",
                Arrays.asList("Otman" , "Baudelaire", "Guy DE MAUPASSANT", "Victor HUGO"), 3);

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5, question6));
    }


}