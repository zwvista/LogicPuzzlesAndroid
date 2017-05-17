package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.data.MiniLitsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class MiniLitsMainActivity extends GameMainActivity<MiniLitsGame, MiniLitsDocument, MiniLitsGameMove, MiniLitsGameState> {
    public MiniLitsDocument doc() {return app.minilitsDocument;}

    @Click
    void btnOptions() {
        MiniLitsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MiniLitsGameActivity_.intent(this).start();
    }
}
