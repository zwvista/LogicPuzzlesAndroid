package com.zwstudio.logicpuzzlesandroid.puzzles.tents.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.data.TentsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameState;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class TentsMainActivity extends GameMainActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState> {
    public TentsDocument doc() {return app.tentsDocument;}

    @Click
    void btnOptions() {
        TentsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        TentsGameActivity_.intent(this).start();
    }
}
