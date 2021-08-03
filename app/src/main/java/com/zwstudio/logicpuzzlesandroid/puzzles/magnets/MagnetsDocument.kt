package com.zwstudio.logicpuzzlesandroid.puzzles.magnets

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

open class MagnetsDocument(context: Context) : GameDocument<MagnetsGameMove>(context) {
    override fun saveMove(move: MagnetsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        MagnetsGameMove(Position(rec.row, rec.col), MagnetsObject.values()[rec.intValue1])
}