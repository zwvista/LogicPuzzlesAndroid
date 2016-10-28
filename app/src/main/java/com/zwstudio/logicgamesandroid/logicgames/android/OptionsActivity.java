package com.zwstudio.logicgamesandroid.logicgames.android;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity
public abstract class OptionsActivity extends BaseActivity {

    @Click
    protected void btnDone() {
        finish();
    }

    @Click
    protected void btnDefault() {
        // http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        onDefault();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        Builder builder = new Builder(this);
        builder.setMessage("Do you really want to reset the options?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    protected abstract void onDefault();
}
