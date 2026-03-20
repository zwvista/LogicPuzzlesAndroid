package com.zwstudio.logicpuzzlesandroid.puzzles.liarliar

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LiarLiarDocument(context: Context) : GameDocument<LiarLiarGameMove>(context) {
    override fun saveMove(move: LiarLiarGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        LiarLiarGameMove(Position(rec.row, rec.col), LiarLiarObject.entries[rec.intValue1])
}