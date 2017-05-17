package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data.NeighboursDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class NeighboursMainActivity extends GameMainActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState> {
    public NeighboursDocument doc() {return app.neighboursDocument;}

    @Click
    void btnOptions() {
        NeighboursOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        NeighboursGameActivity_.intent(this).start();
    }
}
