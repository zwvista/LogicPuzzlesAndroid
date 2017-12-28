package com.zwstudio.logicpuzzlesandroid.home.android;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.zwstudio.logicpuzzlesandroid.R;

import java.util.Random;

// http://stackoverflow.com/questions/27579765/play-background-music-in-all-activities-of-android-app
// http://www.codeproject.com/Articles/258176/Adding-Background-Music-to-Android-App
public class MusicService extends Service  implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;
    private int length = 0;
    private boolean playMusic;
    Random rand = new Random();

    public MusicService() {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        createMediaPlayer();
    }

    class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createMediaPlayer();
    }

    private void createMediaPlayer() {
        int n = rand.nextInt() % 2;
        mPlayer = MediaPlayer.create(this, n == 0 ? R.raw.music1 : R.raw.music2);
        mPlayer.setVolume(100, 100);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnCompletionListener(this);
        if (playMusic) resumeMusic();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();

        // http://stackoverflow.com/questions/5301891/android-start-service-with-parameter
        Bundle extras = intent.getExtras();
        playMusic = extras.getBoolean("playMusic");
        if (!playMusic) pauseMusic();

        return START_STICKY;
    }

    public void pauseMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();

        }
    }

    public void resumeMusic() {
        if (!mPlayer.isPlaying()) {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }
}