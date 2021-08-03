package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CastleBaileyDocument(context: Context) : GameDocument<CastleBaileyGameMove>(context) {
    override fun saveMove(move: CastleBaileyGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        CastleBaileyGameMove(Position(rec.row, rec.col), CastleBaileyObject.values()[rec.intValue1])
}
