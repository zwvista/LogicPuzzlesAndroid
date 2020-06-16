package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import com.zwstudio.logicpuzzlesandroid.R.raw
import com.zwstudio.logicpuzzlesandroid.home.android.MusicService
import fj.data.Stream
import org.apache.commons.lang3.StringUtils
import java.util.*

// http://stackoverflow.com/questions/27579765/play-background-music-in-all-activities-of-android-app
// http://www.codeproject.com/Articles/258176/Adding-Background-Music-to-Android-App
// https://stackoverflow.com/questions/31419328/is-it-possible-to-have-mediaplayer-play-one-audio-file-and-when-it-finishes-play
class MusicService : Service(), MediaPlayer.OnErrorListener, OnCompletionListener {
    private val mBinder: IBinder = ServiceBinder()
    var mPlayer: MediaPlayer? = null
    private var length = 0
    private var playMusic = false
    var rand = Random()
    override fun onCompletion(mp: MediaPlayer) {
        stopMusic()
        createMediaPlayer()
    }

    internal inner class ServiceBinder : Binder() {
        val service get() = this@MusicService
    }

    override fun onBind(arg0: Intent): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        createMediaPlayer()
    }

    private fun createMediaPlayer() {
        // https://stackoverflow.com/questions/6539715/android-how-do-can-i-get-a-list-of-all-files-in-a-folder
        val fields = raw::class.java.fields
        val indexes = Stream.range(0, fields.size.toLong())
            .filter { i: Int? -> StringUtils.startsWith(fields[i!!].name, "music") }.toJavaList()
        val n = rand.nextInt(indexes.size)
        mPlayer = MediaPlayer.create(this, fields[indexes[n]].getInt(fields[indexes[n]]))
        mPlayer!!.setVolume(100f, 100f)
        mPlayer!!.setOnErrorListener(this)
        mPlayer!!.setOnCompletionListener(this)
        if (playMusic) resumeMusic()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mPlayer!!.start()

        // http://stackoverflow.com/questions/5301891/android-start-service-with-parameter
        val extras = intent.extras
        playMusic = extras!!.getBoolean("playMusic")
        if (!playMusic) pauseMusic()
        return START_STICKY
    }

    fun pauseMusic() {
        if (mPlayer!!.isPlaying) {
            mPlayer!!.pause()
            length = mPlayer!!.currentPosition
        }
    }

    fun resumeMusic() {
        if (!mPlayer!!.isPlaying) {
            mPlayer!!.seekTo(length)
            mPlayer!!.start()
        }
    }

    fun stopMusic() {
        mPlayer!!.stop()
        mPlayer!!.release()
        mPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPlayer != null) {
            try {
                mPlayer!!.stop()
                mPlayer!!.release()
            } finally {
                mPlayer = null
            }
        }
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show()
        if (mPlayer != null) {
            try {
                mPlayer!!.stop()
                mPlayer!!.release()
            } finally {
                mPlayer = null
            }
        }
        return false
    }
}