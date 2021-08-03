package com.zwstudio.logicpuzzlesandroid.puzzles.parks

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ParksDocument(context: Context) : GameDocument<ParksGameMove>(context) {
    override fun saveMove(move: ParksGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        ParksGameMove(Position(rec.row, rec.col), ParksObject.objFromString(rec.strValue1!!))
}