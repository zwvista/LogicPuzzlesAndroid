package com.zwstudio.logicpuzzlesandroid.common.android;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.widget.CheckedTextView;
import android.widget.Spinner;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

@EActivity
public abstract class GameOptionsActivity extends BaseActivity {

    @ViewById
    public Spinner spnMarker;
    @ViewById
    public CheckedTextView ctvAllowedObjectsOnly;

    public static List<String> lstMarkers = Arrays.asList("No Marker", "Marker First", "Marker Last");

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
