package com.zwstudio.logicgamesandroid.logicgames.android;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.zwstudio.lightupandroid.R;
import com.zwstudio.logicgamesandroid.logicgames.data.LogicGamesGameProgress;

import java.sql.SQLException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_logicgames_options)
public class LogicGamesOptionsActivity extends LogicGamesActivity {

    @InjectView(R.id.ctvPlayMusic)
    CheckedTextView ctvPlayMusic;
    @InjectView(R.id.ctvPlaySound)
    CheckedTextView ctvPlaySound;
    @InjectView(R.id.btnDone)
    Button btnDone;
    @InjectView(R.id.btnDefault)
    Button btnDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogicGamesGameProgress rec = doc().gameProgress();
        ctvPlayMusic.setChecked(rec.playMusic);
        ctvPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctvPlayMusic.setChecked(!rec.playMusic);
                rec.playMusic = !rec.playMusic;
                try {
                    doc().db.getDaoLogicGamesGameProgress().update(rec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                app().playOrPauseMusic();
            }
        });

        ctvPlaySound.setChecked(rec.playSound);
        ctvPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctvPlaySound.setChecked(!rec.playSound);
                rec.playSound = !rec.playSound;
                try {
                    doc().db.getDaoLogicGamesGameProgress().update(rec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }
}
