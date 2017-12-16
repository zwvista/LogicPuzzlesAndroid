package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class CalcudokuDocument extends GameDocument<CalcudokuGame, CalcudokuGameMove> {
    protected void saveMove(CalcudokuGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj;
    }
    public CalcudokuGameMove loadMove(MoveProgress rec) {
        return new CalcudokuGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.intValue1;
        }};
    }
}
