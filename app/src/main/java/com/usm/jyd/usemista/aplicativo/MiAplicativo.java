package com.usm.jyd.usemista.aplicativo;

import android.app.Application;
import android.content.Context;

import com.usm.jyd.usemista.database.DBPensum;

/**
 * Created by der_w on 10/9/2015.
 */
public class MiAplicativo extends Application {

    private static MiAplicativo sInstance;

    private static DBPensum mDatabase;

    public static MiAplicativo getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public synchronized static DBPensum getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DBPensum(getAppContext());
        }
        return mDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDatabase = new DBPensum(this);
    }
}
