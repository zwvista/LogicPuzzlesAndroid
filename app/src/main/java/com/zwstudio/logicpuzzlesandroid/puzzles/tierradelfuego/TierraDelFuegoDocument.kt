package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TierraDelFuegoDocument(context: Context) : GameDocument<TierraDelFuegoGameMove>(context) {
    override fun saveMove(move: TierraDelFuegoGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        TierraDelFuegoGameMove(Position(rec.row, rec.col), TierraDelFuegoObject.entries[rec.intValue1])
}