package com.zwstudio.logicpuzzlesandroid.puzzles.steps

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class StepsDocument(context: Context) : GameDocument<StepsGameMove>(context) {
    override fun saveMove(move: StepsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        StepsGameMove(Position(rec.row, rec.col), rec.intValue1)
}