package com.zwstudio.logicpuzzlesandroid.puzzles.rabbits

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RabbitsDocument(context: Context) : GameDocument<RabbitsGameMove>(context) {
    override fun saveMove(move: RabbitsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        RabbitsGameMove(Position(rec.row, rec.col), RabbitsObject.entries[rec.intValue1])
}