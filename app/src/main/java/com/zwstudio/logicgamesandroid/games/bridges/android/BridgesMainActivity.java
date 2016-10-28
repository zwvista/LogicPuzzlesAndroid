package com.zwstudio.logicgamesandroid.games.bridges.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.games.bridges.data.BridgesDocument;
import com.zwstudio.logicgamesandroid.games.bridges.domain.BridgesGame;
import com.zwstudio.logicgamesandroid.games.bridges.domain.BridgesGameMove;
import com.zwstudio.logicgamesandroid.games.bridges.domain.BridgesGameState;
import com.zwstudio.logicgamesandroid.common.android.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_bridges_main)
public class BridgesMainActivity extends MainActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState> {
    public BridgesDocument doc() {return app.bridgesDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        BridgesOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BridgesGameActivity_.intent(this).start();
    }
}
