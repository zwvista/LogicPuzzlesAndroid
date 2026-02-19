package com.zwstudio.logicpuzzlesandroid.puzzles.suspendedgravity

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SuspendedGravityDocument(context: Context) : GameDocument<SuspendedGravityGameMove>(context) {
    override fun saveMove(move: SuspendedGravityGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        SuspendedGravityGameMove(Position(rec.row, rec.col), SuspendedGravityObject.entries[rec.intValue1])
}