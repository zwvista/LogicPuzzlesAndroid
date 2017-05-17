package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data.LoopyDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class LoopyMainActivity extends GameMainActivity<LoopyGame, LoopyDocument, LoopyGameMove, LoopyGameState> {
    public LoopyDocument doc() {return app.loopyDocument;}

    @Click
    void btnOptions() {
        LoopyOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        LoopyGameActivity_.intent(this).start();
    }
}
