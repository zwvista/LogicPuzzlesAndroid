package com.zwstudio.lightupandroid.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zwstudio.lightupandroid.R;
import com.zwstudio.lightupandroid.data.GameDocument;
import com.zwstudio.lightupandroid.data.GameProgress;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameDocument doc = ((GameApplication)getApplicationContext()).getGameDocument();
        InputStream in_s = null;
        try {
            in_s = getApplicationContext().getAssets().open("Levels.xml");
            doc.loadXml(in_s);
            GameProgress rec = doc.gameProgeress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
