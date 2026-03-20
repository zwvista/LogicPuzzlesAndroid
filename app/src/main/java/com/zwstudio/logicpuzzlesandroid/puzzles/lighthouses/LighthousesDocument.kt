package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LighthousesDocument(context: Context) : GameDocument<LighthousesGameMove>(context) {
    override fun saveMove(move: LighthousesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        LighthousesGameMove(Position(rec.row, rec.col), LighthousesObject.entries[rec.intValue1])
}