package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.data.CalcudokuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class CalcudokuHelpActivity extends GameHelpActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState> {
    @Bean
    protected CalcudokuDocument document;
    public CalcudokuDocument doc() {return document;}
}
