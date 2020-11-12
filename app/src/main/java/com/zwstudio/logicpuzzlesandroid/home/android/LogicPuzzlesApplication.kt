package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Application
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import io.realm.Realm
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EApplication

@EApplication
open class LogicPuzzlesApplication : Application() {
    @Bean
    lateinit var homeDocument: HomeDocument

    @Bean
    lateinit var soundManager: SoundManager
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        realm = Realm.getDefaultInstance()
        // cannot use @AfterInject
        soundManager.init()
    }

    override fun onTerminate() {
        super.onTerminate()
        soundManager.doUnbindService()
    }
}

lateinit var realm: Realm
