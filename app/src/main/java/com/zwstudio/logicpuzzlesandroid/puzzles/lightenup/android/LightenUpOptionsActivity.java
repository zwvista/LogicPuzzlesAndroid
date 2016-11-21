package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.activity_lightenup_options)
public class LightenUpOptionsActivity extends OptionsActivity {
    public LightenUpDocument doc() {return app.lightenupDocument;}

    @ViewById
    Spinner spnMarker;
    @ViewById
    CheckedTextView ctvNormalLightbulbsOnly;

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

        spnMarker.setSelection(doc().getMarkerOption());
        ctvNormalLightbulbsOnly.setChecked(doc().isNormalLightbulbsOnly());
    }

    @ItemSelect
    protected void spnMarkerItemSelected(boolean selected, int position) {
        doc().setMarkerOption(position);
        try {
            app.daoGameProgress.update(doc().gameProgress());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click
    protected void ctvNormalLightbulbsOnly() {
        ctvNormalLightbulbsOnly.setChecked(!doc().isNormalLightbulbsOnly());
        doc().setNormalLightbulbsOnly(!doc().isNormalLightbulbsOnly());
        try {
            app.daoGameProgress.update(doc().gameProgress());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void onDefault() {
        doc().setMarkerOption(0);
        doc().setNormalLightbulbsOnly(false);
        try {
            app.daoGameProgress.update(doc().gameProgress());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        spnMarker.setSelection(doc().getMarkerOption());
        ctvNormalLightbulbsOnly.setChecked(doc().isNormalLightbulbsOnly());
    }
}
