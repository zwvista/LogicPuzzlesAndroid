package com.zwstudio.logicgamesandroid.logicgames.android;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckedTextView;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.logicgames.data.LogicGamesGameProgress;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;

@EActivity(R.layout.activity_logicgames_options)
public class LogicGamesOptionsActivity extends LogicGamesActivity {

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
    protected void ctvPlayMusic(View v) {
        ctvPlayMusic.setChecked(!rec.playMusic);
        rec.playMusic = !rec.playMusic;
        try {
            doc().db.getDaoLogicGamesGameProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        app().playOrPauseMusic();
    }

    @Click
    protected void ctvPlaySound(View v) {
        ctvPlaySound.setChecked(!rec.playSound);
        rec.playSound = !rec.playSound;
        try {
            doc().db.getDaoLogicGamesGameProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click
    protected void btnDone(View v) {
        finish();
    }

    @Click
    protected void btnDefault(View v) {
        // http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        rec.playMusic = true;
                        rec.playSound = true;
                        try {
                            doc().db.getDaoLogicGamesGameProgress().update(rec);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        ctvPlayMusic.setChecked(rec.playMusic);
                        app().playOrPauseMusic();
                        ctvPlaySound.setChecked(rec.playSound);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        Builder builder = new Builder(LogicGamesOptionsActivity.this);
        builder.setMessage("Do you really want to reset the options?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
