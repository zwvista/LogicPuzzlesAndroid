package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BalancedTapasDocument(context: Context) : GameDocument<BalancedTapasGameMove>(context) {
    override fun saveMove(move: BalancedTapasGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BalancedTapasGameMove(Position(rec.row, rec.col), BalancedTapasObject.objTypeFromString(rec.strValue1!!))
}
