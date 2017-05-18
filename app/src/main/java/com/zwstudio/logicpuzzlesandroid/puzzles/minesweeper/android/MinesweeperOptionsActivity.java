package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.data.MinesweeperDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class MinesweeperOptionsActivity extends GameOptionsActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState> {
    @Bean
    protected MinesweeperDocument document;
    public MinesweeperDocument doc() {return document;}
}
