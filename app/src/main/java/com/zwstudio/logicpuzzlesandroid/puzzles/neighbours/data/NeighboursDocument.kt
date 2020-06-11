package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGame
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameMove
import org.androidannotations.annotations.EBean

@EBean
class NeighboursDocument : GameDocument<NeighboursGame, NeighboursGameMove>() {
    override fun saveMove(move: NeighboursGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        NeighboursGameMove(Position(rec.row, rec.col), rec.intValue1, GridLineObject.values()[rec.intValue2])
}