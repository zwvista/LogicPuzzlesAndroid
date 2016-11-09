package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeGameProgress;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.activity_nurikabe_options)
public class NurikabeOptionsActivity extends OptionsActivity {
    public NurikabeDocument doc() {return app.nurikabeDocument;}

    @ViewById
    Spinner spnMarker;
    @ViewById
    CheckedTextView ctvNormalLightbulbsOnly;

    NurikabeGameProgress rec;

    @AfterViews
    protected void init() {
        List<String> lst = Arrays.asList("No Marker", "Marker After Lightbulb", "Marker Before Lightbulb");
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

        rec = doc().gameProgress();
        spnMarker.setSelection(rec.markerOption);
        ctvNormalLightbulbsOnly.setChecked(rec.normalLightbulbsOnly);
    }

    @ItemSelect
    protected void spnMarkerItemSelected(boolean selected, int position) {
        rec.markerOption = position;
        try {
            app.daoNurikabeGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click
    protected void ctvNormalLightbulbsOnly() {
        ctvNormalLightbulbsOnly.setChecked(!rec.normalLightbulbsOnly);
        rec.normalLightbulbsOnly = !rec.normalLightbulbsOnly;
        try {
            app.daoNurikabeGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void onDefault() {
        rec.markerOption = 0;
        rec.normalLightbulbsOnly = false;
        try {
            app.daoNurikabeGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        spnMarker.setSelection(rec.markerOption);
        ctvNormalLightbulbsOnly.setChecked(rec.normalLightbulbsOnly);
    }
}
