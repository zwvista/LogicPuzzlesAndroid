package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.data.PowerGridDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class PowerGridHelpActivity extends GameHelpActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState> {
    public PowerGridDocument doc() {return app.powergridDocument;}

    @AfterViews
    protected void init() {
        super.init();
    }
}
