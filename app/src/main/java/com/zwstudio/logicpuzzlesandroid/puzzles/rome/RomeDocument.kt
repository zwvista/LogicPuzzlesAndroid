package com.zwstudio.logicpuzzlesandroid.puzzles.rome

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RomeDocument(context: Context) : GameDocument<RomeGameMove>(context) {
    override fun saveMove(move: RomeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        RomeGameMove(Position(rec.row, rec.col), RomeObject.entries[rec.intValue1])
}