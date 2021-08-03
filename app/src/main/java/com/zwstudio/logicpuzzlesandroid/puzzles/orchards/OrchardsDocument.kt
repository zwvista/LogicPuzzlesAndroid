package com.zwstudio.logicpuzzlesandroid.puzzles.orchards

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class OrchardsDocument(context: Context) : GameDocument<OrchardsGameMove>(context) {
    override fun saveMove(move: OrchardsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        OrchardsGameMove(Position(rec.row, rec.col), OrchardsObject.objFromString(rec.strValue1!!))
}