package com.zwstudio.logicgamesandroid.logicgames.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.zwstudio.logicgamesandroid.bridges.data.BridgesGameProgress;
import com.zwstudio.logicgamesandroid.bridges.data.BridgesLevelProgress;
import com.zwstudio.logicgamesandroid.bridges.data.BridgesMoveProgress;
import com.zwstudio.logicgamesandroid.clouds.data.CloudsGameProgress;
import com.zwstudio.logicgamesandroid.clouds.data.CloudsLevelProgress;
import com.zwstudio.logicgamesandroid.clouds.data.CloudsMoveProgress;
import com.zwstudio.logicgamesandroid.lightup.data.LightUpGameProgress;
import com.zwstudio.logicgamesandroid.lightup.data.LightUpLevelProgress;
import com.zwstudio.logicgamesandroid.lightup.data.LightUpMoveProgress;
import com.zwstudio.logicgamesandroid.slitherlink.data.SlitherLinkGameProgress;
import com.zwstudio.logicgamesandroid.slitherlink.data.SlitherLinkLevelProgress;
import com.zwstudio.logicgamesandroid.slitherlink.data.SlitherLinkMoveProgress;

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

    private Dao<LogicGamesGameProgress, Integer> daoLogicGamesGameProgress;

    private Dao<LightUpGameProgress, Integer> daoLightUpGameProgress;
    private Dao<LightUpLevelProgress, Integer> daoLightUpLevelProgress;
    private Dao<LightUpMoveProgress, Integer> daoLightUpMoveProgress;

    private Dao<BridgesGameProgress, Integer> daoBridgesGameProgress;
    private Dao<BridgesLevelProgress, Integer> daoBridgesLevelProgress;
    private Dao<BridgesMoveProgress, Integer> daoBridgesMoveProgress;

    private Dao<SlitherLinkGameProgress, Integer> daoSlitherLinkGameProgress;
    private Dao<SlitherLinkLevelProgress, Integer> daoSlitherLinkLevelProgress;
    private Dao<SlitherLinkMoveProgress, Integer> daoSlitherLinkMoveProgress;

    private Dao<CloudsGameProgress, Integer> daoCloudsGameProgress;
    private Dao<CloudsLevelProgress, Integer> daoCloudsLevelProgress;
    private Dao<CloudsMoveProgress, Integer> daoCloudsMoveProgress;

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
        daoLogicGamesGameProgress = null;
        daoLightUpGameProgress = null;
        daoLightUpLevelProgress = null;
        daoLightUpMoveProgress = null;
        daoBridgesGameProgress = null;
        daoBridgesLevelProgress = null;
        daoBridgesMoveProgress = null;
        daoSlitherLinkGameProgress = null;
        daoSlitherLinkLevelProgress = null;
        daoSlitherLinkMoveProgress = null;
        daoCloudsGameProgress = null;
        daoCloudsLevelProgress = null;
        daoCloudsMoveProgress = null;
    }

    public Dao<LogicGamesGameProgress, Integer> getDaoLogicGamesGameProgress() throws SQLException {
        if (daoLogicGamesGameProgress == null)
            daoLogicGamesGameProgress = getDao(LogicGamesGameProgress.class);
        return daoLogicGamesGameProgress;
    }

    public Dao<LightUpGameProgress, Integer> getDaoLightUpGameProgress() throws SQLException {
        if (daoLightUpGameProgress == null)
            daoLightUpGameProgress = getDao(LightUpGameProgress.class);
        return daoLightUpGameProgress;
    }

    public Dao<LightUpLevelProgress, Integer> getDaoLightUpLevelProgress() throws SQLException {
        if (daoLightUpLevelProgress == null)
            daoLightUpLevelProgress = getDao(LightUpLevelProgress.class);
        return daoLightUpLevelProgress;
    }

    public Dao<LightUpMoveProgress, Integer> getDaoLightUpMoveProgress() throws SQLException {
        if (daoLightUpMoveProgress == null)
            daoLightUpMoveProgress = getDao(LightUpMoveProgress.class);
        return daoLightUpMoveProgress;
    }

    public Dao<BridgesGameProgress, Integer> getDaoBridgesGameProgress() throws SQLException {
        if (daoBridgesGameProgress == null)
            daoBridgesGameProgress = getDao(BridgesGameProgress.class);
        return daoBridgesGameProgress;
    }

    public Dao<BridgesLevelProgress, Integer> getDaoBridgesLevelProgress() throws SQLException {
        if (daoBridgesLevelProgress == null)
            daoBridgesLevelProgress = getDao(BridgesLevelProgress.class);
        return daoBridgesLevelProgress;
    }

    public Dao<BridgesMoveProgress, Integer> getDaoBridgesMoveProgress() throws SQLException {
        if (daoBridgesMoveProgress == null)
            daoBridgesMoveProgress = getDao(BridgesMoveProgress.class);
        return daoBridgesMoveProgress;
    }

    public Dao<SlitherLinkGameProgress, Integer> getDaoSlitherLinkGameProgress() throws SQLException {
        if (daoSlitherLinkGameProgress == null)
            daoSlitherLinkGameProgress = getDao(SlitherLinkGameProgress.class);
        return daoSlitherLinkGameProgress;
    }

    public Dao<SlitherLinkLevelProgress, Integer> getDaoSlitherLinkLevelProgress() throws SQLException {
        if (daoSlitherLinkLevelProgress == null)
            daoSlitherLinkLevelProgress = getDao(SlitherLinkLevelProgress.class);
        return daoSlitherLinkLevelProgress;
    }

    public Dao<SlitherLinkMoveProgress, Integer> getDaoSlitherLinkMoveProgress() throws SQLException {
        if (daoSlitherLinkMoveProgress == null)
            daoSlitherLinkMoveProgress = getDao(SlitherLinkMoveProgress.class);
        return daoSlitherLinkMoveProgress;
    }

    public Dao<CloudsGameProgress, Integer> getDaoCloudsGameProgress() throws SQLException {
        if (daoCloudsGameProgress == null)
            daoCloudsGameProgress = getDao(CloudsGameProgress.class);
        return daoCloudsGameProgress;
    }

    public Dao<CloudsLevelProgress, Integer> getDaoCloudsLevelProgress() throws SQLException {
        if (daoCloudsLevelProgress == null)
            daoCloudsLevelProgress = getDao(CloudsLevelProgress.class);
        return daoCloudsLevelProgress;
    }

    public Dao<CloudsMoveProgress, Integer> getDaoCloudsMoveProgress() throws SQLException {
        if (daoCloudsMoveProgress == null)
            daoCloudsMoveProgress = getDao(CloudsMoveProgress.class);
        return daoCloudsMoveProgress;
    }

}