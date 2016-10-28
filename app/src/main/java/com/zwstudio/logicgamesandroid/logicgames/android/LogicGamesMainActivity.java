package com.zwstudio.logicgamesandroid.logicgames.android;

import android.content.Intent;
import android.media.AudioManager;
import android.view.View;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.logicgames.data.LogicGamesDocument;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_logicgames_main)
public class LogicGamesMainActivity extends BaseActivity {
    public LogicGamesDocument doc() {return app.logicGamesDocument;}

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
        LogicGamesOptionsActivity_.intent(this).start();
    }

    private void resumeGame(String gameName, boolean toResume) {
        doc().resumeGame(gameName);
        Class<?> cls = null;
        try {
            cls = Class.forName(String.format("com.zwstudio.logicgamesandroid.%s.android.%sMainActivity_",
                    gameName.toLowerCase(), gameName));
            Intent intent = new Intent(this, cls);
            intent.putExtra("toResume", toResume);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
