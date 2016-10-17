package com.zwstudio.logicgamesandroid.logicgames.android;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zwstudio.lightupandroid.R;
import com.zwstudio.logicgamesandroid.bridges.android.BridgesMainActivity;
import com.zwstudio.logicgamesandroid.lightup.android.LightUpMainActivity;
import com.zwstudio.logicgamesandroid.logicgames.data.LogicGamesDocument;

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
                String name = doc().gameProgress().gameName;
                if (name.equals(LogicGamesDocument.getGameName(BridgesMainActivity.class)))
                    resumeGame(BridgesMainActivity.class);
                else
                    resumeGame(LightUpMainActivity.class);
            }
        });
        btnLightUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeGame(LightUpMainActivity.class);
            }
        });
        btnBridges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeGame(BridgesMainActivity.class);
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

    private void resumeGame(Class<?> cls) {
        doc().resumeGame(cls);
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
