package com.zwstudio.logicpuzzlesandroid.puzzles.scissors

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ScissorsDocument(context: Context) : GameDocument<ScissorsGameMove>(context) {
    override fun saveMove(move: ScissorsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        ScissorsGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}
