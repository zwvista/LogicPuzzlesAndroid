package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameMove
import org.androidannotations.annotations.EBean

@EBean
open class CalcudokuDocument : GameDocument<CalcudokuGame?, CalcudokuGameMove?>() {
    protected override fun saveMove(move: CalcudokuGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress): CalcudokuGameMove {
        return object : CalcudokuGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = rec.intValue1
            }
        }
    }
}