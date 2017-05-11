package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.data.MiniLitsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_minilits_main)
public class MiniLitsMainActivity extends MainActivity<MiniLitsGame, MiniLitsDocument, MiniLitsGameMove, MiniLitsGameState> {
    public MiniLitsDocument doc() {return app.minilitsDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        MiniLitsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MiniLitsGameActivity_.intent(this).start();
    }
}
