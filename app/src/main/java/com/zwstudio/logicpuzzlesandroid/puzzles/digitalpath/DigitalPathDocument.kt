package com.zwstudio.logicpuzzlesandroid.puzzles.digitalpath

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DigitalPathDocument(context: Context) : GameDocument<DigitalPathGameMove>(context) {
    override fun saveMove(move: DigitalPathGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        DigitalPathGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}