package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TierraDelFuegoDocument(context: Context) : GameDocument<TierraDelFuegoGameMove>(context) {
    override fun saveMove(move: TierraDelFuegoGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TierraDelFuegoGameMove(Position(rec.row, rec.col), TierraDelFuegoObject.objFromString(rec.strValue1!!))
}