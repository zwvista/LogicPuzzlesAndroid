package com.zwstudio.logicpuzzlesandroid.puzzles.hitori

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HitoriDocument(context: Context) : GameDocument<HitoriGameMove>(context) {
    override fun saveMove(move: HitoriGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        HitoriGameMove(Position(rec.row, rec.col), HitoriObject.values()[rec.intValue1])
}
