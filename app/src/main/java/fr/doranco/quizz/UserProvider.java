package fr.doranco.quizz;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

import fr.doranco.quizz.MyDataBaseSQLite;

public class UserProvider extends ContentProvider {

    private SQLiteDatabase db;
    private static final String TABLE_USERS_NAME = "users";
    //private static final String TABLE_QUESTIONS_NAME = "questions";


    // Le provider name c'est le chemin complet vers la Class du Provider (ici UserProvider)
    private  static final String PROVIDER_NAME = "fr.doranco.quizz.UserProvider";
    protected static final Uri CONTENT_USERS_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_USERS_NAME);
    //protected static final Uri CONTENT_QUESTIONS_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_QUESTIONS_NAME);


    private static final UriMatcher uriMatcher;
    private static final int uriCode = 1;

    private static HashMap<String, String> values;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // " content://fr.doranco.quizz.UserProvider/Users "
        uriMatcher.addURI(PROVIDER_NAME, "users", uriCode);
        //uriMatcher.addURI(PROVIDER_NAME, "questions", uriCode);

        // "c ontent://fr.doranco.quizz.UserProvider/Users/* "
        uriMatcher.addURI(PROVIDER_NAME, "users/*", uriCode);
        //uriMatcher.addURI(PROVIDER_NAME, "questions/*", uriCode);
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        MyDataBaseSQLite dbHelper = new MyDataBaseSQLite(context);
        db = dbHelper.getWritableDatabase();
        if (db != null)  {return true;}

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qbUsers = new SQLiteQueryBuilder();
        qbUsers.setTables(TABLE_USERS_NAME);  // On donne le nom de la Table pour pourvoir récupérer les informations

        // Vérifier que l'uri rensigné en parametre est autorisé ou pas
        int code = uriMatcher.match(uri);

        switch (code) {
            case uriCode :
                qbUsers.setProjectionMap(values);
                break;
            default :
                throw new IllegalArgumentException("URI inconnue : " + uri);
        }
        if (sortOrder == null || sortOrder.isEmpty() ) {
            sortOrder = "id";
        }
        Cursor cursos = qbUsers.query(db, columns, selection, selectionArgs, null, null, sortOrder);
        cursos.setNotificationUri(getContext().getContentResolver(), uri);

        return cursos;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db.insert(TABLE_USERS_NAME, "", values);
        if (rowId > 0 ) {
            Uri rowUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        throw new SQLiteException("Erreur lors de l'insertion d'une colonne dans la BDD  ! " + uri );
    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        return 0;
    }
}
