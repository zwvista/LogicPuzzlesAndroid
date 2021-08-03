package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FourMeNotDocument(context: Context) : GameDocument<FourMeNotGameMove>(context) {
    override fun saveMove(move: FourMeNotGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        FourMeNotGameMove(Position(rec.row, rec.col), FourMeNotObject.objFromString(rec.strValue1!!))
}