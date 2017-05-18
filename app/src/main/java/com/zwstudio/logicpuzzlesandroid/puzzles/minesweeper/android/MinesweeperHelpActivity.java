package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.data.MinesweeperDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class MinesweeperHelpActivity extends GameHelpActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState> {
    @Bean
    protected MinesweeperDocument document;
    public MinesweeperDocument doc() {return document;}
}
