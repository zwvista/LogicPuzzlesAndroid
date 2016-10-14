package com.zwstudio.logicgamesandroid.lightup.android;

import com.zwstudio.logicgamesandroid.common.GameApplication;
import com.zwstudio.logicgamesandroid.common.RoboAppCompatActivity;
import com.zwstudio.logicgamesandroid.lightup.data.GameDocument;

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
