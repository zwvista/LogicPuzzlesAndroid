package com.zwstudio.logicpuzzlesandroid.puzzles.fussywaiter

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FussyWaiterDocument(context: Context) : GameDocument<FussyWaiterGameMove>(context) {
    override fun saveMove(move: FussyWaiterGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        FussyWaiterGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}