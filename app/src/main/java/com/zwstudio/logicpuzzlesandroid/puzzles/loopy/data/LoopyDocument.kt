package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGame
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameMove
import org.androidannotations.annotations.EBean

@EBean
class LoopyDocument : GameDocument<LoopyGame?, LoopyGameMove?>() {
    protected override fun saveMove(move: LoopyGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress): LoopyGameMove {
        return object : LoopyGameMove() {
            init {
                p = Position(rec.row, rec.col)
                dir = rec.intValue1
            }
        }
    }
}