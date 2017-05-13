package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.data.FenceItUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class FenceItUpMainActivity extends GameMainActivity<FenceItUpGame, FenceItUpDocument, FenceItUpGameMove, FenceItUpGameState> {
    public FenceItUpDocument doc() {return app.fenceitupDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        FenceItUpOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        FenceItUpGameActivity_.intent(this).start();
    }
}
