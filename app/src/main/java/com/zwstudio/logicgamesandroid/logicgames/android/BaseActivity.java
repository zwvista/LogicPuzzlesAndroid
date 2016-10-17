package com.zwstudio.logicgamesandroid.logicgames.android;

/**
 * Created by zwvista on 2016/10/10.
 */

// http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
public class BaseActivity extends RoboAppCompatActivity {
    public GameApplication app() {return (GameApplication)getApplicationContext();}

    @Override
    protected void onStart() {
        super.onStart();
        app().activityStarted();
    }

    @Override
    protected void onStop() {
        app().activityStopped();
        super.onStop();
    }
}
