package com.zwstudio.logicpuzzlesandroid.puzzles.tapa

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TapaDocument(context: Context) : GameDocument<TapaGameMove>(context) {
    override fun saveMove(move: TapaGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TapaGameMove(Position(rec.row, rec.col), TapaObject.objTypeFromString(rec.strValue1!!))
}