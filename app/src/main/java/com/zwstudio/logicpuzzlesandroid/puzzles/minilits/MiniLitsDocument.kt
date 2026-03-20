package com.zwstudio.logicpuzzlesandroid.puzzles.minilits

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MiniLitsDocument(context: Context) : GameDocument<MiniLitsGameMove>(context) {
    override fun saveMove(move: MiniLitsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        MiniLitsGameMove(Position(rec.row, rec.col), MiniLitsObject.entries[rec.intValue1])
}