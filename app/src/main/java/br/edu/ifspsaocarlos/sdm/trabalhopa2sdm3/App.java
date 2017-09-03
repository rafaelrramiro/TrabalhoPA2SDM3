package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3;

import android.app.Application;

/**
 * Created by Note on 03/09/2017.
 */

public class App  extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chatifsp-db");
        Database db =  helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}