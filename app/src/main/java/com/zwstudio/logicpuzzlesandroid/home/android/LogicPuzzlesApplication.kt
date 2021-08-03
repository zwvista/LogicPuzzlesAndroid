package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.AbcDocument
import io.realm.Realm
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

// https://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/48767617#48767617
class LogicPuzzlesApplication : Application(), LifecycleObserver {
    val homeDocument: HomeDocument by inject()
    private val soundManager: SoundManager by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Koin Android logger
            androidLogger(Level.ERROR)
            //inject Android context
            androidContext(this@LogicPuzzlesApplication)
            // use modules
            modules(logicPuzzlesModule)
        }
        Realm.init(this)
        realm = Realm.getDefaultInstance()
        soundManager.init()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
    private val logicPuzzlesModule = module {
        single { SoundManager(androidContext() as LogicPuzzlesApplication) }
        single { HomeDocument() }
        single { AbcDocument(androidContext()) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        soundManager.activityStarted()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        soundManager.activityStopped()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        soundManager.doUnbindService()
    }
}

lateinit var realm: Realm
