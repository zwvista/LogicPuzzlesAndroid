package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BusySeasDocument(context: Context) : GameDocument<BusySeasGameMove>(context) {
    override fun saveMove(move: BusySeasGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BusySeasGameMove(Position(rec.row, rec.col), BusySeasObject.objFromString(rec.strValue1!!))
}
