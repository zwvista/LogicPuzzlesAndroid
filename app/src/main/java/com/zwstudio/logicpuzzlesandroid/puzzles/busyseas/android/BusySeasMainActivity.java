package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.data.BusySeasDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class BusySeasMainActivity extends MainActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState> {
    public BusySeasDocument doc() {return app.busyseasDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        BusySeasOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BusySeasGameActivity_.intent(this).start();
    }
}
