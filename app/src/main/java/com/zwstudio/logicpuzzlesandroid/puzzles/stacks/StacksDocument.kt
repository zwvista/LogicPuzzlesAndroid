package com.zwstudio.logicpuzzlesandroid.puzzles.stacks

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class StacksDocument(context: Context) : GameDocument<StacksGameMove>(context) {
    override fun saveMove(move: StacksGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        StacksGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}