package com.zwstudio.logicpuzzlesandroid.puzzles.lits

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LitsDocument(context: Context) : GameDocument<LitsGameMove>(context) {
    override fun saveMove(move: LitsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        LitsGameMove(Position(rec.row, rec.col), LitsObject.objFromString(rec.strValue1!!))
}