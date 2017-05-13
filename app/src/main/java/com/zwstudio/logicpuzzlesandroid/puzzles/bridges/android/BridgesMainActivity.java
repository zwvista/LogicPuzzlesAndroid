package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameState;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
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
