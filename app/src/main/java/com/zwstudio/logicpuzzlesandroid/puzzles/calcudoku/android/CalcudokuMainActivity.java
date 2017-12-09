package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.data.CalcudokuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class CalcudokuMainActivity extends GameMainActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState> {
    @Bean
    protected CalcudokuDocument document;
    public CalcudokuDocument doc() {return document;}

    @Click
    void btnOptions() {
        CalcudokuOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        CalcudokuGameActivity_.intent(this).start();
    }
}
