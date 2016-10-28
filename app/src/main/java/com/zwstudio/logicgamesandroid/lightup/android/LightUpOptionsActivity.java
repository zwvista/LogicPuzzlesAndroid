package com.zwstudio.logicgamesandroid.lightup.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.lightup.data.LightUpDocument;
import com.zwstudio.logicgamesandroid.lightup.data.LightUpGameProgress;
import com.zwstudio.logicgamesandroid.logicgames.android.OptionsActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.activity_lightup_options)
public class LightUpOptionsActivity extends OptionsActivity {
    public LightUpDocument doc() {return app.lightUpDocument;}

    @ViewById
    Spinner spnMarker;
    @ViewById
    CheckedTextView ctvNormalLightbulbsOnly;

    LightUpGameProgress rec;

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
                    doc().app.daoLightUpGameProgress.update(rec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ctvNormalLightbulbsOnly.setChecked(rec.normalLightbulbsOnly);
        ctvNormalLightbulbsOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctvNormalLightbulbsOnly.setChecked(!rec.normalLightbulbsOnly);
                rec.normalLightbulbsOnly = !rec.normalLightbulbsOnly;
                try {
                    doc().app.daoLightUpGameProgress.update(rec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void onDefault() {
        rec.markerOption = 0;
        rec.normalLightbulbsOnly = false;
        try {
            doc().app.daoLightUpGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        spnMarker.setSelection(rec.markerOption);
        ctvNormalLightbulbsOnly.setChecked(rec.normalLightbulbsOnly);
    }
}
