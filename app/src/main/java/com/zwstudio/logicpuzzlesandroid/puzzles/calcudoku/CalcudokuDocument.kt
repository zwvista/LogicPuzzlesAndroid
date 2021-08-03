package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CalcudokuDocument(context: Context) : GameDocument<CalcudokuGameMove>(context) {
    override fun saveMove(move: CalcudokuGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        CalcudokuGameMove(Position(rec.row, rec.col), rec.intValue1)
}