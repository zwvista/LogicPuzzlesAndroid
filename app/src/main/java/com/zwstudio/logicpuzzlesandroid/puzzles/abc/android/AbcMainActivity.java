package com.zwstudio.logicpuzzlesandroid.puzzles.abc.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_abc_main)
public class AbcMainActivity extends MainActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState> {
    public AbcDocument doc() {return app.abcDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        AbcOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        AbcGameActivity_.intent(this).start();
    }
}
