package com.zwstudio.logicpuzzlesandroid.puzzles.turntwice

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TurnTwiceDocument(context: Context) : GameDocument<TurnTwiceGameMove>(context) {
    override fun saveMove(move: TurnTwiceGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TurnTwiceGameMove(Position(rec.row, rec.col), TurnTwiceObject.objFromString(rec.strValue1!!))
}