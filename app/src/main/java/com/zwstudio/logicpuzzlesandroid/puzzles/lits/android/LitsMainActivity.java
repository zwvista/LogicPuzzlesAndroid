package com.zwstudio.logicpuzzlesandroid.puzzles.lits.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.data.LitsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class LitsMainActivity extends GameMainActivity<LitsGame, LitsDocument, LitsGameMove, LitsGameState> {
    public LitsDocument doc() {return app.litsDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        LitsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        LitsGameActivity_.intent(this).start();
    }
}
