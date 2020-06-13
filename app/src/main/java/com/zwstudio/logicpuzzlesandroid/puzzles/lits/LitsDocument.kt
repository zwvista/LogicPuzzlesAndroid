package com.zwstudio.logicpuzzlesandroid.puzzles.lits

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class LitsDocument : GameDocument<LitsGame, LitsGameMove>() {
    override fun saveMove(move: LitsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        LitsGameMove(Position(rec.row, rec.col), LitsObject.objFromString(rec.strValue1!!))
}