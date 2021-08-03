package com.zwstudio.logicpuzzlesandroid.puzzles.pata

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PataDocument(context: Context) : GameDocument<PataGameMove>(context) {
    override fun saveMove(move: PataGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        PataGameMove(Position(rec.row, rec.col), PataObject.objTypeFromString(rec.strValue1!!))
}