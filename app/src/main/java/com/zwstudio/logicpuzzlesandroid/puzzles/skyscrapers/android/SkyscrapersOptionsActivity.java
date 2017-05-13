package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersDocument;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class SkyscrapersOptionsActivity extends OptionsActivity {
    public SkyscrapersDocument doc() {return app.skyscrapersDocument;}

    protected void onDefault() {}
}
