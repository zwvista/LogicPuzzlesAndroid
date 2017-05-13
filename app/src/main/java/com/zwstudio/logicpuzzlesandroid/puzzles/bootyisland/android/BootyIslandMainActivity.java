package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.data.BootyIslandDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class BootyIslandMainActivity extends MainActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState> {
    public BootyIslandDocument doc() {return app.bootyislandDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        BootyIslandOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BootyIslandGameActivity_.intent(this).start();
    }
}
