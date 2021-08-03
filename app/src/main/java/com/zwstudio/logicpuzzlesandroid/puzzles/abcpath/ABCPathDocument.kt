package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ABCPathDocument(context: Context) : GameDocument<ABCPathGameMove>(context) {
    override fun saveMove(move: ABCPathGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        ABCPathGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}
