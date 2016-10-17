package com.zwstudio.logicgamesandroid.logicgames.android;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zwstudio.logicgamesandroid.R;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_logicgames_main)
public class LogicGamesMainActivity extends LogicGamesActivity {

    @InjectView(R.id.btnResumeGame)
    Button btnResumeGame;
    @InjectView(R.id.btnLightUp)
    Button btnLightUp;
    @InjectView(R.id.btnBridges)
    Button btnBridges;
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
                String gameName = doc().gameProgress().gameName;
                resumeGame(gameName);
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeGame((String) v.getTag());
            }
        };
        btnLightUp.setOnClickListener(onClickListener);
        btnBridges.setOnClickListener(onClickListener);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogicGamesMainActivity.this, LogicGamesOptionsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resumeGame(String gameName) {
        doc().resumeGame(gameName);
        Class<?> cls = null;
        try {
            cls = Class.forName(String.format("com.zwstudio.logicgamesandroid.%s.android.%sMainActivity",
                    gameName.toLowerCase(), gameName));
            Intent intent = new Intent(this, cls);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
