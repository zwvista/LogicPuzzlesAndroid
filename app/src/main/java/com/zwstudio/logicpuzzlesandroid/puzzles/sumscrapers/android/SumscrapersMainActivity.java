package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.data.SumscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class SumscrapersMainActivity extends GameMainActivity<SumscrapersGame, SumscrapersDocument, SumscrapersGameMove, SumscrapersGameState> {
    public SumscrapersDocument doc() {return app.sumscrapersDocument;}

    @Click
    void btnOptions() {
        SumscrapersOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        SumscrapersGameActivity_.intent(this).start();
    }
}
