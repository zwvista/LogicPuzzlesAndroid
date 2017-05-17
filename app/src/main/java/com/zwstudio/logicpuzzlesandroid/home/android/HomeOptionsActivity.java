package com.zwstudio.logicpuzzlesandroid.home.android;

import android.widget.CheckedTextView;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeGameProgress;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;

@EActivity(R.layout.activity_home_options)
public class HomeOptionsActivity extends BaseActivity {
    public HomeDocument doc() {return app.homeDocument;}

    @ViewById
    CheckedTextView ctvPlayMusic;
    @ViewById
    CheckedTextView ctvPlaySound;

    HomeGameProgress rec;

    @AfterViews
    protected void init() {
        rec = doc().gameProgress();
        ctvPlayMusic.setChecked(rec.playMusic);
        ctvPlaySound.setChecked(rec.playSound);
    }

    @Click
    protected void ctvPlayMusic() {
        ctvPlayMusic.setChecked(!rec.playMusic);
        rec.playMusic = !rec.playMusic;
        try {
            app.daoHomeGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        app.soundManager.playOrPauseMusic();
    }

    @Click
    protected void ctvPlaySound() {
        ctvPlaySound.setChecked(!rec.playSound);
        rec.playSound = !rec.playSound;
        try {
            app.daoHomeGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void onDefault() {
        rec.playMusic = true;
        rec.playSound = true;
        try {
            app.daoHomeGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ctvPlayMusic.setChecked(rec.playMusic);
        app.soundManager.playOrPauseMusic();
        ctvPlaySound.setChecked(rec.playSound);
    }
}
