package com.zwstudio.lightupandroid.android;

import com.zwstudio.lightupandroid.data.GameDocument;

/**
 * Created by zwvista on 2016/10/10.
 */

public class BaseActivity extends RoboAppCompatActivity {
    GameApplication app() {return (GameApplication)getApplicationContext();}
    GameDocument doc() {return app().getGameDocument();}
}
