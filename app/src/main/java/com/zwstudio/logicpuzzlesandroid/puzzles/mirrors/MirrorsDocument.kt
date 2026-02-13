package com.zwstudio.logicpuzzlesandroid.puzzles.mirrors

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MirrorsDocument(context: Context) : GameDocument<MirrorsGameMove>(context) {
    override fun saveMove(move: MirrorsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        MirrorsGameMove(Position(rec.row, rec.col), MirrorsObject.entries[rec.intValue1])
}