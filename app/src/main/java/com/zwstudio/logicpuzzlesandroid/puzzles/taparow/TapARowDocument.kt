package com.zwstudio.logicpuzzlesandroid.puzzles.taparow

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TapARowDocument(context: Context) : GameDocument<TapARowGameMove>(context) {
    override fun saveMove(move: TapARowGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TapARowGameMove(Position(rec.row, rec.col), TapARowObject.objTypeFromString(rec.strValue1!!))
}