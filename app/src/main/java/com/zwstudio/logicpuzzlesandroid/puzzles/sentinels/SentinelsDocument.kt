package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SentinelsDocument(context: Context) : GameDocument<SentinelsGameMove>(context) {
    override fun saveMove(move: SentinelsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        SentinelsGameMove(Position(rec.row, rec.col), SentinelsObject.objFromString(rec.strValue1!!))
}