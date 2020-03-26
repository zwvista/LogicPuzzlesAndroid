package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGameMove
import org.androidannotations.annotations.EBean

@EBean
open class FillominoDocument : GameDocument<FillominoGame?, FillominoGameMove?>() {
    protected override fun saveMove(move: FillominoGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress): FillominoGameMove {
        return object : FillominoGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = rec.strValue1[0]
            }
        }
    }
}