package com.zwstudio.logicpuzzlesandroid.puzzles.unreliablehints

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class UnreliableHintsDocument(context: Context) : GameDocument<UnreliableHintsGameMove>(context) {
    override fun saveMove(move: UnreliableHintsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        UnreliableHintsGameMove(Position(rec.row, rec.col), UnreliableHintsObject.entries[rec.intValue1])
}
