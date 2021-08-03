package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NumberLinkDocument(context: Context) : GameDocument<NumberLinkGameMove>(context) {
    override fun saveMove(move: NumberLinkGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        NumberLinkGameMove(Position(rec.row, rec.col), rec.intValue1)
}