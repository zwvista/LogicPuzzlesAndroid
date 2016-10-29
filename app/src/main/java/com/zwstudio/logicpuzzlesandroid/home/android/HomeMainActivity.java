package com.zwstudio.logicpuzzlesandroid.home.android;

import android.content.Intent;
import android.media.AudioManager;
import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_home_main)
public class HomeMainActivity extends BaseActivity {
    public HomeDocument doc() {return app.logicGamesDocument;}

    @AfterViews
    protected void init() {
        // http://www.vogella.com/tutorials/AndroidMedia/article.html#tutorial_soundpool
        // Set the hardware buttons to control the music
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Click
    protected void btnResumeGame() {
        String gameName = doc().gameProgress().gameName;
        resumeGame(gameName, true);
    }

    @Click({R.id.btnLightUp, R.id.btnBridges, R.id.btnSlitherLink, R.id.btnClouds, R.id.btnHitori})
    protected void btnStartGame(View v) {
        resumeGame((String) v.getTag(), true);
    }

    @Click
    protected void btnOptions() {
        HomeOptionsActivity_.intent(this).start();
    }

    private void resumeGame(String gameName, boolean toResume) {
        doc().resumeGame(gameName);
        Class<?> cls = null;
        try {
            cls = Class.forName(String.format("com.zwstudio.logicpuzzlesandroid.games.%s.android.%sMainActivity_",
                    gameName.toLowerCase(), gameName));
            Intent intent = new Intent(this, cls);
            intent.putExtra("toResume", toResume);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
