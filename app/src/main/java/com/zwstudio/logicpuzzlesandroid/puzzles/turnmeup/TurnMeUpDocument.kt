package com.zwstudio.logicpuzzlesandroid.puzzles.turnmeup

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TurnMeUpDocument(context: Context) : GameDocument<TurnMeUpGameMove>(context) {
    override fun saveMove(move: TurnMeUpGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        TurnMeUpGameMove(Position(rec.row, rec.col), rec.intValue1)
}