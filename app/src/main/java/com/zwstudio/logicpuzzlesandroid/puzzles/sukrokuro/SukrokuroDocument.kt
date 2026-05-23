package com.zwstudio.logicpuzzlesandroid.puzzles.sukrokuro

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SukrokuroDocument(context: Context) : GameDocument<SukrokuroGameMove>(context) {
    override fun saveMove(move: SukrokuroGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        SukrokuroGameMove(Position(rec.row, rec.col), rec.intValue1)
}