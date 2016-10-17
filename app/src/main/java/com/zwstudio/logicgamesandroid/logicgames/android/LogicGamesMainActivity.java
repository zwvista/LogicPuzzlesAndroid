package com.zwstudio.logicgamesandroid.logicgames.android;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zwstudio.lightupandroid.R;
import com.zwstudio.logicgamesandroid.lightup.android.LightUpGameActivity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_logicgames_main)
public class LogicGamesMainActivity extends LogicGamesActivity {

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
                Intent intent = new Intent(LogicGamesMainActivity.this, LogicGamesOptionsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resumeGame() {
        doc().resumeGame();
        Intent intent = new Intent(LogicGamesMainActivity.this, LightUpGameActivity.class);
        startActivity(intent);
    }
}
