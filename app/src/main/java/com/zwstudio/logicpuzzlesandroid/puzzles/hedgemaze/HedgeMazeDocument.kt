package com.zwstudio.logicpuzzlesandroid.puzzles.hedgemaze

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HedgeMazeDocument(context: Context) : GameDocument<HedgeMazeGameMove>(context) {
    override fun saveMove(move: HedgeMazeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        HedgeMazeGameMove(Position(rec.row, rec.col), HedgeMazeObject.entries[rec.intValue1])
}