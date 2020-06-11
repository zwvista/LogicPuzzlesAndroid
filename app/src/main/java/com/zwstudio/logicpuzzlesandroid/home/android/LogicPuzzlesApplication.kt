package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Application
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.Dao
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress
import com.zwstudio.logicpuzzlesandroid.common.data.LevelProgress
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.home.data.DBHelper
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import com.zwstudio.logicpuzzlesandroid.home.data.HomeGameProgress
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EApplication
import org.androidannotations.ormlite.annotations.OrmLiteDao

@EApplication
open class LogicPuzzlesApplication : Application() {
    @kotlin.jvm.JvmField
    @OrmLiteDao(helper = DBHelper::class)
    var daoHomeGameProgress: Dao<HomeGameProgress?, Int?>? = null

    @kotlin.jvm.JvmField
    @OrmLiteDao(helper = DBHelper::class)
    var daoGameProgress: Dao<GameProgress?, Int?>? = null

    @kotlin.jvm.JvmField
    @OrmLiteDao(helper = DBHelper::class)
    var daoLevelProgress: Dao<LevelProgress?, Int?>? = null

    @kotlin.jvm.JvmField
    @OrmLiteDao(helper = DBHelper::class)
    var daoMoveProgress: Dao<MoveProgress?, Int?>? = null

    @kotlin.jvm.JvmField
    @Bean
    var homeDocument: HomeDocument? = null

    @kotlin.jvm.JvmField
    @Bean
    var soundManager: SoundManager? = null
    override fun onCreate() {
        super.onCreate()
        // cannot use @AfterInject
        soundManager!!.init()
    }

    override fun onTerminate() {
        super.onTerminate()
        soundManager!!.doUnbindService()
        OpenHelperManager.releaseHelper()
    }
}