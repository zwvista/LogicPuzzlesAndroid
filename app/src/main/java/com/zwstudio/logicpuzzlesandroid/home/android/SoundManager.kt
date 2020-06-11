package com.zwstudio.logicpuzzlesandroid.home.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.media.SoundPool
import android.os.IBinder
import com.zwstudio.logicpuzzlesandroid.R.raw
import com.zwstudio.logicpuzzlesandroid.home.android.MusicService.ServiceBinder
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean

@EBean
open class SoundManager {
    @kotlin.jvm.JvmField
    @App
    var app: LogicPuzzlesApplication? = null

    // http://www.codeproject.com/Articles/258176/Adding-Background-Music-to-Android-App
    private var mIsBound = false
    private var mServ: MusicService? = null
    private val Scon: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            mServ = (binder as ServiceBinder).service
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mServ = null
        }
    }

    private fun doBindService() {
        app!!.bindService(Intent(app, MusicService::class.java),
            Scon, Context.BIND_AUTO_CREATE)
        mIsBound = true
    }

    fun doUnbindService() {
        if (mIsBound) {
            app!!.unbindService(Scon)
            mIsBound = false
        }
    }

    // http://stackoverflow.com/questions/13001363/using-mediaplayer-to-play-the-same-file-multiple-times-with-overlap
    // http://www.vogella.com/tutorials/AndroidMedia/article.html#tutorial_soundpool
    private var soundPool: SoundPool? = null
    private var soundIDTap = 0
    private var soundIDSolved = 0
    private var loaded = false

    // http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
    private var stateCounter = 0
    fun init() {
        doBindService()
        val music = Intent()
        music.setClass(app!!, MusicService::class.java)
        // http://stackoverflow.com/questions/5301891/android-start-service-with-parameter
        music.putExtra("playMusic", app!!.homeDocument!!.gameProgress()!!.playMusic)
        app!!.startService(music)

        // Load the sound
        soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)
        soundPool!!.setOnLoadCompleteListener { soundPool, sampleId, status -> loaded = true }
        soundIDTap = soundPool!!.load(app, raw.tap, 1)
        soundIDSolved = soundPool!!.load(app, raw.solved, 1)
        stateCounter = 0
    }

    fun playOrPauseMusic() {
        if (mServ == null) return
        if (app!!.homeDocument!!.gameProgress()!!.playMusic) mServ!!.resumeMusic() else mServ!!.pauseMusic()
    }

    fun activityStarted() {
        stateCounter++
        if (stateCounter == 1 && mServ != null && app!!.homeDocument!!.gameProgress()!!.playMusic) mServ!!.resumeMusic()
    }

    fun activityStopped() {
        stateCounter--
        if (stateCounter == 0 && mServ != null && app!!.homeDocument!!.gameProgress()!!.playMusic) mServ!!.pauseMusic()
    }

    private fun playSound(soundID: Int) {
        val audioManager = app!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val actualVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        val volume = actualVolume / maxVolume
        // Is the sound loaded already?
        if (loaded && app!!.homeDocument!!.gameProgress()!!.playSound) soundPool!!.play(soundID, volume, volume, 1, 0, 1f)
    }

    fun playSoundTap() {
        playSound(soundIDTap)
    }

    fun playSoundSolved() {
        playSound(soundIDSolved)
    }
}