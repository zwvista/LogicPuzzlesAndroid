package com.zwstudio.logicpuzzlesandroid.puzzles.pouringwater

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PouringWaterDocument(context: Context) : GameDocument<PouringWaterGameMove>(context) {
    override fun saveMove(move: PouringWaterGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        PouringWaterGameMove(Position(rec.row, rec.col), PouringWaterObject.entries[rec.intValue1])
}