package fr.doranco.quizz;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements IQuizConstants {

    private static final String TAG = MainActivity.class.getSimpleName();



    private TextView mTextView;
    private EditText mEditTextPrenom;
    private Button mButtonCommencerQuizz;
    private User mUser;
    private TextView listUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*------------Récupérer les éléments XML sous forme JAVA---------------*/
        mTextView = findViewById(R.id.textView);
        mEditTextPrenom = findViewById(R.id.editTextPrenom);
        mButtonCommencerQuizz = findViewById(R.id.buttonCommencerQuizz);
        listUsers = findViewById(R.id.listUsers);


        /*------On DESACTIVE le boutton si aucun nom n'est entré dans le champ editText------*/
        mButtonCommencerQuizz.setEnabled(false);

        mUser = new User();

        /*--------------On met un listener sur le EditText pour voir chaque changement fait par l'utilisateur----------------------*/
        /*--------------Par exemple: il ajoute une lettre, il supprimer une lettre, il selectionne, il clique sur le editText------*/
        mEditTextPrenom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    /*--------------On ACTIVE le boutton quand le texte n'est plus vide --------------*/
                    mButtonCommencerQuizz.setEnabled( !charSequence.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable charSequence) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String firstname = "";
        int score;

        if (firstname.isEmpty()) {
            mTextView.setText("Veuillez entrer votre prénom");
        } else {
            Uri uri = Uri.parse("content://fr.doranco.quizz.UserProvider/users");
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                StringBuilder str = new StringBuilder();
                while (!cursor.isAfterLast()) {
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    firstname = cursor.getString(cursor.getColumnIndex("firstname"));
                    str.append(id);
                    str.append(" - ");
                    str.append(firstname);
                    cursor.moveToNext();
                }
            }
        }
    }

    public void commencerQuizz(View view) {
        String firstname = mEditTextPrenom.getText().toString(); // On récupère le prénom entrer
        // mUser.setmFirstname(firstname);

        // On l'insère dans le BDD
        try {
            // On appelle le service pour lancer la musique
            Intent intent = new Intent(MainActivity.this, MusicQuizz.class);
            startService(intent);

            // On appelle l'activité souhaité
            Intent quizActivityIntent = new Intent(MainActivity.this, PageQuizz.class);
            quizActivityIntent.putExtra("firstname", firstname);
            startActivityForResult(quizActivityIntent, QUIZ_ACTIVITY_REQUEST_CODE);

        } catch (SQLiteException e) {
            Toast.makeText(getBaseContext(), "Erreur lors de l'ajout de l'utilisateur", Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
        }
    }

    public void afficherUtilisateurs(View view) {


        Uri uri = Uri.parse("content://fr.doranco.quizz.UserProvider/users");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            StringBuilder str = new StringBuilder();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
                int score = cursor.getInt(cursor.getColumnIndex("score"));
                str.append(id);
                str.append(" - ");
                str.append(firstname);
                str.append(" - ");
                str.append(score);
                str.append(" - ");
                str.append(System.lineSeparator());
                cursor.moveToNext();
            }
            listUsers.setText((str));
        }
        else {
            listUsers.setText("Pas encore d'Utilisateurs");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QUIZ_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int score = data.getIntExtra(EXTRA_SCORE_PARAM, 0);
        }

    }
}