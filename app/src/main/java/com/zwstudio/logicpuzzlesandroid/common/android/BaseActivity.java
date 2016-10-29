package com.zwstudio.logicpuzzlesandroid.common.android;

import android.support.v7.app.AppCompatActivity;

import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;

/**
 * Created by zwvista on 2016/10/10.
 */

// http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
@EActivity
public abstract class BaseActivity extends AppCompatActivity {
    @App
    public LogicPuzzlesApplication app;

    @Override
    protected void onStart() {
        super.onStart();
        app.soundManager.activityStarted();
    }

    @Override
    protected void onStop() {
        app.soundManager.activityStopped();
        super.onStop();
    }
}
