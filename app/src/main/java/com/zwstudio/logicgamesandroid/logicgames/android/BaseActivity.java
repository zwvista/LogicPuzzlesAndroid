package com.zwstudio.logicgamesandroid.logicgames.android;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by zwvista on 2016/10/10.
 */

// http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
public class BaseActivity extends AppCompatActivity {
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
