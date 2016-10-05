package com.zwstudio.lightupandroid.android;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.lightupandroid.R;
import com.zwstudio.lightupandroid.data.GameDocument;
import com.zwstudio.lightupandroid.data.GameProgress;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_options)
public class OptionsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.spnMarker)
    Spinner spnMarker;
    @InjectView(R.id.ctvNormalLightbulbsOnly)
    CheckedTextView ctvNormalLightbulbsOnly;


    GameDocument doc() {return ((GameApplication)getApplicationContext()).getGameDocument();}
    GameProgress options() {return doc().gameProgress();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        GameProgress rec = options();
        spnMarker.setSelection(rec.markerOption);

        spnMarker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    rec.markerOption = position;
                    doc().db.getDaoGameProgress().update(rec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ctvNormalLightbulbsOnly.setChecked(rec.normalLightbulbsOnly);
    }
}
