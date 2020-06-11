package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsObject
import org.androidannotations.annotations.EBean

@EBean
open class MagnetsDocument : GameDocument<MagnetsGame, MagnetsGameMove>() {
    protected override fun saveMove(move: MagnetsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress): MagnetsGameMove {
        return object : MagnetsGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = MagnetsObject.values()[rec.intValue1]
            }
        }
    }
}