package com.zwstudio.lightupandroid.android;

import com.zwstudio.lightupandroid.data.GameDocument;

/**
 * Created by zwvista on 2016/10/10.
 */

public class BaseActivity extends RoboAppCompatActivity {
    GameApplication app() {return (GameApplication)getApplicationContext();}
    GameDocument doc() {return app().getGameDocument();}

    @Override
    protected void onResume() {
        super.onResume();
        app().playMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        app().pauseMusic();
    }
}
