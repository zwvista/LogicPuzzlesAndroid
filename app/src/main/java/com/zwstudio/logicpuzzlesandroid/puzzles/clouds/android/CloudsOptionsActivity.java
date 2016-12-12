package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsDocument;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.List;

@EActivity(R.layout.activity_clouds_options)
public class CloudsOptionsActivity extends OptionsActivity {
    public CloudsDocument doc() {return app.cloudsDocument;}

    @ViewById
    Spinner spnMarker;

    @AfterViews
    protected void init() {
        List<String> lst = OptionsActivity.lstMarkers;
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

    protected void onDefault() {
        doc().setMarkerOption(0);
        try {
            app.daoGameProgress.update(doc().gameProgress());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        spnMarker.setSelection(doc().getMarkerOption());
    }
}
