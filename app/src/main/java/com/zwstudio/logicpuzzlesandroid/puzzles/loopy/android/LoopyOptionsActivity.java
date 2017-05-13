package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data.LoopyDocument;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class LoopyOptionsActivity extends OptionsActivity {
    public LoopyDocument doc() {return app.loopyDocument;}

    protected void onDefault() {}
}
