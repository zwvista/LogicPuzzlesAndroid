package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameMove
import org.androidannotations.annotations.EBean

@EBean
open class BridgesDocument : GameDocument<BridgesGame?, BridgesGameMove?>() {
    protected override fun saveMove(move: BridgesGameMove, rec: MoveProgress) {
        rec.row = move.pFrom!!.row
        rec.col = move.pFrom!!.col
        rec.row2 = move.pTo!!.row
        rec.col2 = move.pTo!!.col
    }

    override fun loadMove(rec: MoveProgress): BridgesGameMove {
        return object : BridgesGameMove() {
            init {
                pFrom = Position(rec.row, rec.col)
                pTo = Position(rec.row2, rec.col2)
            }
        }
    }
}