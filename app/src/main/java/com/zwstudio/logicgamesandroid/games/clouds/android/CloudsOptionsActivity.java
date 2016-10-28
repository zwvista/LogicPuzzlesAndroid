package com.zwstudio.logicgamesandroid.games.clouds.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.games.clouds.data.CloudsDocument;
import com.zwstudio.logicgamesandroid.games.clouds.data.CloudsGameProgress;
import com.zwstudio.logicgamesandroid.common.android.OptionsActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.activity_clouds_options)
public class CloudsOptionsActivity extends OptionsActivity {
    public CloudsDocument doc() {return app.cloudsDocument;}

    @ViewById
    Spinner spnMarker;

    CloudsGameProgress rec;

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

        spnMarker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                rec.markerOption = position;
                try {
                    doc().app.daoCloudsGameProgress.update(rec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    protected void onDefault() {
        rec.markerOption = 0;
        try {
            doc().app.daoCloudsGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        spnMarker.setSelection(rec.markerOption);
    }
}
