package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesDocument;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class BridgesOptionsActivity extends OptionsActivity {
    public BridgesDocument doc() {return app.bridgesDocument;}

    protected void onDefault() {}
}
