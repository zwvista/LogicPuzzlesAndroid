package com.zwstudio.logicpuzzlesandroid.puzzles.pointing

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PointingDocument(context: Context) : GameDocument<PointingGameMove>(context) {
    override fun saveMove(move: PointingGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
    }

    override fun loadMove(rec: MoveProgress) =
        PointingGameMove(Position(rec.row, rec.col))
}
