package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RobotFencesDocument(context: Context) : GameDocument<RobotFencesGameMove>(context) {
    override fun saveMove(move: RobotFencesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        RobotFencesGameMove(Position(rec.row, rec.col), rec.intValue1)
}