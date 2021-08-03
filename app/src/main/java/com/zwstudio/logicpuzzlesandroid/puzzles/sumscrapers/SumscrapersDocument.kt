package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SumscrapersDocument(context: Context) : GameDocument<SumscrapersGameMove>(context) {
    override fun saveMove(move: SumscrapersGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        SumscrapersGameMove(Position(rec.row, rec.col), rec.intValue1)
}