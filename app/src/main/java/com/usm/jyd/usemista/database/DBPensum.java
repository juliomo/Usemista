package com.usm.jyd.usemista.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.usm.jyd.usemista.aplicativo.MiAplicativo;
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

    public void insertUserMateria(Materia materia){
        String sql= "INSERT INTO "+PensumHelper.TABLE_USER_MATERIA+" VALUES(?,?,?,?,?,?,?);";
        SQLiteStatement statement= mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
            statement.clearBindings();
            statement.bindString(2, materia.getCod());
            statement.bindString(3, materia.getTitulo());
            statement.bindString(4, materia.getSemestre());
            statement.bindString(5, materia.getObjetivo());
            statement.bindString(6, materia.getContenido());
            statement.bindString(7, materia.getModulo());
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();

    }
    public void deleteUserMateria(String u_ma_cod) {
        String[] selectionArgs = { String.valueOf(u_ma_cod) };
        mDatabase.delete(PensumHelper.TABLE_USER_MATERIA, PensumHelper.COLUMN_U_COD, selectionArgs);
    }
    public  ArrayList<Materia> getAllUserMateria(){
        ArrayList<Materia> listUserMateria = new ArrayList<>();

        String[] columns = {PensumHelper.COLUMN_U_UID,
                PensumHelper.COLUMN_U_COD,
                PensumHelper.COLUMN_U_TITULO,
                PensumHelper.COLUMN_U_SEMESTRE,
                PensumHelper.COLUMN_U_CONTENIDO,
                PensumHelper.COLUMN_U_OBJETIVO,
                PensumHelper.COLUMN_U_MODULO
        };
        Cursor cursor = mDatabase.query(PensumHelper.TABLE_USER_MATERIA, columns, null, null, null, null, null);//parametros no estudiados
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new materia object and retrieve the data from the cursor to be stored in this materia object
                Materia materia = new Materia();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank materia object to contain our data
                materia.setCod(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_COD)));
                materia.setTitulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_TITULO)));
                materia.setSemestre(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_SEMESTRE)));
                materia.setObjetivo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_CONTENIDO)));
                materia.setContenido(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_OBJETIVO)));
                materia.setModulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_MODULO)));
                //add the materia to the list of materia objects which we plan to return
                listUserMateria.add(materia);
            }
            while (cursor.moveToNext());
        }
        return listUserMateria;
    }

    public void insertMateriaPensumIndividual(ArrayList<Materia> listMaterias,String ma_modulo, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAllTopic(ma_modulo);
        }
        //create a sql prepared statement
        String sql = "INSERT INTO " + PensumHelper.TABLE_MATERIA + " VALUES (?,?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listMaterias.size(); i++) {
            Materia currentMateria = listMaterias.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentMateria.getCod());
            statement.bindString(3, currentMateria.getTitulo());
            statement.bindString(4, currentMateria.getSemestre()); ///Cuidado ESTA PUDE GENERAR ERROR YA Q NO SE A PROBADO
            statement.bindString(5, currentMateria.getObjetivo());
            statement.bindString(6, currentMateria.getContenido());
            statement.bindString(7, currentMateria.getModulo());
            statement.bindString(8, currentMateria.getU_materia());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listMaterias.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    public ArrayList<Materia> getAllMateriaPensumIndividual(String ma_modulo) {

        String selection = PensumHelper.COLUMN_MODULO + " LIKE ?";
        String[] selectionArgs = { String.valueOf(ma_modulo) };

        ArrayList<Materia> listMateria = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {PensumHelper.COLUMN_UID,
                PensumHelper.COLUMN_COD,
                PensumHelper.COLUMN_TITULO,
                PensumHelper.COLUMN_SEMESTRE,
                PensumHelper.COLUMN_OBJETIVO,
                PensumHelper.COLUMN_CONTENIDO,
                PensumHelper.COLUMN_MODULO,
                PensumHelper.COLUMN_U_MATERIA
        };
        Cursor cursor = mDatabase.query(PensumHelper.TABLE_MATERIA, columns, selection, selectionArgs, null, null, null);//parametros no estudiados
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new materia object and retrieve the data from the cursor to be stored in this materia object
                Materia materia = new Materia();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank materia object to contain our data
                materia.setCod(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_COD)));
                materia.setTitulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_TITULO)));
                materia.setSemestre(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_SEMESTRE)));
                materia.setObjetivo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_OBJETIVO)));
                materia.setContenido(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_CONTENIDO)));
                materia.setModulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_MODULO)));
                materia.setU_materia(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_MATERIA)));
                //add the materia to the list of materia objects which we plan to return
                listMateria.add(materia);
            }
            while (cursor.moveToNext());
        }
        return listMateria;
    }



    public void insertMateriaPensum(ArrayList<Materia> listMaterias, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        //create a sql prepared statement
        String sql = "INSERT INTO " + PensumHelper.TABLE_MATERIA + " VALUES (?,?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listMaterias.size(); i++) {
            Materia currentMateria = listMaterias.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentMateria.getCod());
            statement.bindString(3, currentMateria.getTitulo());
            statement.bindString(4, currentMateria.getSemestre()); ///Cuidado ESTA PUDE GENERAR ERROR YA Q NO SE A PROBADO
            statement.bindString(5, currentMateria.getObjetivo());
            statement.bindString(6, currentMateria.getContenido());
            statement.bindString(7, currentMateria.getModulo());
            statement.bindString(8, currentMateria.getU_materia());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listMaterias.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Materia> getAllMateriaPensum() {
        ArrayList<Materia> listMateria = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {PensumHelper.COLUMN_UID,
                PensumHelper.COLUMN_COD,
                PensumHelper.COLUMN_TITULO,
                PensumHelper.COLUMN_SEMESTRE,
                PensumHelper.COLUMN_OBJETIVO,
                PensumHelper.COLUMN_CONTENIDO,
                PensumHelper.COLUMN_MODULO,
                PensumHelper.COLUMN_U_MATERIA
        };
        Cursor cursor = mDatabase.query(PensumHelper.TABLE_MATERIA, columns, null, null, null, null, null);//parametros no estudiados
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new materia object and retrieve the data from the cursor to be stored in this materia object
                Materia materia = new Materia();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank materia object to contain our data
                materia.setCod(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_COD)));
                materia.setTitulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_TITULO)));
                materia.setSemestre(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_SEMESTRE)));
                materia.setObjetivo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_OBJETIVO)));
                materia.setContenido(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_CONTENIDO)));
                materia.setModulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_MODULO)));
                materia.setU_materia(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_MATERIA)));
                //add the materia to the list of materia objects which we plan to return
                listMateria.add(materia);
            }
            while (cursor.moveToNext());
        }
        return listMateria;
    }

    public void deleteAll() {
        mDatabase.delete(PensumHelper.TABLE_MATERIA, null, null);
    }
    public void deleteAllTopic(String ma_modulo){

        String selection = PensumHelper.COLUMN_MODULO + " LIKE ?";
        String[] selectionArgs = { String.valueOf(ma_modulo) };
        mDatabase.delete(PensumHelper.TABLE_MATERIA,selection,selectionArgs);
    }
    public  void updateMateriaUserPic(String newValor,String ma_cod ){

        ContentValues values = new ContentValues();
        values.put(PensumHelper.COLUMN_U_MATERIA, newValor);

        // Which row to update, based on the ID
        String selection = PensumHelper.COLUMN_COD + " LIKE ?";
        String[] selectionArgs = { String.valueOf(ma_cod) };

        mDatabase.update(PensumHelper.TABLE_MATERIA,values,selection,selectionArgs);
    }

    private static class PensumHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "pensum";
        private static final int DB_VERSION = 1;

        public static final String TABLE_MATERIA = "materia";
        public static final String COLUMN_UID = "ma_id";
        public static final String COLUMN_COD = "ma_cod";
        public static final String COLUMN_TITULO = "ma_title";
        public static final String COLUMN_SEMESTRE = "ma_semestre";
        public static final String COLUMN_OBJETIVO = "ma_objetivo";
        public static final String COLUMN_CONTENIDO = "ma_contenido";
        public static final String COLUMN_MODULO = "ma_modulo";
        public static final String COLUMN_U_MATERIA = "ma_u_materia";

        private static final String CREATE_TABLE_MATERIA = "CREATE TABLE " + TABLE_MATERIA + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_COD + " TEXT, " +
                COLUMN_TITULO + " TEXT," +
                COLUMN_SEMESTRE + " TEXT," +
                COLUMN_OBJETIVO + " TEXT," +
                COLUMN_CONTENIDO + " TEXT," +
                COLUMN_MODULO + " TEXT," +
                COLUMN_U_MATERIA+ " TEXT DEFAULT 0"+
                ");";

        public static final String TABLE_USER_MATERIA = "user_materia";
        public static final String COLUMN_U_UID = "u_ma_id";
        public static final String COLUMN_U_COD = "u_ma_cod";
        public static final String COLUMN_U_TITULO = "u_ma_title";
        public static final String COLUMN_U_SEMESTRE = "u_ma_semestre";
        public static final String COLUMN_U_OBJETIVO = "u_ma_objetivo";
        public static final String COLUMN_U_CONTENIDO = "u_ma_contenido";
        public static final String COLUMN_U_MODULO = "u_ma_modulo";

        private static final String CREATE_TABLE_USER_MATERIA = "CREATE TABLE " + TABLE_USER_MATERIA + " (" +
                COLUMN_U_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_U_COD + " TEXT, " +
                COLUMN_U_TITULO + " TEXT," +
                COLUMN_U_SEMESTRE + " TEXT," +
                COLUMN_U_OBJETIVO + " TEXT," +
                COLUMN_U_CONTENIDO + " TEXT," +
                COLUMN_U_MODULO + " TEXT" +
                ");";

        private Context mContext;
        public PensumHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_MATERIA);
                db.execSQL(CREATE_TABLE_USER_MATERIA);
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