package com.zwstudio.logicpuzzlesandroid.common.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;

import fj.function.Effect0;

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

    protected void yesNoDialog(CharSequence message, Effect0 yesAction) {
        // http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        yesAction.f();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
