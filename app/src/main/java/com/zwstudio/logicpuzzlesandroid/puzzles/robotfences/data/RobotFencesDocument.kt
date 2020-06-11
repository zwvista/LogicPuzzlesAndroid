package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGameMove
import org.androidannotations.annotations.EBean

@EBean
class RobotFencesDocument : GameDocument<RobotFencesGame, RobotFencesGameMove>() {
    override fun saveMove(move: RobotFencesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        RobotFencesGameMove(Position(rec.row, rec.col), rec.intValue1)
}