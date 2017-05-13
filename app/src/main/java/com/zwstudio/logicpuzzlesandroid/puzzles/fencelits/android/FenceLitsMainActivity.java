package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.data.FenceLitsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class FenceLitsMainActivity extends MainActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState> {
    public FenceLitsDocument doc() {return app.fencelitsDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        FenceLitsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        FenceLitsGameActivity_.intent(this).start();
    }
}
