package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.data.MinesweeperDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class MinesweeperMainActivity extends GameMainActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState> {
    @Bean
    protected MinesweeperDocument document;
    public MinesweeperDocument doc() {return document;}

    @Click
    void btnOptions() {
        MinesweeperOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MinesweeperGameActivity_.intent(this).start();
    }
}
