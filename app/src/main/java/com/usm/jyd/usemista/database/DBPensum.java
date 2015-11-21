package com.usm.jyd.usemista.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.SyncStateContract;
import android.view.Menu;

import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.objects.HVWeek;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.MenuStatus;

import java.text.DateFormat;
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
    public void insertHorarioVirtual(HorarioVirtual horarioVirtual){

        String sql = "INSERT INTO " + PensumHelper.TABLE_HORARIO_VIRTUAL + " VALUES (?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, horarioVirtual.getCod());
        statement.bindString(3, horarioVirtual.getTitulo());
        statement.bindString(4, horarioVirtual.getCalendar());
        statement.bindLong(5, horarioVirtual.getCalIni() == null
                ? -1 : horarioVirtual.getCalIni().getTime());
        statement.bindLong(6, horarioVirtual.getCalEnd() == null
                ? -1 : horarioVirtual.getCalEnd().getTime());
        statement.bindLong(7, horarioVirtual.getColor());

            statement.execute();

        //set the transaction as successful and end the transaction

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    public  ArrayList<HorarioVirtual> getAllHorarioVirtual(){
        ArrayList<HorarioVirtual> listHorarioVirtual = new ArrayList<>();

        String[] columns = {PensumHelper.COLUMN_HV_UID,
                PensumHelper.COLUMN_HV_COD,
                PensumHelper.COLUMN_HV_TITULO,
                PensumHelper.COLUMN_HV_CALENDAR,
                PensumHelper.COLUMN_HV_CAL_INI,
                PensumHelper.COLUMN_HV_CAL_END,
                PensumHelper.COLUMN_HV_COLOR
        };
        Cursor cursor = mDatabase.query(PensumHelper.TABLE_HORARIO_VIRTUAL, columns, null, null, null, null, null);//parametros no estudiados
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new materia object and retrieve the data from the cursor to be stored in this materia object
                HorarioVirtual horarioVirtual = new HorarioVirtual();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank materia object to contain our data
                horarioVirtual.setCod(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_HV_COD)));
                horarioVirtual.setTitulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_HV_TITULO)));
                horarioVirtual.setCalendar(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_HV_CALENDAR)));

                long calIniDateMilliseconds = cursor.getLong(cursor.getColumnIndex(PensumHelper.COLUMN_HV_CAL_INI));
                horarioVirtual.setCalIni(calIniDateMilliseconds != -1 ? new Date(calIniDateMilliseconds) : null );

                long calEndDateMilliseconds = cursor.getLong(cursor.getColumnIndex(PensumHelper.COLUMN_HV_CAL_END));
                horarioVirtual.setCalEnd(calEndDateMilliseconds != -1 ? new Date(calEndDateMilliseconds) : null);

                horarioVirtual.setColor(cursor.getInt(cursor.getColumnIndex(PensumHelper.COLUMN_HV_COLOR)));
                //add the materia to the list of materia objects which we plan to return
                listHorarioVirtual.add(horarioVirtual);
            }
            while (cursor.moveToNext());
        }
        return listHorarioVirtual;
    }
    public void deleteHorarioVirtual(String u_ma_cod) {
        String selection = PensumHelper.COLUMN_HV_COD + " LIKE ?";
        String[] selectionArgs = { String.valueOf(u_ma_cod) };
        mDatabase.delete(PensumHelper.TABLE_HORARIO_VIRTUAL, selection, selectionArgs);
    }

    public void insertHorarioVirtualWeek(HVWeek hvWeek){

        String sql = "INSERT INTO " + PensumHelper.TABLE_HV_WEEK + " VALUES (?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        statement.clearBindings();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindString(2, hvWeek.getCod());
        statement.bindString(3, hvWeek.getModulo());
        statement.bindString(4, hvWeek.getAula());
        statement.bindString(5, hvWeek.getWeekDay());
        statement.bindLong(6, hvWeek.getTimeIni() == null
                ? -1 : hvWeek.getTimeIni().getTime());
        statement.bindLong(7, hvWeek.getTimeEnd() == null
                ? -1 : hvWeek.getTimeEnd().getTime());
        statement.execute();

        //set the transaction as successful and end the transaction

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    public  ArrayList<HVWeek> getAllHorarioVirtualWeek(){
        ArrayList<HVWeek> listHVWeek = new ArrayList<>();

        String[] columns = {PensumHelper.COLUMN_HV_WEEK_UID,
                PensumHelper.COLUMN_HV_WEEK_COD,
                PensumHelper.COLUMN_HV_WEEK_MOD,
                PensumHelper.COLUMN_HV_WEEK_AULA,
                PensumHelper.COLUMN_HV_WEEK_WEEKDAY,
                PensumHelper.COLUMN_HV_WEEK_START,
                PensumHelper.COLUMN_HV_WEEK_END
        };
        Cursor cursor = mDatabase.query(PensumHelper.TABLE_HV_WEEK, columns, null, null, null, null, null);//parametros no estudiados
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new materia object and retrieve the data from the cursor to be stored in this materia object
                HVWeek hvWeek = new HVWeek();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank materia object to contain our data
                hvWeek.setCod(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_HV_WEEK_COD)));
                hvWeek.setModulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_HV_WEEK_MOD)));
                hvWeek.setAula(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_HV_WEEK_AULA)));
                hvWeek.setWeekDay(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_HV_WEEK_WEEKDAY)));

                long timeIniDateMilliseconds = cursor.getLong(cursor.getColumnIndex(PensumHelper.COLUMN_HV_WEEK_START));
                hvWeek.setTimeIni(timeIniDateMilliseconds != -1 ? new Date(timeIniDateMilliseconds) : null);

                long timeEndDateMilliseconds = cursor.getLong(cursor.getColumnIndex(PensumHelper.COLUMN_HV_WEEK_END));
                hvWeek.setTimeEnd(timeEndDateMilliseconds != -1 ? new Date(timeEndDateMilliseconds) : null);


                //add the materia to the list of materia objects which we plan to return
                listHVWeek.add(hvWeek);
            }
            while (cursor.moveToNext());
        }
        return listHVWeek;
    }
    public void deleteHorarioVirtualWeek(String u_ma_cod) {
        String selection = PensumHelper.COLUMN_HV_WEEK_COD + " LIKE ?";
        String[] selectionArgs = { String.valueOf(u_ma_cod) };
        mDatabase.delete(PensumHelper.TABLE_HV_WEEK, selection, selectionArgs);
    }


    public void insertUserMateria(Materia materia){

        ContentValues values =  new ContentValues();
        values.put(PensumHelper.COLUMN_U_COD,materia.getCod());
        values.put(PensumHelper.COLUMN_U_TITULO,materia.getTitulo());
        values.put(PensumHelper.COLUMN_U_SEMESTRE,materia.getSemestre());
        values.put(PensumHelper.COLUMN_U_OBJETIVO,materia.getObjetivo());
        values.put(PensumHelper.COLUMN_U_CONTENIDO,materia.getContenido());
        values.put(PensumHelper.COLUMN_U_MODULO,materia.getModulo());

        mDatabase.insert(PensumHelper.TABLE_USER_MATERIA,
                null ,values);
    }
    public  void updateUserMateriaHvPic(String newValor,String ma_cod ){

        ContentValues values = new ContentValues();
        values.put(PensumHelper.COLUMN_U_HV_ACTIVO, newValor);

        // Which row to update, based on the ID
        String selection = PensumHelper.COLUMN_U_COD + " LIKE ?";
        String[] selectionArgs = { String.valueOf(ma_cod) };

        mDatabase.update(PensumHelper.TABLE_USER_MATERIA, values, selection, selectionArgs);
    }
    public void deleteUserMateria(String u_ma_cod) {
        String selection = PensumHelper.COLUMN_U_COD + " LIKE ?";
        String[] selectionArgs = { String.valueOf(u_ma_cod) };
        mDatabase.delete(PensumHelper.TABLE_USER_MATERIA, selection, selectionArgs);
    }
    public  ArrayList<Materia> getAllUserMateria(){
        ArrayList<Materia> listUserMateria = new ArrayList<>();

        String[] columns = {PensumHelper.COLUMN_U_UID,
                PensumHelper.COLUMN_U_COD,
                PensumHelper.COLUMN_U_TITULO,
                PensumHelper.COLUMN_U_SEMESTRE,
                PensumHelper.COLUMN_U_CONTENIDO,
                PensumHelper.COLUMN_U_OBJETIVO,
                PensumHelper.COLUMN_U_MODULO,
                PensumHelper.COLUMN_U_HV_ACTIVO
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
                materia.setU_materia(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_HV_ACTIVO)));
                //add the materia to the list of materia objects which we plan to return
                listUserMateria.add(materia);
            }
            while (cursor.moveToNext());
        }
        return listUserMateria;
    }
    public Materia getOneUserMateria(String u_ma_cod){
        Materia materia=new Materia();

        // Which row to update, based on the ID
        String selection = PensumHelper.COLUMN_U_COD + " LIKE ?";
        String[] selectionArgs = { String.valueOf(u_ma_cod) };

        String[] columns = {PensumHelper.COLUMN_U_UID,
                PensumHelper.COLUMN_U_COD,
                PensumHelper.COLUMN_U_TITULO,
                PensumHelper.COLUMN_U_SEMESTRE,
                PensumHelper.COLUMN_U_CONTENIDO,
                PensumHelper.COLUMN_U_OBJETIVO,
                PensumHelper.COLUMN_U_MODULO,
                PensumHelper.COLUMN_U_HV_ACTIVO
        };
        Cursor cursor = mDatabase.query(PensumHelper.TABLE_USER_MATERIA, columns, selection, selectionArgs, null, null, null);//parametros no estudiados
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank materia object to contain our data
                materia.setCod(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_COD)));
                materia.setTitulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_TITULO)));
                materia.setSemestre(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_SEMESTRE)));
                materia.setObjetivo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_CONTENIDO)));
                materia.setContenido(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_OBJETIVO)));
                materia.setModulo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_MODULO)));
                materia.setU_materia(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_U_HV_ACTIVO)));

            }
            while (cursor.moveToNext());
        }
        return materia;
    }


    public void insertMenuStatus(MenuStatus menuStatus){

        ContentValues values =  new ContentValues();
        values.put(PensumHelper.COLUMN_M_COD,menuStatus.getCod());
        values.put(PensumHelper.COLUMN_M_ITEM,menuStatus.getItem());
        values.put(PensumHelper.COLUMN_M_ACTIVO,menuStatus.getActivo());


        mDatabase.insert(PensumHelper.TABLE_MENU_STATUS,
               null ,values);

    }
    public  void updateMenuStatus(MenuStatus menuStatus){

        ContentValues values = new ContentValues();
        values.put(PensumHelper.COLUMN_M_ITEM, menuStatus.getItem());
        values.put(PensumHelper.COLUMN_M_ACTIVO, menuStatus.getActivo());

        // Which row to update, based on the ID
        String selection = PensumHelper.COLUMN_M_COD + " LIKE ?";
        String[] selectionArgs = { String.valueOf(menuStatus.getCod()) };

        mDatabase.update(PensumHelper.TABLE_MENU_STATUS, values, selection, selectionArgs);
    }
    public  ArrayList<MenuStatus> getAllMenuStatus(){
        ArrayList<MenuStatus> listMenuStatus = new ArrayList<>();

        String[] columns = {PensumHelper.COLUMN_M_COD,
                PensumHelper.COLUMN_M_ITEM,
                PensumHelper.COLUMN_M_ACTIVO
        };
        Cursor cursor = mDatabase.query(PensumHelper.TABLE_MENU_STATUS, columns, null, null, null, null, null);//parametros no estudiados
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new materia object and retrieve the data from the cursor to be stored in this materia object
                MenuStatus menuStatus = new MenuStatus();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank materia object to contain our data
                menuStatus.setCod(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_M_COD)));
                menuStatus.setItem(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_M_ITEM)));
                menuStatus.setActivo(cursor.getString(cursor.getColumnIndex(PensumHelper.COLUMN_M_ACTIVO)));
                //add the materia to the list of materia objects which we plan to return
                listMenuStatus.add(menuStatus);
            }
            while (cursor.moveToNext());
        }
        return listMenuStatus;
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

        /*****************************************************************
         *  //////Tabla DE MATERIA//////////////////////////////////////
         *****************************************************************/
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

        /******************************************************************
         * ///////// TABLA DE ESTADOS DEL MENU PRINCIPAL!////////////////
         ******************************************************************/
        public static final String TABLE_MENU_STATUS = "menu_status";
        public static final String COLUMN_M_UID = "me_id";
        public static final String COLUMN_M_COD = "me_cod";
        public static final String COLUMN_M_ITEM = "me_item";
        public static final String COLUMN_M_ACTIVO = "me_activo";

        private static final String CREATE_TABLE_MENU_STATUS = "CREATE TABLE " + TABLE_MENU_STATUS + " (" +
                COLUMN_M_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_M_COD + " TEXT, " +
                COLUMN_M_ITEM + " TEXT DEFAULT 0," +
                COLUMN_M_ACTIVO + " TEXT DEFAULT 0" +
                ");";

        private static final String INSERT_MENU_STATUS ="INSERT INTO "+TABLE_MENU_STATUS+" ("+
                COLUMN_M_COD+") VALUES"+
                "('pensumSub1')," +
                "('pensumSub2')," +
                "('matSub1')," +
                "('matSub2')," +
                "('hvSub1')," +
                "('hvSub2')," +
                "('ntSub1')," +
                "('ntSub2')," +
                "('calSub1')," +
                "('calSub2')," +
                "('prfSub1')," +
                "('prfSub2');";


        /***********************************************************************
         * ////////// TABLA DE MATERIAS SELECCIONADAS POR EL USUARIO//////////
         ***********************************************************************/
        public static final String TABLE_USER_MATERIA = "user_materia";
        public static final String COLUMN_U_UID = "u_ma_id";
        public static final String COLUMN_U_COD = "u_ma_cod";
        public static final String COLUMN_U_TITULO = "u_ma_title";
        public static final String COLUMN_U_SEMESTRE = "u_ma_semestre";
        public static final String COLUMN_U_OBJETIVO = "u_ma_objetivo";
        public static final String COLUMN_U_CONTENIDO = "u_ma_contenido";
        public static final String COLUMN_U_MODULO = "u_ma_modulo";
        public static final String COLUMN_U_HV_ACTIVO = "u_ma_hv_activo";

        private static final String CREATE_TABLE_USER_MATERIA = "CREATE TABLE " + TABLE_USER_MATERIA + " (" +
                COLUMN_U_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_U_COD + " TEXT, " +
                COLUMN_U_TITULO + " TEXT," +
                COLUMN_U_SEMESTRE + " TEXT," +
                COLUMN_U_OBJETIVO + " TEXT," +
                COLUMN_U_CONTENIDO + " TEXT," +
                COLUMN_U_MODULO + " TEXT," +
                COLUMN_U_HV_ACTIVO+" TEXT DEFAULT 0"+
                ");";

        /***********************************************************************
         * ////////// TABLA DE HORARIO VIRTUAL////////////////////////////////
         ***********************************************************************/
        public static final String TABLE_HORARIO_VIRTUAL = "horario_virtual";
        public static final String COLUMN_HV_UID = "hv_id";
        public static final String COLUMN_HV_COD = "hv_ma_cod";
        public static final String COLUMN_HV_TITULO = "hv_title";
        public static final String COLUMN_HV_CALENDAR = "hv_calendar";
        public static final String COLUMN_HV_CAL_INI = "hv_cal_ini";
        public static final String COLUMN_HV_CAL_END = "hv_cal_end";
        public static final String COLUMN_HV_COLOR = "hv_color";

        private static final String CREATE_TABLE_HORARIO_VIRTUAL = "CREATE TABLE " + TABLE_HORARIO_VIRTUAL + " (" +
                COLUMN_HV_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_HV_COD + " TEXT, " +
                COLUMN_HV_TITULO + " TEXT," +
                COLUMN_HV_CALENDAR + " TEXT," +
                COLUMN_HV_CAL_INI + " DATETIME," +
                COLUMN_HV_CAL_END + " DATETIME," +
                COLUMN_HV_COLOR + " INTEGER" +
                ");";

        public static final String TABLE_HV_WEEK = "horario_virtual_week";
        public static final String COLUMN_HV_WEEK_UID = "hvw_id";
        public static final String COLUMN_HV_WEEK_COD = "hvw_ma_cod";
        public static final String COLUMN_HV_WEEK_MOD = "hvw_mod";
        public static final String COLUMN_HV_WEEK_AULA = "hvw_aula";
        public static final String COLUMN_HV_WEEK_WEEKDAY = "hvw_weekday";
        public static final String COLUMN_HV_WEEK_START = "hvw_time_start";
        public static final String COLUMN_HV_WEEK_END = "hvw_time_end";




        private static final String CREATE_TABLE_HORARIO_VIRTUAL_WEEK = "CREATE TABLE " + TABLE_HV_WEEK + " (" +
                COLUMN_HV_WEEK_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_HV_WEEK_COD + " TEXT, " +
                COLUMN_HV_WEEK_MOD + " TEXT," +
                COLUMN_HV_WEEK_AULA + " TEXT," +
                COLUMN_HV_WEEK_WEEKDAY + " TEXT," +
                COLUMN_HV_WEEK_START + " DATETIME," +
                COLUMN_HV_WEEK_END + " DATETIME" +

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
                db.execSQL(CREATE_TABLE_MENU_STATUS);
                db.execSQL(INSERT_MENU_STATUS);
                db.execSQL(CREATE_TABLE_HORARIO_VIRTUAL);
                db.execSQL(CREATE_TABLE_HORARIO_VIRTUAL_WEEK);
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