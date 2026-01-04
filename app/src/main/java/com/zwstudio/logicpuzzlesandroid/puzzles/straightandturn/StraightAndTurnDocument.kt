package com.zwstudio.logicpuzzlesandroid.puzzles.straightandturn

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class StraightAndTurnDocument(context: Context) : GameDocument<StraightAndTurnGameMove>(context) {
    override fun saveMove(move: StraightAndTurnGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        StraightAndTurnGameMove(Position(rec.row, rec.col), rec.intValue1)
}