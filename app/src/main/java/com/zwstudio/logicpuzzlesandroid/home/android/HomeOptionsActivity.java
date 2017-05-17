package com.zwstudio.logicpuzzlesandroid.home.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    @Click
    protected void btnDone() {
        finish();
    }

    @Click
    protected void btnDefault() {
        // http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
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
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to reset the options?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
