package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadsx

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrossroadsXDocument(context: Context) : GameDocument<CrossroadsXGameMove>(context) {
    override fun saveMove(move: CrossroadsXGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        CrossroadsXGameMove(Position(rec.row, rec.col), rec.intValue1)
}