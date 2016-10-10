package com.zwstudio.lightupandroid.android;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zwstudio.lightupandroid.R;
import com.zwstudio.lightupandroid.data.GameProgress;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_options)
public class OptionsActivity extends BaseActivity {

    @InjectView(R.id.spnMarker)
    Spinner spnMarker;
    @InjectView(R.id.ctvNormalLightbulbsOnly)
    CheckedTextView ctvNormalLightbulbsOnly;
    @InjectView(R.id.ctvPlayMusic)
    CheckedTextView ctvPlayMusic;
    @InjectView(R.id.ctvPlaySound)
    CheckedTextView ctvPlaySound;
    @InjectView(R.id.btnDone)
    Button btnDone;
    @InjectView(R.id.btnDefault)
    Button btnDefault;

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

        GameProgress rec = doc().gameProgress();
        spnMarker.setSelection(rec.markerOption);

        spnMarker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                rec.markerOption = position;
                try {
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
        ctvNormalLightbulbsOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctvNormalLightbulbsOnly.setChecked(!rec.normalLightbulbsOnly);
                rec.normalLightbulbsOnly = !rec.normalLightbulbsOnly;
                try {
                    doc().db.getDaoGameProgress().update(rec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        ctvPlayMusic.setChecked(rec.playMusic);
        ctvPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctvPlayMusic.setChecked(!rec.playMusic);
                rec.playMusic = !rec.playMusic;
                try {
                    doc().db.getDaoGameProgress().update(rec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                app().playOrPauseMusic();
            }
        });

        ctvPlaySound.setChecked(rec.playSound);
        ctvPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctvPlaySound.setChecked(!rec.playSound);
                rec.playSound = !rec.playSound;
                try {
                    doc().db.getDaoGameProgress().update(rec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                rec.markerOption = 0;
                                rec.normalLightbulbsOnly = false;
                                rec.playMusic = true;
                                rec.playSound = true;
                                try {
                                    doc().db.getDaoGameProgress().update(rec);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                spnMarker.setSelection(rec.markerOption);
                                ctvNormalLightbulbsOnly.setChecked(rec.normalLightbulbsOnly);
                                ctvPlayMusic.setChecked(rec.playMusic);
                                app().playOrPauseMusic();
                                ctvPlaySound.setChecked(rec.playSound);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                Builder builder = new Builder(OptionsActivity.this);
                builder.setMessage("Do you really want to reset the options?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }
}
