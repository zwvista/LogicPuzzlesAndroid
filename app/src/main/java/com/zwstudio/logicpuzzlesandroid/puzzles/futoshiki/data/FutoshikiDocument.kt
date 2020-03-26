package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameMove
import org.androidannotations.annotations.EBean

@EBean
open class FutoshikiDocument : GameDocument<FutoshikiGame?, FutoshikiGameMove?>() {
    protected override fun saveMove(move: FutoshikiGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress): FutoshikiGameMove {
        return object : FutoshikiGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = rec.strValue1[0]
            }
        }
    }
}