package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BWTapaDocument(context: Context) : GameDocument<BWTapaGameMove>(context) {
    override fun saveMove(move: BWTapaGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BWTapaGameMove(Position(rec.row, rec.col), BWTapaObject.objTypeFromString(rec.strValue1!!))
}
