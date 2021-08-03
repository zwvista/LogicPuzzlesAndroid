package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PowerGridDocument(context: Context) : GameDocument<PowerGridGameMove>(context) {
    override fun saveMove(move: PowerGridGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        PowerGridGameMove(Position(rec.row, rec.col), PowerGridObject.objFromString(rec.strValue1!!))
}