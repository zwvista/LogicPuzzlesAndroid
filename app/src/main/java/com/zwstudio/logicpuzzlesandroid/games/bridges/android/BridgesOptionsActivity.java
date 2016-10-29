package com.zwstudio.logicpuzzlesandroid.games.bridges.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.games.bridges.data.BridgesDocument;
import com.zwstudio.logicpuzzlesandroid.common.android.OptionsActivity;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_bridges_options)
public class BridgesOptionsActivity extends OptionsActivity {
    public BridgesDocument doc() {return app.bridgesDocument;}

    protected void onDefault() {}
}
