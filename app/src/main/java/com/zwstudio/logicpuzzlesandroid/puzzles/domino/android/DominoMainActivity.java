package com.zwstudio.logicpuzzlesandroid.puzzles.domino.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.data.DominoDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class DominoMainActivity extends GameMainActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState> {
    public DominoDocument doc() {return app.dominoDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        DominoOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        DominoGameActivity_.intent(this).start();
    }
}
