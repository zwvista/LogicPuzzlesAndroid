package com.zwstudio.logicpuzzlesandroid.puzzles.snail

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnailDocument(context: Context) : GameDocument<SnailGameMove>(context) {
    override fun saveMove(move: SnailGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        SnailGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}