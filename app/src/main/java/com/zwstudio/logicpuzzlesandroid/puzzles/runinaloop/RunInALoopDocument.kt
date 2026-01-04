package com.zwstudio.logicpuzzlesandroid.puzzles.runinaloop

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RunInALoopDocument(context: Context) : GameDocument<RunInALoopGameMove>(context) {
    override fun saveMove(move: RunInALoopGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        RunInALoopGameMove(Position(rec.row, rec.col), rec.intValue1)
}