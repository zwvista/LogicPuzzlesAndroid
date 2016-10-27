package com.zwstudio.logicgamesandroid.bridges.android;

import android.content.Intent;
import android.media.AudioManager;
import android.view.View;
import android.widget.Button;

import com.zwstudio.logicgamesandroid.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_bridges_main)
public class BridgesMainActivity extends BridgesActivity {

    @ViewById
    Button btnResumeGame;
    @ViewById
    Button btnOptions;

    @AfterViews
    protected void init() {

        // http://www.vogella.com/tutorials/AndroidMedia/article.html#tutorial_soundpool
        // Set the hardware buttons to control the music
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        btnResumeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeGame();
            }
        });
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BridgesMainActivity.this, BridgesOptionsActivity.class);
                startActivity(intent);
            }
        });

        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String levelID = ((Button)v).getText().toString();
                doc().selectedLevelID = levelID;
                resumeGame();
            }
        };
        // http://stackoverflow.com/questions/25905086/multiple-buttons-onclicklistener-android
        for(int n : levels) {
            int resID = getResources().getIdentifier("btnLevel" + n, "id", "com.zwstudio.logicgamesandroid");
            Button button = (Button)findViewById(resID);
            button.setOnClickListener(onClickListener);
        }

        boolean toResume = getIntent().getBooleanExtra("toResume", false);
        if (toResume) resumeGame();
    }

    private void resumeGame() {
        doc().resumeGame();
        Intent intent = new Intent(BridgesMainActivity.this, BridgesGameActivity.class);
        startActivity(intent);
    }
}
