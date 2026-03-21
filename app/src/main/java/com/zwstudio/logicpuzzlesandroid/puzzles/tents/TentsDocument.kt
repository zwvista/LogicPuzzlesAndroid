package com.zwstudio.logicpuzzlesandroid.puzzles.tents

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TentsDocument(context: Context) : GameDocument<TentsGameMove>(context) {
    override fun saveMove(move: TentsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        TentsGameMove(Position(rec.row, rec.col), TentsObject.entries[rec.intValue1])
}