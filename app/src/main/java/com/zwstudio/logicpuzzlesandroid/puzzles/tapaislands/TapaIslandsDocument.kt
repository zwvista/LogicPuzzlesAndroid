package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TapaIslandsDocument(context: Context) : GameDocument<TapaIslandsGameMove>(context) {
    override fun saveMove(move: TapaIslandsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TapaIslandsGameMove(Position(rec.row, rec.col), TapaIslandsObject.objTypeFromString(rec.strValue1!!))
}