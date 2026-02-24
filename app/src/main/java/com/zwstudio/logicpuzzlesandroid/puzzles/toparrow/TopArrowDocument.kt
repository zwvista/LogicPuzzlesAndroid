package com.zwstudio.logicpuzzlesandroid.puzzles.toparrow

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TopArrowDocument(context: Context) : GameDocument<TopArrowGameMove>(context) {
    override fun saveMove(move: TopArrowGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        TopArrowGameMove(Position(rec.row, rec.col), rec.intValue1)
}