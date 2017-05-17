package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class LineSweeperMainActivity extends GameMainActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState> {
    public LineSweeperDocument doc() {return app.linesweeperDocument;}

    @Click
    void btnOptions() {
        LineSweeperOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        LineSweeperGameActivity_.intent(this).start();
    }
}
