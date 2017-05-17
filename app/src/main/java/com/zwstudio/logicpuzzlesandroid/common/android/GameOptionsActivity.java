package com.zwstudio.logicpuzzlesandroid.common.android;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Game;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@EActivity
public abstract class GameOptionsActivity<G extends Game<G, GM, GS>, GD extends GameDocument<G, GM>, GM, GS extends GameState> extends BaseActivity {
    public abstract GD doc();

    @ViewById
    public Spinner spnMarker;
    @ViewById
    public CheckedTextView ctvAllowedObjectsOnly;

    public static List<String> lstMarkers = Arrays.asList("No Marker", "Marker First", "Marker Last");

    @AfterViews
    protected void init() {
        List<String> lst = GameOptionsActivity.lstMarkers;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                String s = lst.get(position);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText(s);
                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                String s = lst.get(position);
                CheckedTextView ctv = (CheckedTextView) v.findViewById(android.R.id.text1);
                ctv.setText(s);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnMarker.setAdapter(adapter);

        spnMarker.setSelection(doc().getMarkerOption());
        ctvAllowedObjectsOnly.setChecked(doc().isAllowedObjectsOnly());
    }

    @ItemSelect
    protected void spnMarkerItemSelected(boolean selected, int position) {
        GameProgress rec = doc().gameProgress();
        doc().setMarkerOption(rec, position);
        try {
            app.daoGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click
    protected void ctvAllowedObjectsOnly() {
        GameProgress rec = doc().gameProgress();
        ctvAllowedObjectsOnly.setChecked(!doc().isAllowedObjectsOnly());
        doc().setAllowedObjectsOnly(rec, !doc().isAllowedObjectsOnly());
        try {
            app.daoGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                {
                    //Yes button clicked
                    GameProgress rec = doc().gameProgress();
                    doc().setMarkerOption(rec, 0);
                    doc().setAllowedObjectsOnly(rec, false);
                    try {
                        app.daoGameProgress.update(rec);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    spnMarker.setSelection(doc().getMarkerOption());
                    ctvAllowedObjectsOnly.setChecked(doc().isAllowedObjectsOnly());
                    break;
                }
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
}
