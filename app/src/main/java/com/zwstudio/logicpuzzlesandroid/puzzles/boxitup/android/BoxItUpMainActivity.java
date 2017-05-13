package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.data.BoxItUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class BoxItUpMainActivity extends MainActivity<BoxItUpGame, BoxItUpDocument, BoxItUpGameMove, BoxItUpGameState> {
    public BoxItUpDocument doc() {return app.boxitupDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        BoxItUpOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BoxItUpGameActivity_.intent(this).start();
    }
}
