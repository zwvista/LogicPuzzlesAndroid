package com.zwstudio.logicpuzzlesandroid.puzzles.cleaningpath

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CleaningPathDocument(context: Context) : GameDocument<CleaningPathGameMove>(context) {
    override fun saveMove(move: CleaningPathGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        CleaningPathGameMove(Position(rec.row, rec.col), rec.intValue1)
}