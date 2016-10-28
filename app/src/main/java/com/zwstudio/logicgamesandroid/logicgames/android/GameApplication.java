package com.zwstudio.logicgamesandroid.logicgames.android;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.bridges.data.BridgesDocument;
import com.zwstudio.logicgamesandroid.clouds.data.CloudsDocument;
import com.zwstudio.logicgamesandroid.hitori.data.HitoriDocument;
import com.zwstudio.logicgamesandroid.lightup.data.LightUpDocument;
import com.zwstudio.logicgamesandroid.logicgames.data.DBHelper;
import com.zwstudio.logicgamesandroid.logicgames.data.LogicGamesDocument;
import com.zwstudio.logicgamesandroid.slitherlink.data.SlitherLinkDocument;

public class GameApplication extends Application {

    public LogicGamesDocument logicGamesDocument;
    public LightUpDocument lightUpDocument;
    public BridgesDocument bridgesDocument;
    public SlitherLinkDocument slitherlinkDocument;
    public CloudsDocument cloudsDocument;
    public HitoriDocument hitoriDocument;

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
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
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

    @Override
    public void onCreate() {
        super.onCreate();

        DBHelper db = OpenHelperManager.getHelper(this, DBHelper.class);
        logicGamesDocument = new LogicGamesDocument(db);
        lightUpDocument = new LightUpDocument(db, getApplicationContext(), "LightUpLevels.xml");
        bridgesDocument = new BridgesDocument(db, getApplicationContext(), "BridgesLevels.xml");
        slitherlinkDocument = new SlitherLinkDocument(db, getApplicationContext(), "SlitherLinkLevels.xml");
        cloudsDocument = new CloudsDocument(db, getApplicationContext(), "CloudsLevels.xml");
        hitoriDocument = new HitoriDocument(db, getApplicationContext(), "HitoriLevels.xml");

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        // http://stackoverflow.com/questions/5301891/android-start-service-with-parameter
        music.putExtra("playMusic", logicGamesDocument.gameProgress().playMusic);
        startService(music);

        // Load the sound
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundIDTap = soundPool.load(this, R.raw.tap, 1);
        soundIDSolved = soundPool.load(this, R.raw.solved, 1);

        stateCounter = 0;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        doUnbindService();
        OpenHelperManager.releaseHelper();
    }

    public void playOrPauseMusic() {
        if (mServ == null) return;
        if (logicGamesDocument.gameProgress().playMusic)
            mServ.resumeMusic();
        else
            mServ.pauseMusic();
    }

    public void activityStarted() {
        stateCounter++;
        if (stateCounter == 1 && mServ != null && logicGamesDocument.gameProgress().playMusic)
            mServ.resumeMusic();
    }

    public void activityStopped() {
        stateCounter--;
        if (stateCounter == 0 && mServ != null && logicGamesDocument.gameProgress().playMusic)
            mServ.pauseMusic();
    }

    private void playSound(int soundID) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        // Is the sound loaded already?
        if (loaded && logicGamesDocument.gameProgress().playSound)
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
    }

    public void playSoundTap() {
        playSound(soundIDTap);
    }

    public void playSoundSolved() {
        playSound(soundIDSolved);
    }
}
