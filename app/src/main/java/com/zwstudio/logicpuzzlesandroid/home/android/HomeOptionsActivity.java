package com.zwstudio.logicpuzzlesandroid.home.android;

import android.widget.CheckedTextView;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;
import com.zwstudio.logicpuzzlesandroid.home.data.LogicGamesDocument;
import com.zwstudio.logicpuzzlesandroid.home.data.LogicGamesGameProgress;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;

@EActivity(R.layout.activity_home_options)
public class HomeOptionsActivity extends OptionsActivity {
    public LogicGamesDocument doc() {return app.logicGamesDocument;}

    @ViewById
    CheckedTextView ctvPlayMusic;
    @ViewById
    CheckedTextView ctvPlaySound;

    LogicGamesGameProgress rec;

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
            app.daoLogicGamesGameProgress.update(rec);
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
            app.daoLogicGamesGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void onDefault() {
        rec.playMusic = true;
        rec.playSound = true;
        try {
            app.daoLogicGamesGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ctvPlayMusic.setChecked(rec.playMusic);
        app.soundManager.playOrPauseMusic();
        ctvPlaySound.setChecked(rec.playSound);
    }
}
