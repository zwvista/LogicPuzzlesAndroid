package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.data.SumscrapersDocument;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class SumscrapersOptionsActivity extends OptionsActivity {
    public SumscrapersDocument doc() {return app.sumscrapersDocument;}

    protected void onDefault() {}
}
