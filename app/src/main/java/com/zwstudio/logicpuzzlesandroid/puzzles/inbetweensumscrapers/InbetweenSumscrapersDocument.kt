package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweensumscrapers

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class InbetweenSumscrapersDocument(context: Context) : GameDocument<InbetweenSumscrapersGameMove>(context) {
    override fun saveMove(move: InbetweenSumscrapersGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        InbetweenSumscrapersGameMove(Position(rec.row, rec.col), rec.intValue1)
}