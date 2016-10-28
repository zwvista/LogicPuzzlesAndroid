package com.zwstudio.logicgamesandroid.logicgames.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;

import com.zwstudio.logicgamesandroid.R;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by zwvista on 2016/10/28.
 */

@EBean
public class SoundManager {
    @App
    GameApplication app;

    // http://www.codeproject.com/Articles/258176/Adding-Background-Music-to-Android-App
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        app.bindService(new Intent(app, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            app.unbindService(Scon);
            mIsBound = false;
        }
    }

    // http://stackoverflow.com/questions/13001363/using-mediaplayer-to-play-the-same-file-multiple-times-with-overlap
    // http://www.vogella.com/tutorials/AndroidMedia/article.html#tutorial_soundpool
    private SoundPool soundPool;
    private int soundIDTap, soundIDSolved;
    boolean loaded = false;

    // http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
    private int stateCounter;

    void init() {
        doBindService();
        Intent music = new Intent();
        music.setClass(app, MusicService.class);
        // http://stackoverflow.com/questions/5301891/android-start-service-with-parameter
        music.putExtra("playMusic", app.logicGamesDocument.gameProgress().playMusic);
        app.startService(music);

        // Load the sound
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundIDTap = soundPool.load(app, R.raw.tap, 1);
        soundIDSolved = soundPool.load(app, R.raw.solved, 1);

        stateCounter = 0;
    }

    public void playOrPauseMusic() {
        if (mServ == null) return;
        if (app.logicGamesDocument.gameProgress().playMusic)
            mServ.resumeMusic();
        else
            mServ.pauseMusic();
    }

    public void activityStarted() {
        stateCounter++;
        if (stateCounter == 1 && mServ != null && app.logicGamesDocument.gameProgress().playMusic)
            mServ.resumeMusic();
    }

    public void activityStopped() {
        stateCounter--;
        if (stateCounter == 0 && mServ != null && app.logicGamesDocument.gameProgress().playMusic)
            mServ.pauseMusic();
    }

    private void playSound(int soundID) {
        AudioManager audioManager = (AudioManager) app.getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        // Is the sound loaded already?
        if (loaded && app.logicGamesDocument.gameProgress().playSound)
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
    }

    public void playSoundTap() {
        playSound(soundIDTap);
    }

    public void playSoundSolved() {
        playSound(soundIDSolved);
    }
}
