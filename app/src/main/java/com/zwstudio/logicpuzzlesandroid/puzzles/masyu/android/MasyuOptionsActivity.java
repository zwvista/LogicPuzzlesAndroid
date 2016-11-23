package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuDocument;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_masyu_options)
public class MasyuOptionsActivity extends OptionsActivity {
    public MasyuDocument doc() {return app.masyuDocument;}

    protected void onDefault() {}
}
