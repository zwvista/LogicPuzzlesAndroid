package com.zwstudio.logicpuzzlesandroid.puzzles.domino.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGame
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameMove
import org.androidannotations.annotations.EBean

@EBean
open class DominoDocument : GameDocument<DominoGame?, DominoGameMove?>() {
    protected override fun saveMove(move: DominoGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj!!.ordinal
    }

    override fun loadMove(rec: MoveProgress): DominoGameMove {
        return object : DominoGameMove() {
            init {
                p = Position(rec.row, rec.col)
                dir = rec.intValue1
                obj = GridLineObject.values()[rec.intValue2]
            }
        }
    }
}