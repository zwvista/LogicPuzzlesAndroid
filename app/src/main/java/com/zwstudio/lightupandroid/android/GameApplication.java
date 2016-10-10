package com.zwstudio.lightupandroid.android;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zwstudio.lightupandroid.R;
import com.zwstudio.lightupandroid.data.DBHelper;
import com.zwstudio.lightupandroid.data.GameDocument;

import java.io.IOException;
import java.io.InputStream;

public class GameApplication extends Application {
    private DBHelper dbHelper = null;

    private GameDocument doc;

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

    // http://stackoverflow.com/questions/18459122/play-sound-on-button-click-android
    MediaPlayer mpTap, mpSolved;

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
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

    @Override
    public void onCreate() {
        super.onCreate();
        doc = new GameDocument(getDBHelper());
        InputStream is = null;
        try {
            is = getApplicationContext().getAssets().open("Levels.xml");
            doc.loadXml(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mpTap = MediaPlayer.create(this, R.raw.tap);
        mpSolved = MediaPlayer.create(this, R.raw.solved);

        doBindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        // http://stackoverflow.com/questions/5301891/android-start-service-with-parameter
        music.putExtra("playMusic", doc.gameProgress().playMusic);
        startService(music);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        doUnbindService();
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    public DBHelper getDBHelper() {
        if (dbHelper == null)
            dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        return dbHelper;
    }

    public GameDocument getGameDocument() {
        return doc;
    }

    public void playOrPauseMusic() {
        if (doc.gameProgress().playMusic)
            mServ.resumeMusic();
        else
            mServ.pauseMusic();
    }

    private void playSound(MediaPlayer mp) {
        if (doc.gameProgress().playSound)
            mp.start();
    }

    public void playSoundTap() {
        playSound(mpTap);
    }

    public void playSoundSolved() {
        playSound(mpSolved);
    }
}
