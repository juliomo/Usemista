package com.usm.jyd.usemista.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by der_w on 10/8/2015.
 */
public class DBPensum {
    private PensumHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DBPensum(Context context) {
        mHelper = new PensumHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertMateriaPensum(ArrayList<Materia> listMaterias, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        //create a sql prepared statement
        String sql = "INSERT INTO " + PensumHelper.TABLE_MATERIA + " VALUES (?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listMaterias.size(); i++) {
            Materia currentMateria = listMaterias.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentMateria.getTitulo());
            statement.bindLong(3, currentMateria.getSemestre()); ///Cuidado ESTA PUDE GENERAR ERROR YA Q NO SE A PROBADO
            statement.bindString(4, currentMateria.getObjetivo());
            statement.bindString(5, currentMateria.getContenido());
            statement.bindString(6, currentMateria.getModulo());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listMaterias.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Materia> getAllMateriaPensum() {
        ArrayList<Materia> listMovies = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {PensumHelper.COLUMN_UID,
                PensumHelper.COLUMN_TITULO,
                PensumHelper.COLUMN_SEMESTRE,
                PensumHelper.COLUMN_CONTENIDO,
                PensumHelper.COLUMN_OBJETIVO,
                PensumHelper.COLUMN_MODULO
        };
        Cursor cursor = mDatabase.query(PensumHelper.TABLE_MATERIA, columns, null, null, null, null, null);//parametros no estudiados
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new materia object and retrieve the data from the cursor to be stored in this materia object
                Materia materia = new Materia();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank materia object to contain our data
                materia.setTitulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_TITULO)));
                materia.setSemestre(cursor.getInt(cursor.getColumnIndex(PensumHelper.COLUMN_SEMESTRE)));
                materia.setObjetivo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_CONTENIDO)));
                materia.setContenido(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_OBJETIVO)));
                materia.setModulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_MODULO)));
                //add the movie to the list of movie objects which we plan to return
                listMovies.add(materia);
            }
            while (cursor.moveToNext());
        }
        return listMovies;
    }

    public void deleteAll() {
        mDatabase.delete(PensumHelper.TABLE_MATERIA, null, null);
    }

    private static class PensumHelper extends SQLiteOpenHelper {

        public static final String TABLE_MATERIA = "materia";
        public static final String COLUMN_UID = "ma_id";
        public static final String COLUMN_TITULO = "ma_title";
        public static final String COLUMN_SEMESTRE = "ma_semestre";
        public static final String COLUMN_OBJETIVO = "ma_objetivo";
        public static final String COLUMN_CONTENIDO = "ma_contenido";
        public static final String COLUMN_MODULO = "ma_modulo";

        private static final String CREATE_TABLE_MATERIA = "CREATE TABLE " + TABLE_MATERIA + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITULO + " TEXT," +
                COLUMN_SEMESTRE + " INTEGER," +
                COLUMN_OBJETIVO + " TEXT," +
                COLUMN_CONTENIDO + " TEXT," +
                COLUMN_MODULO + " TEXT," +
                ");";

        private static final String DB_NAME = "pensum";
        private static final int DB_VERSION = 1;
        private Context mContext;

        public PensumHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_MATERIA);
                L.m("create table box office executed");
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                L.m("upgrade table box office executed");
                db.execSQL(" DROP TABLE " + TABLE_MATERIA + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }
    }
}