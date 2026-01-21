package com.zwstudio.logicpuzzlesandroid.puzzles.pleasecomeback

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PleaseComeBackDocument(context: Context) : GameDocument<PleaseComeBackGameMove>(context) {
    override fun saveMove(move: PleaseComeBackGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        PleaseComeBackGameMove(Position(rec.row, rec.col), rec.intValue1)
}