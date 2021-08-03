package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NoughtsAndCrossesDocument(context: Context) : GameDocument<NoughtsAndCrossesGameMove>(context) {
    override fun saveMove(move: NoughtsAndCrossesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        NoughtsAndCrossesGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}