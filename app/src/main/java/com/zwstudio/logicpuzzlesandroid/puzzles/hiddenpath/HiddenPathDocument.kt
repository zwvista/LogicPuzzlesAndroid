package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenpath

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HiddenPathDocument(context: Context) : GameDocument<HiddenPathGameMove>(context) {
    override fun saveMove(move: HiddenPathGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        HiddenPathGameMove(Position(rec.row, rec.col), rec.intValue1)
}
