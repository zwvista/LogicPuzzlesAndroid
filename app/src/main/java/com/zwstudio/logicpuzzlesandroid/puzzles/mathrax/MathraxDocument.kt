package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MathraxDocument(context: Context) : GameDocument<MathraxGameMove>(context) {
    override fun saveMove(move: MathraxGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        MathraxGameMove(Position(rec.row, rec.col), rec.intValue1)
}