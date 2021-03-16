package fr.doranco.quizz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDataBaseSQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quiz_users";
    private static final Integer DATA_VERSION = 1;
    private static final String TABLE__USERS_NAME = "users";
    private static final String TABLE__QUESTIONS_NAME = "questions";

    private static final String CREATE_DB_TABLE_USER = "CREATE TABLE " + TABLE__USERS_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, firstname TEXT NOT NULL UNIQUE, score INT)";

    private static final String CREATE_DB_TABLE_QUESTION = "CREATE TABLE " + TABLE__QUESTIONS_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "question TEXT NOT NULL, " +
            "reponse1 TEXT NOT NULL, " +
            "reponse2 TEXT NOT NULL," +
            "reponse3 TEXT NOT NULL," +
            "reponse4 TEXT NOT NULL)";


    protected MyDataBaseSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);   // Si la version executer par la classe mère est différentes de la version déja crée, il appelle directement la méthode onUpgrade() !!
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE_USER);
        db.execSQL(CREATE_DB_TABLE_QUESTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Normalement , on fait un ALTER sur la  table pour ne pas perdre les données !
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE__USERS_NAME );
        db.execSQL(CREATE_DB_TABLE_USER);

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE__QUESTIONS_NAME );
        db.execSQL(CREATE_DB_TABLE_QUESTION);
    }
}
