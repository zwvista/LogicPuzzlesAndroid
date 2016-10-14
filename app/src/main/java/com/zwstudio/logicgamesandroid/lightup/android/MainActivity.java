package com.zwstudio.logicgamesandroid.lightup.android;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zwstudio.lightupandroid.R;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends LightUpActivity {

    @InjectView(R.id.btnResumeGame)
    Button btnResumeGame;
    @InjectView(R.id.btnOptions)
    Button btnOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
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
            int resID = getResources().getIdentifier("btnLevel" + n, "id",
                    "com.zwstudio.lightupandroid");
            Button button = (Button)findViewById(resID);
            button.setOnClickListener(onClickListener);
        }
    }

    private void resumeGame() {
        doc().resumeGame();
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }
}
