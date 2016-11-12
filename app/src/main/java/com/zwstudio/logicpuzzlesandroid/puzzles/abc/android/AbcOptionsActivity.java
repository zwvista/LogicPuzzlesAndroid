package com.zwstudio.logicpuzzlesandroid.puzzles.abc.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_abc_options)
public class AbcOptionsActivity extends OptionsActivity {
    public AbcDocument doc() {return app.abcDocument;}

    protected void onDefault() {}
}
