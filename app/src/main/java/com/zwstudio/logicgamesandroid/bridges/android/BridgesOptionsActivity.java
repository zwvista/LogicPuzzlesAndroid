package com.zwstudio.logicgamesandroid.bridges.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.bridges.data.BridgesDocument;
import com.zwstudio.logicgamesandroid.common.android.OptionsActivity;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_bridges_options)
public class BridgesOptionsActivity extends OptionsActivity {
    public BridgesDocument doc() {return app.bridgesDocument;}

    protected void onDefault() {}
}
