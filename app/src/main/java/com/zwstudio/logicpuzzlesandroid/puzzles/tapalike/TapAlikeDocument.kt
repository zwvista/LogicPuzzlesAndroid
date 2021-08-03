package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TapAlikeDocument(context: Context) : GameDocument<TapAlikeGameMove>(context) {
    override fun saveMove(move: TapAlikeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TapAlikeGameMove(Position(rec.row, rec.col), TapAlikeObject.objTypeFromString(rec.strValue1!!))
}