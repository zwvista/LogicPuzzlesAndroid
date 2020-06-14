package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class BalancedTapasDocument : GameDocument<BalancedTapasGameMove>() {
    override fun saveMove(move: BalancedTapasGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BalancedTapasGameMove(Position(rec.row, rec.col), BalancedTapasObject.objTypeFromString(rec.strValue1!!))
}
