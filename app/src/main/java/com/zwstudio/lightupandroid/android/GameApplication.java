package com.zwstudio.lightupandroid.android;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zwstudio.lightupandroid.data.DBHelper;
import com.zwstudio.lightupandroid.data.GameDocument;

public class GameApplication extends Application {
    private DBHelper dbHelper = null;

    private GameDocument gameDocument;

    @Override
    public void onCreate() {
        super.onCreate();
        gameDocument = new GameDocument(getDBHelper());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    public DBHelper getDBHelper() {
        if (dbHelper == null)
            dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        return dbHelper;
    }

    public GameDocument getGameDocument() {
        return gameDocument;
    }
}
