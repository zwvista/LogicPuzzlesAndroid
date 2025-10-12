package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.zwstudio.logicpuzzlesandroid.BuildConfig.APPLICATION_ID
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import dalvik.system.DexFile
import io.realm.Realm
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.reflect.KClass


// https://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/48767617#48767617
class LogicPuzzlesApplication : Application(), LifecycleObserver {
    val homeDocument: HomeDocument by inject()
    private val soundManager: SoundManager by inject()

    override fun onCreate() {
        super.onCreate()
        val logicPuzzlesModule = module {
            single { SoundManager(androidContext() as LogicPuzzlesApplication) }
            single { HomeDocument() }
            scanAllDocuments()
        }
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

    private fun Module.scanAllDocuments() {
        try {
            val packageName = "${APPLICATION_ID}.puzzles"
            val documentClasses = findDocumentClasses(applicationContext,packageName)

            documentClasses.forEach { clazz ->
                single {
                    clazz.getDeclaredConstructor(Context::class.java)
                        .newInstance(androidContext())
                } bind clazz.kotlin as KClass<Any>
                Log.d("Koin", "✅ Registered: ${clazz.simpleName}")
            }

            Log.d("Koin", "Total registered: ${documentClasses.size}")
        } catch (e: Exception) {
            Log.e("Koin", "Error scanning Document classes", e)
        }
    }

    fun findDocumentClasses(context: Context, basePackage: String): List<Class<*>> {
        val result = mutableListOf<Class<*>>()
        val path = context.packageCodePath
        val dexFile = DexFile(path)

        val entries = dexFile.entries()
        while (entries.hasMoreElements()) {
            val className = entries.nextElement()
            if (className.startsWith(basePackage) && className.endsWith("Document")) {
                try {
                    result.add(Class.forName(className))
                } catch (_: Throwable) {
                }
            }
        }
        return result
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
