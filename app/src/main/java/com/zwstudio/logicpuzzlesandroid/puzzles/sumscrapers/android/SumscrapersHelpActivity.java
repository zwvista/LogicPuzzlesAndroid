package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.data.SumscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class SumscrapersHelpActivity extends GameHelpActivity<SumscrapersGame, SumscrapersDocument, SumscrapersGameMove, SumscrapersGameState> {
    public SumscrapersDocument doc() {return app.sumscrapersDocument;}

    @AfterViews
    protected void init() {
        super.init();
    }
}
