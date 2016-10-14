package com.zwstudio.logicgamesandroid.common;

/**
 * Created by zwvista on 2016/10/10.
 */

public class BaseActivity extends RoboAppCompatActivity {
    public GameApplication app() {return (GameApplication)getApplicationContext();}

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
