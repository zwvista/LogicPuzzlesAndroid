package com.zwstudio.lightupandroid.android;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zwstudio.lightupandroid.data.DBHelper;
import com.zwstudio.lightupandroid.data.GameDocument;

import java.io.IOException;
import java.io.InputStream;

public class GameApplication extends Application {
    private DBHelper dbHelper = null;

    private GameDocument doc;

    @Override
    public void onCreate() {
        super.onCreate();
        doc = new GameDocument(getDBHelper());
        InputStream is = null;
        try {
            is = getApplicationContext().getAssets().open("Levels.xml");
            doc.loadXml(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return doc;
    }
}
