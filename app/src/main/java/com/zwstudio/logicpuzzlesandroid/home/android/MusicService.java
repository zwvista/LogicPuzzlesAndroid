package com.zwstudio.logicpuzzlesandroid.home.android;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.zwstudio.logicpuzzlesandroid.R;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import static fj.data.Stream.range;

// http://stackoverflow.com/questions/27579765/play-background-music-in-all-activities-of-android-app
// http://www.codeproject.com/Articles/258176/Adding-Background-Music-to-Android-App
// https://stackoverflow.com/questions/31419328/is-it-possible-to-have-mediaplayer-play-one-audio-file-and-when-it-finishes-play
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
        stopMusic();
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
        // https://stackoverflow.com/questions/6539715/android-how-do-can-i-get-a-list-of-all-files-in-a-folder
        Field[] fields = R.raw.class.getFields();
        List<Integer> indexes = range(0, fields.length)
                .filter(i -> StringUtils.startsWith(fields[i].getName(), "music")).toJavaList();
        int n = rand.nextInt() % indexes.size();
        try {
            mPlayer = MediaPlayer.create(this, fields[indexes.get(n)].getInt(fields[indexes.get(n)]));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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

    @Override
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