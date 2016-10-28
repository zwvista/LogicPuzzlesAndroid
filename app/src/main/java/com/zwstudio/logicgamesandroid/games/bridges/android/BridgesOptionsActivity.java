package com.zwstudio.logicgamesandroid.games.bridges.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.games.bridges.data.BridgesDocument;
import com.zwstudio.logicgamesandroid.common.android.OptionsActivity;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_bridges_options)
public class BridgesOptionsActivity extends OptionsActivity {
    public BridgesDocument doc() {return app.bridgesDocument;}

    protected void onDefault() {}
}
