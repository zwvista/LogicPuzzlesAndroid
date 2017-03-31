package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.data.PowerGridDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_powergrid_main)
public class PowerGridMainActivity extends MainActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState> {
    public PowerGridDocument doc() {return app.powergridDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        PowerGridOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        PowerGridGameActivity_.intent(this).start();
    }
}
