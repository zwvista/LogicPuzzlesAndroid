package com.zwstudio.logicpuzzlesandroid.puzzles.lits.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsObject
import org.androidannotations.annotations.EBean

@EBean
class LitsDocument : GameDocument<LitsGame?, LitsGameMove?>() {
    protected override fun saveMove(move: LitsGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.strValue1 = move.obj!!.objAsString()
    }

    override fun loadMove(rec: MoveProgress): LitsGameMove {
        return object : LitsGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = LitsObject.Companion.objFromString(rec.strValue1)
            }
        }
    }
}