package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.data.FenceItUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class FenceItUpHelpActivity extends GameHelpActivity<FenceItUpGame, FenceItUpDocument, FenceItUpGameMove, FenceItUpGameState> {
    public FenceItUpDocument doc() {return app.fenceitupDocument;}

    @AfterViews
    protected void init() {
        super.init();
    }
}
