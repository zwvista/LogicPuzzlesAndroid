package com.zwstudio.logicpuzzlesandroid.home.android;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.LevelProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.home.data.DBHelper;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeGameProgress;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.ormlite.annotations.OrmLiteDao;


@EApplication
public class LogicPuzzlesApplication extends Application {

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<HomeGameProgress, Integer> daoHomeGameProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<GameProgress, Integer> daoGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<LevelProgress, Integer> daoLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<MoveProgress, Integer> daoMoveProgress;

    @Bean
    public HomeDocument homeDocument;
    @Bean
    public SoundManager soundManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // cannot use @AfterInject
        soundManager.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        soundManager.doUnbindService();
        OpenHelperManager.releaseHelper();
    }
}
