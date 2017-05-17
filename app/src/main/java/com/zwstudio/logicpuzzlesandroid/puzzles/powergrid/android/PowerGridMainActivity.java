package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.data.PowerGridDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class PowerGridMainActivity extends GameMainActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState> {
    public PowerGridDocument doc() {return app.powergridDocument;}

    @Click
    void btnOptions() {
        PowerGridOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        PowerGridGameActivity_.intent(this).start();
    }
}
