package com.zwstudio.logicpuzzlesandroid.puzzles.arrows

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ArrowsDocument(context: Context) : GameDocument<ArrowsGameMove>(context) {
    override fun saveMove(move: ArrowsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        ArrowsGameMove(Position(rec.row, rec.col), rec.intValue1)
}
