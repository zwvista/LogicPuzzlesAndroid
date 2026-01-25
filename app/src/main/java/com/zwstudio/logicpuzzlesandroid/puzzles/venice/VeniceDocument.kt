package com.zwstudio.logicpuzzlesandroid.puzzles.venice

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class VeniceDocument(context: Context) : GameDocument<VeniceGameMove>(context) {
    override fun saveMove(move: VeniceGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        VeniceGameMove(Position(rec.row, rec.col), VeniceObject.entries[rec.intValue1])
}