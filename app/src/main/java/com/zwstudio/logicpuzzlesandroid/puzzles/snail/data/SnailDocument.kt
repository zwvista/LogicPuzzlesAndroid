package com.zwstudio.logicpuzzlesandroid.puzzles.snail.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGame
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameMove
import org.androidannotations.annotations.EBean

@EBean
class SnailDocument : GameDocument<SnailGame, SnailGameMove>() {
    override fun saveMove(move: SnailGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        SnailGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}