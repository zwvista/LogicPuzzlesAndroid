package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NumberPathDocument(context: Context) : GameDocument<NumberPathGameMove>(context) {
    override fun saveMove(move: NumberPathGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        NumberPathGameMove(Position(rec.row, rec.col), rec.intValue1)
}