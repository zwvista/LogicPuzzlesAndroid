package com.zwstudio.logicpuzzlesandroid.puzzles.gems

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GemsDocument(context: Context) : GameDocument<GemsGameMove>(context) {
    override fun saveMove(move: GemsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        GemsGameMove(Position(rec.row, rec.col), rec.intValue1)
}