package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class TapDifferentlyDocument : GameDocument<TapDifferentlyGameMove>() {
    override fun saveMove(move: TapDifferentlyGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TapDifferentlyGameMove(Position(rec.row, rec.col), TapDifferentlyObject.objTypeFromString(rec.strValue1!!))
}