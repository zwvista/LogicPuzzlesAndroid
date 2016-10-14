package com.zwstudio.logicgamesandroid.lightup.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "Game.db";
    private static String databasePath;

    private static final int DATABASE_VERSION = 1;

    private Dao<GameProgress, Integer> daoGameProgress;
    private Dao<LevelProgress, Integer> daoLevelProgress;
    private Dao<MoveProgress, Integer> daoMoveProgress;

    public DBHelper(Context context) throws IOException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        databasePath = context.getDatabasePath(DATABASE_NAME).getParent() + "/";
        boolean dbexist = checkdatabase();
        if (!dbexist) {
            // If database did not exist, try copying existing database from assets folder.
            try {
                File dir = new File(databasePath);
                dir.mkdirs();
                InputStream myinput = context.getAssets().open(DATABASE_NAME);
                String outfilename = databasePath + DATABASE_NAME;
                Log.i(DBHelper.class.getName(), "DB Path : " + outfilename);
                OutputStream myoutput = new FileOutputStream(outfilename);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myinput.read(buffer)) > 0) {
                    myoutput.write(buffer, 0, length);
                }
                myoutput.flush();
                myoutput.close();
                myinput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Check whether or not database exist
    */
    private boolean checkdatabase() {
        boolean checkdb = false;

        String myPath = databasePath + DATABASE_NAME;
        File dbfile = new File(myPath);
        checkdb = dbfile.exists();

        Log.i(DBHelper.class.getName(), "DB Exist : " + checkdb);

        return checkdb;
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        daoGameProgress = null;
        daoMoveProgress = null;
        daoLevelProgress = null;
    }

    public Dao<GameProgress, Integer> getDaoGameProgress() throws SQLException {
        if (daoGameProgress == null)
            daoGameProgress = getDao(GameProgress.class);
        return daoGameProgress;
    }

    public Dao<LevelProgress, Integer> getDaoLevelProgress() throws SQLException {
        if (daoLevelProgress == null)
            daoLevelProgress = getDao(LevelProgress.class);
        return daoLevelProgress;
    }

    public Dao<MoveProgress, Integer> getDaoMoveProgress() throws SQLException {
        if (daoMoveProgress == null)
            daoMoveProgress = getDao(MoveProgress.class);
        return daoMoveProgress;
    }

}